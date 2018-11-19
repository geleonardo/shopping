package com.gudushidai.mall.web.admin;

import com.gudushidai.mall.entity.Classification;
import com.gudushidai.mall.entity.ProcuctImage;
import com.gudushidai.mall.entity.Product;
import com.gudushidai.mall.entity.ProductSpecifications;
import com.gudushidai.mall.entity.pojo.ResultBean;
import com.gudushidai.mall.service.ClassificationService;
import com.gudushidai.mall.service.ProductImageService;
import com.gudushidai.mall.service.ProductService;
import com.gudushidai.mall.service.ProductSpecificationsService;
import com.gudushidai.mall.utils.FileUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/product")
public class AdminProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ClassificationService classificationService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private ProductSpecificationsService productSpecificationsService;

    @Value("${AccessKeyID}")
    private String AccessKeyID;
    @Value("${AccessKeySecret}")
    private String AccessKeySecret;




    @RequestMapping("/toList.html")
    public String toList() {
        return "admin/product/list";
    }

    @RequestMapping("/toAdd.html")
    public String toAdd() {
        return "admin/product/add";
    }

    @RequestMapping("/toEdit.html")
    public String toEdit(int id, Map<String, Object> map) {
        Product product = productService.findById(id);
        Classification classification = classificationService.findById(product.getCsid());
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
        product.setCategorySec(classification);
        map.put("product", product);

        return "admin/product/edit";
    }

    @RequestMapping("/toaddSpecificationsEdit.html")
    public String toaddSpecificationsEdit(int id, Map<String, Object> map) {
        List<ProductSpecifications> specifications = productSpecificationsService.getByProduct(id);
        map.put("specifications",specifications);
        map.put("id",id);
        return "admin/product/specificationsEdit";
    }


    @ResponseBody
    @RequestMapping("/addSpecifications.do")
    public ResultBean<Boolean> addSpecifications(Integer id, String toAddSpeName, Integer toAddSpePrice) {
        productSpecificationsService.addSpecifications(id,toAddSpeName,toAddSpePrice);
        return new ResultBean<>(true);
    }

    @ResponseBody
    @RequestMapping("/changeSpecificationName.do")
    public ResultBean<Boolean> changeSpecificationName(Integer id, String new_type_name) {
        productSpecificationsService.changeSpecificationName(id,new_type_name);
        return new ResultBean<>(true);
    }

    @ResponseBody
    @RequestMapping("/changeSpecificationPrice.do")
    public ResultBean<Boolean> changeSpecificationPrice(Integer id, Integer new_type_price) {
        productSpecificationsService.changeSpecificationPrice(id,new_type_price);
        return new ResultBean<>(true);
    }



    @ResponseBody
    @RequestMapping("/delSpecifications.do")
    public ResultBean<Boolean> delSpecifications(Integer id) {
        productSpecificationsService.delSpecifications(id);
        return new ResultBean<>(true);
    }


    @ResponseBody
    @RequestMapping("/list.do")
    public ResultBean<List<Product>> listProduct(int pageindex,
                                                 @RequestParam(value = "pageSize", defaultValue = "15") int pageSize) {
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "id");
        Sort sort=new Sort(order);
        Pageable pageable = new PageRequest(pageindex, pageSize, sort);
        List<Product> list = productService.findAll(pageable).getContent();
        return new ResultBean<>(list);
    }

    @ResponseBody
    @RequestMapping("/getTotal")
    public ResultBean<Integer> getTotal() {
        Pageable pageable = new PageRequest(1, 15, null);
        int total = (int) productService.findAll(pageable).getTotalElements();
        return new ResultBean<>(total);
    }

    @RequestMapping("/del.do")
    @ResponseBody
    public ResultBean<Boolean> del(int id) {
        productService.delById(id);
        return new ResultBean<>(true);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/add.do")
    public void add(MultipartFile image,
                    MultipartFile image1,
                    MultipartFile image2,
                    MultipartFile image3,
                    String title,
                    Double marketPrice,
                    Double shopPrice,
                    int isHot,
                    String desc,
                    int csid,
                    HttpServletRequest request,
                    HttpServletResponse response) throws Exception {
        Product product = new Product();
        product.setTitle(title);
        product.setMarketPrice(marketPrice);
        product.setShopPrice(shopPrice);
        product.setDesc(desc);
        product.setIsHot(isHot);
        product.setCsid(csid);
        product.setPdate(new Date());
        String imgUrl = FileUtil.saveFile(image,AccessKeyID,AccessKeySecret);
        String imgUrl1 = FileUtil.saveFile(image1,AccessKeyID,AccessKeySecret);
        String imgUrl2 = FileUtil.saveFile(image2,AccessKeyID,AccessKeySecret);
        String imgUrl3 = FileUtil.saveFile(image3,AccessKeyID,AccessKeySecret);
        product.setImage(imgUrl);
        int id = productService.create(product);
        if (id <= 0) {
            request.setAttribute("message", "添加失败！");
            request.getRequestDispatcher("toAdd.html").forward(request, response);
        } else {
            //增加图片关联关系表，将图片链接存入
            if(!StringUtils.isEmpty(imgUrl1)){
                productImageService.create(id,imgUrl1);
            }
            if(!StringUtils.isEmpty(imgUrl2)){
                productImageService.create(id,imgUrl2);
            }
            if(!StringUtils.isEmpty(imgUrl3)){
                productImageService.create(id,imgUrl3);
            }
            //增加入库默认规格
            productSpecificationsService.createDefault(id,shopPrice);
            request.getRequestDispatcher("toEdit.html?id=" + id).forward(request, response);
        }
    }


    @RequestMapping(method = RequestMethod.POST, value = "/update.do")
    public void update(int id,
                       String title,
                       Double marketPrice,
                       Double shopPrice,
                       String desc,
                       int csid,
                       int isHot,
                       MultipartFile image,
                       MultipartFile image1,
                       MultipartFile image2,
                       MultipartFile image3,
                       String image_src1,
                       String image_src2,
                       String image_src3,
                       HttpServletRequest request,
                       HttpServletResponse response) throws Exception {
        Product product = productService.findById(id);
        product.setTitle(title);
        product.setMarketPrice(marketPrice);
        product.setShopPrice(shopPrice);
        product.setDesc(desc);
        product.setIsHot(isHot);
        product.setCsid(csid);
        product.setPdate(new Date());
        String imgUrl = FileUtil.saveFile(image,AccessKeyID,AccessKeySecret);
        String imgUrl1 = FileUtil.saveFile(image1,AccessKeyID,AccessKeySecret);
        String imgUrl2 = FileUtil.saveFile(image2,AccessKeyID,AccessKeySecret);
        String imgUrl3 = FileUtil.saveFile(image3,AccessKeyID,AccessKeySecret);
        if (StringUtils.isNotBlank(imgUrl)) {
            product.setImage(imgUrl);
        }
        boolean flag = false;
        try {
            productService.update(product);
            flag = true;
        } catch (Exception e) {
            throw new Exception(e);
        }
        if (!flag) {
            request.setAttribute("message", "更新失败！");
        }else {
            //增加图片关联关系表，将图片链接存入
            if(!StringUtils.isEmpty(imgUrl1)){
                productImageService.create(id,imgUrl1);
            }else {
                productImageService.create(id,image_src1==null?"":image_src1);
            }
            if(!StringUtils.isEmpty(imgUrl2)){
                productImageService.create(id,imgUrl2);
            }else {
                productImageService.create(id,image_src2==null?"":image_src2);
            }
            if(!StringUtils.isEmpty(imgUrl3)){
                productImageService.create(id,imgUrl3);
            }else {
                productImageService.create(id,image_src3==null?"":image_src3);
            }
        }
        response.sendRedirect("toList.html");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/img/{filename:.+}")
    public void getImage(@PathVariable(name = "filename", required = true) String filename,
                         HttpServletResponse res) throws IOException {
        File file = new File("file/" + filename);
        if (file != null && file.exists()) {
            res.setHeader("content-type", "application/octet-stream");
            res.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            res.setContentLengthLong(file.length());
            Files.copy(Paths.get(file.toURI()), res.getOutputStream());
        }
    }


}
