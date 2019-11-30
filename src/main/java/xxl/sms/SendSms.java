package xxl.sms;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

/**
 * 发送验证码
 */
public class SendSms {
    /**
     * 阿里短信验证码
     *
     * @param phone
     * @param code
     * @return
     */
    public static boolean aliVerificationCode(String phone, String name, String template, String code, String regionId, String accessKey, String accessSecret, String outId) {
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKey, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", regionId);
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", name);
        request.putQueryParameter("TemplateCode", template);
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");
        if (outId != null) {
            request.putQueryParameter("OutId", outId);
        }
        try {
            CommonResponse response = client.getCommonResponse(request);
            return true;
        } catch (ClientException e) {
            return false;
        }
    }

    /**
     * 没有outId
     *
     * @param phone
     * @param name
     * @param template
     * @param code
     * @param regionId
     * @param accessKey
     * @param accessSecret
     * @return
     */
    public static boolean aliVerificationCode(String phone, String name, String template, String code, String regionId, String accessKey, String accessSecret) {
        return aliVerificationCode(phone, name, template, code, regionId, accessKey, accessSecret, null);
    }
}
