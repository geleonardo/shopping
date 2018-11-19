package com.gudushidai.mall.service.impl;

import com.gudushidai.mall.dao.ProductCarMapper;
import com.gudushidai.mall.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gudushidai.mall.service.ProductService;
import com.gudushidai.mall.service.ProductSpecificationsService;
import com.gudushidai.mall.service.ShopCartService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author hfb
 * @date 2017/11/21
 */
@Service
public class ShopCartServiceImpl implements ShopCartService {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCarMapper productCarMapper;
    @Autowired
    private ProductSpecificationsService productSpecificationsService;


    /**
     * 加购物车
     * 将商品id保存到Session中List<Integer>中
     *
     * @param productId
     * @param request
     */
    @Override
    public void addCart(int productId,int product_specificationId,int good_num, HttpServletRequest request) throws Exception {
        User loginUser = (User) request.getSession().getAttribute("user");
        if (loginUser == null)
            throw new Exception("未登录！请重新登录");
        //购物车入库
        productCarMapper.addCart(loginUser.getId(),productId,product_specificationId,good_num);



//        List<Integer> productIds = (List<Integer>) request.getSession().getAttribute(NAME_PREFIX + loginUser.getId());
//        if (productIds == null) {
//            productIds = new ArrayList<>();
//            request.getSession().setAttribute(NAME_PREFIX + loginUser.getId(), productIds);
//        }
//        productIds.add(productId);
    }

    /**
     * 移除
     *
     * 移除session List中对应的商品Id
     *
     * @param request
     */
    @Override
    public void remove(int cartId, HttpServletRequest request) throws Exception {
        User loginUser = (User) request.getSession().getAttribute("user");
        if (loginUser == null)
            throw new Exception("未登录！请重新登录");
        productCarMapper.remove(loginUser.getId(),cartId);


//        List<Integer> productIds = (List<Integer>) request.getSession().getAttribute(NAME_PREFIX + loginUser.getId());
//        Iterator<Integer> iterator = productIds.iterator();
//        while (iterator.hasNext()) {
//            if (productId == iterator.next()) {
//                iterator.remove();
//            }
//        }
    }

    /**
     * 查看购物车
     *
     * 查询出session的List中所有的商品Id,并封装成List<OrderItem>返回
     *
     * @param request
     * @return
     */
    @Override
    public List<OrderItem> listCart(HttpServletRequest request) throws Exception {
        User loginUser = (User) request.getSession().getAttribute("user");
        if (loginUser == null)
            throw new Exception("未登录！请重新登录");

        List<ShopCart> userShopCart = productCarMapper.getUserShopCart(loginUser.getId());
        Map<Integer, OrderItem> productMap = new HashMap<>();
        if(userShopCart==null||userShopCart.size()==0){
            return new ArrayList<>();
        }
        List<OrderItem> orderItems = new ArrayList<>();
        for(ShopCart shopCart:userShopCart){
            try {
                Product product = productService.findById(shopCart.getProductId().intValue());
                //查询分类价格
                ProductSpecifications productSpecifications = productSpecificationsService.getBySpecificationId(shopCart.getProduct_specificationId());
                OrderItem orderItem = new OrderItem();
                orderItem.setCartId(shopCart.getId().intValue());
                orderItem.setProduct(product);
                orderItem.setProductId(shopCart.getProductId().intValue());
                orderItem.setCount(shopCart.getGood_num());
                orderItem.setTypeName(productSpecifications.getType_name());
                orderItem.setSinglePrice(productSpecifications.getType_price().doubleValue());
                orderItem.setSubTotal(shopCart.getGood_num()*productSpecifications.getType_price().doubleValue());
                orderItems.add(orderItem);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return orderItems;

//        List<Integer> productIds = (List<Integer>) request.getSession().getAttribute(NAME_PREFIX + loginUser.getId());
//        // key: productId value:OrderItem
//        Map<Integer, OrderItem> productMap = new HashMap<>();
//        if (productIds == null){
//            return new ArrayList<>();
//        }
//        // 遍历List中的商品id，每个商品Id对应一个OrderItem
//        for (Integer productId : productIds) {
//            if (productMap.get(productId) == null) {
//                Product product = productService.findById(productId);
//                OrderItem orderItem = new OrderItem();
//                orderItem.setProduct(product);
//                orderItem.setProductId(productId);
//                orderItem.setCount(1);
//                orderItem.setSubTotal(product.getShopPrice());
//                productMap.put(productId, orderItem);
//            } else {
//                OrderItem orderItem = productMap.get(productId);
//                int count = orderItem.getCount();
//                orderItem.setCount(++count);
//                Double subTotal = orderItem.getSubTotal();
//                orderItem.setSubTotal(orderItem.getSubTotal()+subTotal);
//                productMap.put(productId, orderItem);
//            }
//        }
//        List<OrderItem> orderItems = new ArrayList<>(productMap.values());
//        return orderItems;
    }

    @Override
    public void deleteMyCart(Integer userid) {
        productCarMapper.deleteMyCart(userid);
    }
}
