package com.likai.test;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.cr.v20180321.CrClient;
import com.tencentcloudapi.cr.v20180321.models.UploadDataFileRequest;
import com.tencentcloudapi.cr.v20180321.models.UploadDataFileResponse;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

public class TestDemo {

    @Test
    public void fun01() {
        try {
            String secretId = "AKIDvd5KVbvOq34pKFwN3lDPL7qSqqlZbSYP";
            String secretKey = "lGUVSAdrpb7e4HF2cw0XqvnOwIOK7gz2";
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey
            Credential cred = new Credential(secretId, secretKey);

            HttpProfile httpProfile = new HttpProfile();
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            CrClient crclient = new CrClient(cred, "ap-guangzhou", clientProfile);

            UploadDataFileRequest udfreq = new UploadDataFileRequest();
            udfreq.setModule("Data");
            udfreq.setOperation("Upload");
            udfreq.setFileName("李凯.jpg");
            udfreq.setFile(Files.readAllBytes(Paths.get("E:\\heaven.jpg")));
            UploadDataFileResponse udfresp = crclient.UploadDataFile(udfreq);

            System.out.println(UploadDataFileResponse.toJsonString(udfresp));
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
