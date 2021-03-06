package com.gudushidai.mall.web.user;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.gudushidai.mall.config.AlipayConfig;
import com.gudushidai.mall.config.WeixinPayConfig;
import com.gudushidai.mall.service.OrderService;
import com.gudushidai.mall.wechat.PayCommonUtil;
import com.gudushidai.mall.wechat.WXPayUtil;
import com.gudushidai.mall.wechat.WeixinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.net.URLEncoder;
import java.util.*;

@Controller
@RequestMapping("/pay")
public class PayController {


    @Autowired
    private OrderService orderService;

    @Autowired
    private WeixinService weixinService;

    /**
     * 打开订单列表页面
     *
     * @return
     */
    @RequestMapping("/orderPay.html")
    public String toOrderPay(HttpServletRequest request,HttpServletResponse response, Double total, int orderid , Model model) throws Exception {
        System.out.println(orderid+"               订单id和金额                  "+total);
        model.addAttribute("total",total);
        model.addAttribute("orderid",orderid);
        String ua = (request).getHeader("user-agent")
                .toLowerCase();
        if (ua.indexOf("micromessenger") > 0) {// 是微信浏览器
            //先获得code
//            response.sendRedirect("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
//                    ConstantUtil.APP_ID +
//                    "&redirect_uri=" +
//                    URLEncoder.encode("http://www.gudushidai.com/mall/pay/orderPayWX.html?total="+total+"&orderid="+orderid) +
//                    "&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect");
            model.addAttribute("return_url",URLEncoder.encode("http://www.gudushidai.com/mall/pay/orderPayWX.html?total="+total+"&orderid="+orderid));
            model.addAttribute("appid", WeixinPayConfig.APP_ID);
            return "mall/pay/wxorderPay";
        }else {
            return "mall/pay/orderPay";
        }
    }

    @RequestMapping("/orderPayWX.html")
    public String toOrderPayWX(HttpServletRequest request,HttpServletResponse response, Double total, int orderid ,String code, Model model) throws Exception {
        Map<String,String> map = weixinService.createOrder(request,response,total,orderid,code);
        System.out.println("这里输出微信返回的map对象=================================================这里输出微信返回的map对象");
        System.out.println(map+"");
        System.out.println("这里输出微信返回的map对象=================================================这里输出微信返回的map对象");
        model.addAttribute("map",map);
        return "mall/pay/wxorderPayGo";
    }


    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
           if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
               ip = request.getHeader("HTTP_CLIENT_IP");
           }
          if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
             ip = request.getHeader("HTTP_X_FORWARDED_FOR");
          }
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
             ip = request.getRemoteAddr();
         }
        return ip;
    }


    @RequestMapping("/gotoPay")
    public void gotoPay( HttpServletRequest request, HttpServletResponse response) throws  Exception {
        if(request.getParameter("WIDout_trade_no")!=null){
            // 商户订单号，商户网站订单系统中唯一订单号，必填
            String out_trade_no = new String(request.getParameter("WIDout_trade_no").getBytes("ISO-8859-1"),"UTF-8");
            // 订单名称，必填
//            String subject = new String(request.getParameter("WIDsubject").getBytes("ISO-8859-1"),"UTF-8");
            String subject = new String(request.getParameter("WIDsubject"));
            System.out.println(subject);
            // 付款金额，必填
            String total_amount=new String(request.getParameter("WIDtotal_amount").getBytes("ISO-8859-1"),"UTF-8");
            // 商品描述，可空
            String body = new String(request.getParameter("WIDbody"));
            // 超时时间 可空
            String timeout_express="2m";
            // 销售产品码 必填
            String product_code="QUICK_WAP_WAY";
            /**********************/
            // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
            //调用RSA签名方式
            AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
            AlipayTradeWapPayRequest alipay_request=new AlipayTradeWapPayRequest();

            // 封装请求支付信息
            AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
            model.setOutTradeNo(out_trade_no);
            model.setSubject(subject);
            model.setTotalAmount(total_amount);
            model.setBody(body);
            model.setTimeoutExpress(timeout_express);
            model.setProductCode(product_code);
            alipay_request.setBizModel(model);
            // 设置异步通知地址
            alipay_request.setNotifyUrl(AlipayConfig.notify_url);
            // 设置同步地址
            alipay_request.setReturnUrl(AlipayConfig.return_url);

            // form表单生产
            String form = "";
            try {
                // 调用SDK生成表单
                form = client.pageExecute(alipay_request).getBody();
                response.setContentType("text/html;charset=" + AlipayConfig.CHARSET);
                response.getWriter().write(form);//直接将完整的表单html输出到页面
                response.getWriter().flush();
                response.getWriter().close();
            } catch (AlipayApiException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    /**
     * 打开订单列表页面
     *
     * @return
     */
    @RequestMapping("/orderPayNotify")
    public String orderPayNotify(HttpServletRequest request,HttpServletResponse response) throws Exception {
        System.out.println("========================================receive order notify======================================================");
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
//            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
//            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            System.out.println("here print out valueStr:::::::::::"+valueStr);
            params.put(name, valueStr);
        }
        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        //商户订单号

        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
        System.out.println("here print out out_trade_no:::::::::::"+out_trade_no);
        //支付宝交易号

        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
        System.out.println("here print out trade_no:::::::::::"+trade_no);
        //交易状态
        String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
        System.out.println("here print out trade_status:::::::::::"+trade_status);
        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
        //计算得出通知验证结果
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        boolean verify_result = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, "RSA2");
        System.out.println("here print out verify_result:::::::::::"+verify_result);
        if(verify_result){//验证成功
            //////////////////////////////////////////////////////////////////////////////////////////
            //请在这里加上商户的业务逻辑程序代码

            //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
            System.out.println("================================================="+trade_status);
            if(trade_status.equals("TRADE_FINISHED")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //如果签约的是可退款协议，退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
                //如果没有签约可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
            } else if (trade_status.equals("TRADE_SUCCESS")){
                System.out.println("===================================="+"TRADE_SUCCESS");
                //支付成功  将订单状态修改
                System.out.println("===================================="+"将订单状态修改成未发货状态");
                int status = orderService.getOrderStatus(out_trade_no);
                if(status==1){
                    orderService.updateStatus(Integer.parseInt(out_trade_no),2);
                }

                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //如果签约的是可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
            }

            //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
//            out.clear();
//            out.println("success");	//请不要修改或删除
            return "success";
            //////////////////////////////////////////////////////////////////////////////////////////
        }else{//验证失败
            //out.println("fail");
            return "fail";
        }
    }


    @RequestMapping("/wxorderPayNotify")
    public String wxorderPayNotify(HttpServletRequest request,HttpServletResponse response) throws Exception {
        BufferedReader reader = null;

        reader = request.getReader();
        String line = "";
        String xmlString = null;
        StringBuffer inputString = new StringBuffer();

        while ((line = reader.readLine()) != null) {
            inputString.append(line);
        }
        xmlString = inputString.toString();
        request.getReader().close();
        System.out.println("----接收到的数据如下：---" + xmlString);
        Map<String, String> map = new HashMap<String, String>();
        String result_code = "";
        String return_code = "";
        String out_trade_no = "";
        map = WXPayUtil.xmlToMap(xmlString);
        result_code = map.get("result_code");
        out_trade_no = map.get("out_trade_no");
        return_code = map.get("return_code");

        System.out.println(checkSign(xmlString));

        if (checkSign(xmlString)) {
            System.out.println("====================================验签成功====================================");
            //更新数据库
            int status = orderService.getOrderStatus(out_trade_no);
            if(status==1){
                orderService.updateStatus(Integer.parseInt(out_trade_no),2);
            }
            return returnXML(result_code);
        } else {
            return returnXML("FAIL");
        }
    }

    private boolean checkSign(String xmlString) {
        Map<String, String> map = null;
        try {
            map = WXPayUtil.xmlToMap(xmlString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String signFromAPIResponse = map.get("sign").toString();
        if (signFromAPIResponse == "" || signFromAPIResponse == null) {
            System.out.println("API返回的数据签名数据不存在，有可能被第三方篡改!!!");
            return false;
        }
        System.out.println("服务器回包里面的签名是:" + signFromAPIResponse);
        //清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
        map.put("sign", "");
        //将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
        String signForAPIResponse = getSign(map);
        if (!signForAPIResponse.equals(signFromAPIResponse)) {
            //签名验不过，表示这个API返回的数据有可能已经被篡改了
            System.out.println("API返回的数据签名验证不通过，有可能被第三方篡改!!! signForAPIResponse生成的签名为" + signForAPIResponse);
            return false;
        }
        System.out.println("恭喜，API返回的数据签名验证通过!!!");
        return true;
    }

    public String getSign(Map<String, String> map) {
        SortedMap<String, String> signParams = new TreeMap<String, String>();
        for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
            signParams.put(stringStringEntry.getKey(), stringStringEntry.getValue());
        }
        signParams.remove("sign");
        String sign = PayCommonUtil.createSign("UTF-8", signParams);
        return sign;
    }

    private String returnXML(String return_code) {
        return "<xml><return_code><![CDATA["
                + return_code
                + "]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    }

}
