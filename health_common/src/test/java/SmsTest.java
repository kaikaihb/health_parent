import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.likai.utils.SMSUtils;
import org.json.JSONException;
import org.junit.Test;

import javax.xml.ws.http.HTTPException;
import java.io.IOException;

public class SmsTest {

    // 短信应用SDK AppID
    private int appid = 1400221721; // 1400开头
    // 短信应用SDK AppKey
    private String appkey = "70abe7f623472d30bc7a14ede094a6a0";
    // 需要发送短信的手机号码
    // private String phoneNumber = "15671641380";
    // 短信模板ID，需要在短信应用中申请
    private int templateId = 353407;// NOTE: 这里的模板ID`7839`只是一个示例，真实的模板ID需要在短信控制台中申请
    // 签名
    private String smsSign = "清风不识字的学习道路";// NOTE: 这里的签名"腾讯云"只是一个示例，真实的签名需要在短信控制台中申请，另外签名参数使用的是`签名内容`，而不是`签名ID`
    private String[] params = {"5678","5"};
    private String[] phoneNumbers = {"19865763080"};// "15671641380","19865763080"

    @Test
    public void fun01() throws Exception {
        try {
            SmsMultiSender msender = new SmsMultiSender(appid, appkey);
            SmsMultiSenderResult result = msender.sendWithParam("86", phoneNumbers,
                    templateId, params, smsSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
            System.out.println(result);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fun02() throws Exception {
        try {
            String phoneNumber = "19865763080";
            String[] params1 = {"5678"};
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
            SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumber,
                    templateId, params1, smsSign, "", "");
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sendShortMessageTest() throws Exception {
        SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,"19865763080","5678");
        SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,"19865763080","2019-06-21");
    }
}
