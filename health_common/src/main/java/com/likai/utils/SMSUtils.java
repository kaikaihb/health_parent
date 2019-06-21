package com.likai.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import org.json.JSONException;
import org.junit.Test;

import javax.xml.ws.http.HTTPException;
import java.io.IOException;

/**
 * 短信发送工具类
 */
public class SMSUtils {
    public static final int VALIDATE_CODE = 353407;//发送短信验证码
    public static final int ORDER_NOTICE = 357901;//体检预约成功通知


    public static void sendShortMessage(int templateId, String phoneNumber, String param) {
        // 短信应用SDK AppID
        int appid = 1400221721;
        String appkey = "70abe7f623472d30bc7a14ede094a6a0";
        // 签名
        String smsSign = "清风不识字的学习道路";
        String[] params = null;

        switch (templateId) {
            case VALIDATE_CODE:
                params = new String[]{param, "5"};
                break;
            case ORDER_NOTICE:
                params = new String[]{param};
                break;
            default:
                break;
        }

        try {
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
            SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumber,
                    templateId, params, smsSign, "", "");
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        sendShortMessage(VALIDATE_CODE, "19865763080", "5578");
    }

}
