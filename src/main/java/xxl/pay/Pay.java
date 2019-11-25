package xxl.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import xxl.mathematica.io.ExportString;
import xxl.mathematica.io.Import;
import xxl.mathematica.io.ImportString;
import xxl.wxpay.WXPay;
import xxl.wxpay.WXPayUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 支付
 */
public class Pay {

    private static Map<String, AlipayClient> aliMap = new HashMap<>();
    private static Map<String, WXPay> payMap = new HashMap<>();

    /**
     * 获取下单二维码
     *
     * @param appId          应用ID
     * @param outTradeNo     商家交易单号
     * @param totalAmount    总金额，以分为单位
     * @param subject        商品明细
     * @param storeId        商家门店编号
     * @param timeoutExpress 交易超时时间
     * @return
     */
    public static Map<String, String> aliBarcode(String appId, String outTradeNo, long totalAmount, String subject, String body, String storeId, String timeoutExpress, String notifyUrl) {
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setNotifyUrl(notifyUrl);
        Map<String, String> map = new HashMap<>();
        map.put("out_trade_no", outTradeNo);
        map.put("total_amount", String.format(Locale.CHINA, "%.2f", totalAmount / 100f));
        map.put("subject", subject);
        map.put("body", body);
        map.put("store_id", storeId);
        map.put("timeout_express", timeoutExpress);
        request.setBizContent(ExportString.exportStringJson(map));
        try {
            AlipayTradePrecreateResponse response = getAli(appId).execute(request);
            return ExportString.exportStringMap(response);
        } catch (AlipayApiException e) {
            return null;
        }
    }

    /**
     * 取消订单
     *
     * @param appId
     * @param tradeNo
     * @param outTradeNo
     * @return
     */
    public static Map<String, String> aliOrderCancel(String appId, String tradeNo, String outTradeNo) {
        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
        Map<String, String> map = new HashMap<>();
        if (tradeNo != null) {
            map.put("trade_no", tradeNo);
        } else {
            map.put("out_trade_no", outTradeNo);
        }
        request.setBizContent(ExportString.exportStringJson(map));
        try {
            AlipayTradeCancelResponse response = getAli(appId).execute(request);
            Map<String, String> res = new HashMap<>();
            res.put("", response.getAction());
            res.put("", response.getOutTradeNo());
            res.put("", response.getRefundSettlementId());
            return res;
        } catch (AlipayApiException e) {
            return null;
        }
    }

    /**
     * 根据阿里订单号或者商家订单号查询订单
     *
     * @param appId
     * @param tradeNo    阿里订单号
     * @param outTradeNo 商家订单号
     * @return
     */
    public static Map<String, Object> aliOrderQuery(String appId, String tradeNo, String outTradeNo) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        Map<String, String> map = new HashMap<>();
        if (tradeNo != null) {
            map.put("trade_no", tradeNo);
        } else {
            map.put("out_trade_no", outTradeNo);
        }
        request.setBizContent(ExportString.exportStringJson(map));
        try {
            return ImportString.importStringMapObject(getAli(appId).execute(request).getBody());
        } catch (AlipayApiException e) {
            return null;
        }
    }

    /**
     * 根据阿里订单号或者商家订单号退款
     *
     * @param appId
     * @param tradeNo
     * @param outTradeNo
     * @return
     */
    public static Map<String, Object> aliOrderRefund(String appId, String tradeNo, String outTradeNo, long refundMoney, String refundReason) {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        Map<String, String> map = new HashMap<>();
        if (tradeNo != null) {
            map.put("trade_no", tradeNo);
        } else {
            map.put("out_trade_no", outTradeNo);
        }
        map.put("refund_amount", String.format(Locale.CHINA, "%.2f", refundMoney / 100f));
        map.put("refund_reason", refundReason);
        request.setBizContent(ExportString.exportStringJson(map));
        try {
            return ImportString.importStringMapObject(getAli(appId).execute(request).getBody());
        } catch (AlipayApiException e) {
            return null;
        }
    }

    /**
     * 注册阿里平台信息
     *
     * @param serverUrl
     * @param appId      程序ID
     * @param privateKey 私钥（建议使用工具生成）
     * @param publicKey  阿里公钥
     */
    public static void registerAli(String serverUrl, String appId, String privateKey, String publicKey) {
        AlipayClient client = aliMap.get(appId);
        if (client == null) {
            aliMap.put(appId, new DefaultAlipayClient(serverUrl, appId, privateKey, "json", "UTF-8", publicKey, "RSA2"));
        }
    }

    /**
     * 注册微信平台信息
     *
     * @param mchId    商户平台账号
     * @param certFile 证书文件
     * @param appId    公众平台账号
     * @param key      商户平台校验值
     */
    public static void registerWx(String mchId, String certFile, String appId, String key) {
        WXPay wxPay = payMap.get(mchId);
        if (wxPay == null) {
            payMap.put(mchId, new WXPay(new SimpleWxConfig(appId, certFile, key, mchId), null, false));
        }
    }

    /**
     * 微信支付二维码
     *
     * @param mchId
     * @param outTradeNo
     * @param money
     * @param goods
     * @param detail
     * @param deviceInfo
     * @param ip
     * @param notifyUrl
     * @return
     */
    public static Map<String, String> wxBarcode(String mchId, String outTradeNo, long money, String goods, String detail, String deviceInfo, String ip, String notifyUrl) {
        Map<String, String> map = new HashMap<>();
        map.put("out_trade_no", outTradeNo);
        map.put("total_fee", String.valueOf(money));
        map.put("body", goods);
        map.put("detail", detail);
        map.put("device_info", deviceInfo);
        map.put("spbill_create_ip", ip);
        map.put("trade_type", "NATIVE");
        if (notifyUrl != null) {
            map.put("notify_url", notifyUrl);
        }
        try {
            return getWxPay(mchId).unifiedOrder(map);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * 微信下载对账单
     *
     * @param mchId
     * @param billDate 日期，格式：yyyMMdd
     * @param billType 类型，可选值：ALL/SUCCESS/REFUND/RECHARGE_REFUND
     * @return
     */
    public static Map<String, String> wxDownloadBill(String mchId, String billDate, String billType) {
        Map<String, String> map = new HashMap<>();
        map.put("bill_date", billDate);
        map.put("bill_type", billType);
        try {
            return getWxPay(mchId).downloadBill(map);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * 微信回调 回复失败
     *
     * @param reason
     * @return
     */
    public static String wxFail(String reason) {
        Map<String, String> map = new HashMap<>();
        map.put("return_code", "FAIL");
        map.put("return_msg", reason);
        try {
            return WXPayUtil.mapToXml(map);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 关闭微信订单
     *
     * @param mchId
     * @param outTradeNo 商户订单号
     * @return
     */
    public static Map<String, String> wxOrderCancel(String mchId, String outTradeNo) {
        Map<String, String> map = new HashMap<>();
        map.put("out_trade_no", outTradeNo);
        try {
            return getWxPay(mchId).closeOrder(map);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 查询微信订单
     *
     * @param mchId      微信商户号
     * @param tradeNo    微信订单号
     * @param outTradeNo 商户订单号
     * @return
     */
    public static Map<String, String> wxOrderQuery(String mchId, String tradeNo, String outTradeNo) {
        try {
            Map<String, String> map = new HashMap<>();
            if (tradeNo != null) {
                map.put("transaction_id", tradeNo);
            } else {
                map.put("out_trade_no", outTradeNo);
            }
            return getWxPay(mchId).orderQuery(map);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 微信退款
     *
     * @param mchId       商户号
     * @param tradeNo     微信订单号
     * @param outTradeNo  商户订单号
     * @param outRefundNo 退款单号
     * @param totalFee    订单总金额
     * @param refundFee   退款总金额
     * @return
     */
    public static Map<String, String> wxOrderRefund(String mchId, String tradeNo, String outTradeNo, String outRefundNo, long totalFee, long refundFee, String notifyUrl) {
        try {
            Map<String, String> map = new HashMap<>();
            if (tradeNo != null) {
                map.put("transaction_id", tradeNo);
            } else {
                map.put("out_trade_no", outTradeNo);
            }
            map.put("out_refund_no", outRefundNo);
            map.put("total_fee", String.valueOf(totalFee));
            map.put("refund_fee", String.valueOf(refundFee));
            if (notifyUrl != null) {
                map.put("notify_url", notifyUrl);
            }
            return getWxPay(mchId).refund(map);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 查询退款订单
     *
     * @param mchId
     * @param refundNo    微信退款单号
     * @param outRefundNo 商家退款单号
     * @param tradeNo     微信订单号
     * @param outTradeNo  商家订单号
     * @return
     */
    public static Map<String, String> wxOrderRefundQuery(String mchId, String refundNo, String outRefundNo, String tradeNo, String outTradeNo) {
        try {
            Map<String, String> map = new HashMap<>();
            if (refundNo != null) {
                map.put("refund_id", refundNo);
            } else if (outRefundNo != null) {
                map.put("out_refund_no", outRefundNo);
            } else if (tradeNo != null) {
                map.put("transaction_id", tradeNo);
            } else {
                map.put("out_trade_no", outTradeNo);
            }
            return getWxPay(mchId).refundQuery(map);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 微信短链接
     *
     * @param mchId
     * @param longUrl
     * @return
     */
    public static Map<String, String> wxShortUrl(String mchId, String longUrl) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("long_url", longUrl);
            return getWxPay(mchId).shortUrl(map);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 微信回调 回复成功
     *
     * @return
     */
    public static String wxSuccess() {
        Map<String, String> map = new HashMap<>();
        map.put("return_code", "SUCCESS");
        map.put("return_msg", "OK");
        try {
            return WXPayUtil.mapToXml(map);
        } catch (Exception e) {
            return null;
        }
    }

    private static AlipayClient getAli(String appId) {
        AlipayClient ali = aliMap.get(appId);
        if (ali == null) {
            throw new IllegalArgumentException("请先使用registerAli注册支付宝平台信息");
        }
        return ali;
    }

    private static WXPay getWxPay(String mchId) {
        WXPay wxPay = payMap.get(mchId);
        if (wxPay == null) {
            throw new IllegalArgumentException("请先使用registerWx注册微信平台信息");
        }
        return wxPay;
    }
}
