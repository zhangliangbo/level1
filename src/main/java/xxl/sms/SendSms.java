package xxl.sms;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import xxl.mathematica.io.ExportString;

import java.util.Map;

/**
 * 发送验证码
 */
public class SendSms {
    /**
     * 阿里短信验证码
     *
     * @param phone
     * @param content
     * @return
     */
    public static boolean aliSms(String phone, String sign, String template, Object content, String regionId, String accessKey, String accessSecret, String outId) {
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKey, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", regionId);
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", sign);
        request.putQueryParameter("TemplateCode", template);
        request.putQueryParameter("TemplateParam", ExportString.exportStringJson(content));
        if (outId != null) {
            request.putQueryParameter("OutId", outId);
        }
        try {
            CommonResponse response = client.getCommonResponse(request);
            Map<String, String> map = ExportString.exportStringMap(response.getData());
            return map.get("Code") != null && "OK".equals(map.get("Code"));
        } catch (ClientException e) {
            return false;
        }
    }

    /**
     * 没有outId
     *
     * @param phone
     * @param sign
     * @param template
     * @param content
     * @param regionId
     * @param accessKey
     * @param accessSecret
     * @return
     */
    public static boolean aliSms(String phone, String sign, String template, Object content, String regionId, String accessKey, String accessSecret) {
        return aliSms(phone, sign, template, content, regionId, accessKey, accessSecret, null);
    }
}
