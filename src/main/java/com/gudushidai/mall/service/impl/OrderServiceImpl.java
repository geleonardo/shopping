package com.gudushidai.mall.service.impl;

import com.gudushidai.mall.dao.OrderMapper;
import com.gudushidai.mall.service.exception.LoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gudushidai.mall.dao.OrderDao;
import com.gudushidai.mall.dao.OrderItemDao;
import com.gudushidai.mall.dao.ProductDao;
import com.gudushidai.mall.entity.Order;
import com.gudushidai.mall.entity.OrderItem;
import com.gudushidai.mall.entity.Product;
import com.gudushidai.mall.entity.User;
import com.gudushidai.mall.service.OrderService;
import com.gudushidai.mall.service.ShopCartService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ShopCartService shopCartService;

    @Autowired
    private OrderMapper orderMapper;


    @Override
    public Order findById(int id) {
        return orderDao.getOne(id);
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return orderDao.findAll(pageable);
    }

    @Override
    public List<Order> findAllExample(Example<Order> example) {
        return orderDao.findAll(example);
    }

    @Override
    public void update(Order order) {
        orderDao.save(order);
    }

    @Override
    public int create(Order order) {
        Order order1 = orderDao.save(order);
        return order1.getId();
    }

    @Override
    public void delById(int id) {
        orderDao.delete(id);
    }

    /**
     * 查询订单项详情
     * @param orderId
     * @return
     */
    @Override
    public List<OrderItem> findItems(int orderId) {
        List<OrderItem> list = orderItemDao.findByOrderId(orderId);
        for (OrderItem orderItem : list) {
            Product product = productDao.findOne(orderItem.getProductId());
            orderItem.setProduct(product);
        }
        return list;
    }

    /**
     * 更改订单状态
     *
     * @param id
     * @param status
     */
    @Override
    public void updateStatus(int id, int status) {
        Order order = orderDao.findOne(id);
        order.setState(status);
        orderDao.save(order);
    }

    /**
     * 查找用户的订单列表
     *
     * @param request
     * @return
     */
    @Override
    public List<Order> findUserOrder(HttpServletRequest request,int page,int rows,int state) {
        //从session中获取登录用户的id，查找他的订单
        Object user = request.getSession().getAttribute("user");
        if (user == null)
            throw new LoginException("请登录！");
        User loginUser = (User) user;
//        List<Order> orders = orderDao.findByUserId(loginUser.getId());
        int start = page*rows;
        List<Order> orders = new ArrayList<>();
        if(state==0){
            orders = orderMapper.findByUserId(loginUser.getId(),start,rows);
        }else {
            orders = orderMapper.findByUserIdAndState(loginUser.getId(),start,rows,state);
        }

        return orders;
    }

    /**
     * 支付
     *
     * @param orderId
     */
    @Override
    public void pay(int orderId) {
        //具体逻辑就不实现了，直接更改状态为 待发货
        Order order = orderDao.findOne(orderId);
        if (order == null)
            throw new RuntimeException("订单不存在");
        orderDao.updateState(STATE_WAITE_SEND,order.getId());
    }

    /**
     * 提交订单
     *
     * @param name
     * @param phone
     * @param addr
     * @param request
     * @param response
     */
    @Override
    @Transactional
    public void submit(String name, String phone, String addr, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object user = request.getSession().getAttribute("user");
        if (user == null)
            throw new LoginException("请登录！");
        User loginUser = (User) user;
        Order order = new Order();
        order.setName(name);
        order.setPhone(phone);
        order.setAddr(addr);
        order.setOrderTime(new Date());
        order.setUserId(loginUser.getId());
        order.setState(STATE_NO_PAY);
        List<OrderItem> orderItems = shopCartService.listCart(request);
        Double total = 0.0;
        order.setTotal(total);
        order.setCreateAt(new Date());
        order.setUpdateAt(new Date());
        order = orderDao.save(order);
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrderId(order.getId());
            total += orderItem.getSubTotal();
            orderItemDao.save(orderItem);
        }
        order.setTotal(total);
        orderDao.save(order);
        //删除购物车中的内容
        shopCartService.deleteMyCart(loginUser.getId());
        //重定向到订单列表页面
//        response.sendRedirect("/mall/order/toList.html");
        System.out.println(order.getId());
        //跳转到支付页面
        //http://abc.gudushidai.com/mall/pay/orderPay.html?total=88.0&orderid=18
        response.sendRedirect("/mall/pay/orderPay.html?total="+total+"&orderid="+order.getId());
    }

    /**
     * 确认收货
     *
     * @param orderId
     */
    @Override
    public void receive(int orderId) {
        Order order = orderDao.findOne(orderId);
        if (order == null)
            throw new RuntimeException("订单不存在");
        orderDao.updateState(STATE_COMPLETE,order.getId());
    }

    @Override
    public List<Order> findAllOrder(int pageindex, int pageSize) {
        int start = pageindex*pageSize;
        int rows = pageSize;
        return orderMapper.findAllOrder(start,rows);
    }

    @Override
    public void updateDeliverId(int orderId, String deliverId) {
        orderMapper.updateDeliverId(orderId,deliverId);
    }
}
