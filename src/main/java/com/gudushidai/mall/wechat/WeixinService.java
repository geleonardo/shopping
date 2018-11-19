package com.gudushidai.mall.wechat;

import com.alibaba.fastjson.JSONObject;
import com.gudushidai.mall.config.WeixinPayConfig;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

@Service
public class WeixinService {


    public Map<String, String> createOrder(HttpServletRequest request, HttpServletResponse response, Double total, int orderid,String code) {
        try {
            //页面获取openId接口
            String getopenid_url = "https://api.weixin.qq.com/sns/oauth2/access_token";
            String  param=
                    "appid="+ WeixinPayConfig.APP_ID+"&secret="+WeixinPayConfig.APP_SECRET+"&code="+code+"&grant_type=authorization_code";
            //向微信服务器发送get请求获取openIdStr
            String openIdStr = HttpRequest.sendGet(getopenid_url, param);
            JSONObject json = JSONObject.parseObject(openIdStr);//转成Json格式
            String openId = json.getString("openid");//获取openId

            //拼接统一下单地址参数
            Map<String, String> paraMap = new HashMap<String, String>();
            //获取请求ip地址
            String ip = request.getHeader("x-forwarded-for");
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
                ip = request.getHeader("Proxy-Client-IP");
            }
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
                ip = request.getRemoteAddr();
            }
            if(ip.indexOf(",")!=-1){
                String[] ips = ip.split(",");
                ip = ips[0].trim();
            }

            paraMap.put("appid", WeixinPayConfig.APP_ID);
            paraMap.put("body", "杂货铺订单");
            paraMap.put("mch_id", WeixinPayConfig.MCH_ID);
            paraMap.put("nonce_str", WXPayUtil.generateNonceStr());
            paraMap.put("openid", openId);
            paraMap.put("out_trade_no", ""+orderid);//订单号
            paraMap.put("spbill_create_ip", ip);
            System.out.println("这里输出预支付订单的价格============================="+(Math.round(total*100))+"");
            paraMap.put("total_fee",(Math.round(total*100))+"");
            paraMap.put("trade_type", "JSAPI");
            paraMap.put("notify_url",WeixinPayConfig.WEIXIN_NOTIFY);// 此路径是微信服务器调用支付结果通知路径
            String sign = WXPayUtil.generateSignature(paraMap, WeixinPayConfig.SHOP_API_SECRET);
            paraMap.put("sign", sign);
            String xml = WXPayUtil.mapToXml(paraMap);//将所有参数(map)转xml格式

            // 统一下单 https://api.mch.weixin.qq.com/pay/unifiedorder
            String unifiedorder_url = "https://api.mch.weixin.qq.com/pay/unifiedorder";

            String xmlStr = HttpRequest.sendPost(unifiedorder_url, xml);//发送post请求"统一下单接口"返回预支付id:prepay_id
            System.out.println("这里输出预支付订单map=================================");
            System.out.println(xmlStr);
            System.out.println("这里输出预支付订单map=================================");
            //以下内容是返回前端页面的json数据
            String prepay_id = "";//预支付id
            if (xmlStr.indexOf("SUCCESS") != -1) {
                Map<String, String> map = WXPayUtil.xmlToMap(xmlStr);
                prepay_id = (String) map.get("prepay_id");
            }
            Map<String, String> payMap = new HashMap<String, String>();
            payMap.put("appId", WeixinPayConfig.APP_ID);
            payMap.put("timeStamp", WXPayUtil.getCurrentTimestamp()+"");
            payMap.put("nonceStr", WXPayUtil.generateNonceStr());
            payMap.put("signType", "MD5");
            payMap.put("package", "prepay_id=" + prepay_id);
            String paySign = WXPayUtil.generateSignature(payMap, WeixinPayConfig.SHOP_API_SECRET);
            payMap.put("paySign", paySign);
            return payMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


}
