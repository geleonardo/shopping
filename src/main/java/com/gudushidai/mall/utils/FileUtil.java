package com.gudushidai.mall.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class FileUtil {

    private static String httpbilibili = "https://api.vc.bilibili.com/api/v1/drawImage/upload";

    private static String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";

    private static String osskey = "http://gudushidai.oss-cn-hangzhou.aliyuncs.com/";

    private static String bucketname = "gudushidai";


    /**
     * 保存上传的文件
     *
     * @return 文件下载的url
     * @throws Exception
     */
    public static String saveFile(MultipartFile multipartFile,String AccessKeyID,String AccessKeySecret) throws Exception {
        if (multipartFile == null || multipartFile.isEmpty())
            return "";
        //将file 上传到 bilibili
        String imgurl = "";

        try {
            imgurl = uploadToAliyun(multipartFile,AccessKeyID,AccessKeySecret);
            //imgurl = uploadToBili(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  imgurl;

//        File target = new File("file");
//        if (!target.isDirectory()) {
//            target.mkdirs();
//        }
//        String originalFilename = file.getOriginalFilename();
//        MessageDigest md = MessageDigest.getInstance("MD5");
//        md.update(file.getBytes());
//        String fileName = (Helper.bytesToHex(md.digest(),0,md.digest().length-1)) + "." + getPostfix(originalFilename);
//        System.out.println("===================================================================");
//        System.out.println("===================================================================");
//        System.out.println("===================================================================");
//        System.out.println("===================================================================");
//        System.out.println("===================================================================");
//        System.out.println("==================================================================="+fileName);
//        try{
//            File file1 = new File(target.getPath() + "/" + fileName);
//            Files.write(Paths.get(file1.toURI()), file.getBytes(), StandardOpenOption.CREATE_NEW);
//        }catch (FileAlreadyExistsException e){
//            return "/mall/admin/product/img/" + fileName;
//        }
//        return "/mall/admin/product/img/" + fileName;
    }

    private static String uploadToAliyun(MultipartFile file,String AccessKeyID,String AccessKeySecret) {
        String imgurl = "";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = AccessKeyID;
        String accessKeySecret = AccessKeySecret;
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String[] names = file.getOriginalFilename().split("[.]");
        String name = uuid + "." + names[names.length - 1];
        String foldernow = "product/"+new SimpleDateFormat("yyyyMMdd").format(new Date()).toString()+ "/";

        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 上传文件。<yourLocalFile>由本地文件路径加文件名包括后缀组成，例如/users/local/myfile.txt。
        try {
            ossClient.putObject(bucketname, foldernow + name, file.getInputStream());
        }catch (Exception e){
            e.printStackTrace();
        }
        // 关闭OSSClient。
        ossClient.shutdown();
        imgurl = osskey + foldernow + name;
        System.out.println(imgurl);
        return imgurl;

    }

    private static String uploadToBili(File file) {
        String imgurl = "";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        try {
            HttpPost httppost = new HttpPost(httpbilibili);
            httppost.setHeader("Cookie","SESSDATA=24e458c9%2C1543379825%2Cec8c3aef;");
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(200000).setSocketTimeout(200000).build();
            httppost.setConfig(requestConfig);

            FileBody bin = new FileBody(file);
            StringBody comment = new StringBody("This is comment", ContentType.TEXT_PLAIN);

            HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("file_up", bin).addPart("comment", comment).build();

            httppost.setEntity(reqEntity);

            System.out.println("executing request " + httppost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                System.out.println(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String responseEntityStr = EntityUtils.toString(response.getEntity());
                    JSONObject jsonObject = JSON.parseObject(responseEntityStr);
                    if(jsonObject.getJSONObject("data").getString("image_url").length()>0){
                        imgurl = jsonObject.getJSONObject("data").getString("image_url");
                        System.out.println("=====================================================");
                        System.out.println("=====================================================");
                        System.out.println("=====================================================");
                        System.out.println(imgurl);
                        System.out.println("=====================================================");
                        System.out.println("=====================================================");
                        System.out.println("=====================================================");
                    }
                    System.out.println(responseEntityStr);
                    System.out.println("Response content length: " + resEntity.getContentLength());
                }
                EntityUtils.consume(resEntity);
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        return imgurl;
    }

    /**
     * 获得文件的后缀名
     *
     * @param fileName
     * @return
     */
    public static String getPostfix(String fileName) {
        if (fileName == null || "".equals(fileName.trim())) {
            return "";
        }
        if (fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        }
        return "";
    }

}
