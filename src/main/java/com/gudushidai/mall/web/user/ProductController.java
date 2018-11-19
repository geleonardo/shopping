package com.gudushidai.mall.web.user;

import com.gudushidai.mall.entity.*;
import com.gudushidai.mall.entity.pojo.ResultBean;
import com.gudushidai.mall.service.*;
import com.gudushidai.mall.service.exception.LoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.gudushidai.mall.entity.*;
import com.gudushidai.mall.service.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ClassificationService classificationService;
    @Autowired
    private ShopCartService shopCartService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private ProductSpecificationsService productSpecificationsService;



    /**
     * 获取商品信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/get.do")
    public ResultBean<Product> getProduct(int id) {
        Product product = productService.findById(id);
        return new ResultBean<>(product);
    }

    /**
     * 打开商品详情页面
     *
     * @param id
     * @param map
     * @return
     */
    @RequestMapping("/get.html")
    public String toProductPage(int id, Map<String, Object> map) {
        Product product = productService.findById(id);
        List<ProcuctImage> procuctImages = productImageService.getImages(id);
        if(procuctImages!=null&&procuctImages.size()>0){
            for(int i=0;i<procuctImages.size();i++){
                if(i==2){
                    map.put("procuctImage1", procuctImages.get(i));
                }
                if(i==1){
                    map.put("procuctImage2", procuctImages.get(i));
                }
                if(i==0){
                    map.put("procuctImage3", procuctImages.get(i));
                }
            }
        }
        List<ProductSpecifications> specificationsList = productSpecificationsService.getByProduct(id);
        if(specificationsList!=null&&specificationsList.size()>0){
            product.setShopPrice(specificationsList.get(0).getType_price().doubleValue());
        }
        map.put("specificationsList", specificationsList);
        map.put("product", product);
        return "mall/product/info";
    }

    /**
     * 查找热门商品
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/hot.do")
    public ResultBean<List<Product>> getHotProduct() {
        List<Product> products = productService.findHotProduct();
        return new ResultBean<>(products);
    }

    /**
     * 查找最新商品
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("/new.do")
    public ResultBean<List<Product>> getNewProduct(int pageNo, int pageSize) {
        Pageable pageable = new PageRequest(pageNo, pageSize);
        List<Product> products = productService.findNewProduct(pageable);
        return new ResultBean<>(products);
    }

    /**
     * 查找所有商品
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("/all.do")
    public ResultBean<List<Product>> getAllProduct(int pageNo, int pageSize) {
        Pageable pageable = new PageRequest(pageNo, pageSize);
        List<Product> products = productService.findAllProduct(pageNo,pageSize);
        return new ResultBean<>(products);
    }

    /**
     * 查找所有商品
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("/searchall.do")
    public ResultBean<List<Product>> searchAllProduct(int pageNo, int pageSize,String good_name) {
        List<Product> products = productService.searchAllProduct(pageNo,pageSize,good_name);
        return new ResultBean<>(products);
    }


    /**
     * 打开分类查看商品页面
     *
     * @return
     */
    @RequestMapping("/category.html")
    public String toCatePage(int cid, Map<String, Object> map) {
        Classification classification = classificationService.findById(cid);
        map.put("category", classification);
        return "mall/product/category";
    }

    @RequestMapping("/toCart.html")
    public String toCart(Map<String, Object> map,HttpServletRequest request){
        Object user = request.getSession().getAttribute("user");
        if (user == null)
            throw new LoginException("请登录！");
        User loginUser = (User) user;
        map.put("user",user);
        return "mall/product/cart";
    }

    /**
     * 按一级分类查找商品
     *
     * @param cid
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("/category.do")
    public ResultBean<List<Product>> getCategoryProduct(int cid, int pageNo, int pageSize) {
        Pageable pageable = new PageRequest(pageNo, pageSize);
        List<Product> products = productService.findByCid(cid, pageable);
        return new ResultBean<>(products);
    }

    /**
     * 按二级分类查找商品
     *
     * @param csId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("/categorySec.do")
    public ResultBean<List<Product>> getCategorySecProduct(int csId, int pageNo, int pageSize) {
        Pageable pageable = new PageRequest(pageNo, pageSize);
        List<Product> products = productService.findByCsid(csId, pageable);
        return new ResultBean<>(products);
    }

    /**
     * 根据一级分类查询它所有的二级分类
     * @param cid
     * @return
     */
    @ResponseBody
    @RequestMapping("/getCategorySec.do")
    public ResultBean<List<Classification>> getCategorySec(int cid){
        List<Classification> list = classificationService.findByParentId(cid);
        return new ResultBean<>(list);
    }

    /**
     * 加购物车
     *
     * @param productId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/addCart.do")
    public ResultBean<Boolean> addToCart(int productId,int product_specificationId,int good_num, HttpServletRequest request) throws Exception {
        System.out.println(productId+"                 "+product_specificationId+"                      "+good_num+"                        ");
        shopCartService.addCart(productId,product_specificationId,good_num, request);
        return new ResultBean<>(true);
    }

    /**
     * 移除购物车
     *
     * @param productId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/delCart.do")
    public ResultBean<Boolean> delToCart(int cartId, HttpServletRequest request) throws Exception {
        shopCartService.remove(cartId, request);
        return new ResultBean<>(true);
    }

    /**
     * 查看购物车商品
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/listCart.do")
    public ResultBean<List<OrderItem>> listCart(HttpServletRequest request) throws Exception {
        List<OrderItem> orderItems = shopCartService.listCart(request);
        return new ResultBean<>(orderItems);
    }


}
