package com.likai.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.StorageClass;
import com.qcloud.cos.region.Region;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class QCloudUtils {
    private static String secretId = "AKIDvd5KVbvOq34pKFwN3lDPL7qSqqlZbSYP";
    private static String secretKey = "lGUVSAdrpb7e4HF2cw0XqvnOwIOK7gz2";
    private static String bucketName = "health-1256102088";
    private static COSClient cosClient = getCOSClient();

    static {
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region("ap-guangzhou");
        ClientConfig clientConfig = new ClientConfig(region);
        // 3 生成 cos 客户端。
        cosClient = new COSClient(cred, clientConfig);
    }

    public static void deleteObject(String fileKey) {
        try {
            // 指定对象在 COS 上的对象键 fileKey
            cosClient.deleteObject(bucketName, fileKey);
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }
    }

    public static void upload(String key,byte[] bytes) {
        InputStream input = new ByteArrayInputStream(bytes);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 从输入流上传必须制定content length, 否则http客户端可能会缓存所有数据，存在内存OOM的情况
        objectMetadata.setContentLength(bytes.length);//指定了字节数组的长度
        // 默认下载时根据cos路径key的后缀返回响应的contenttype, 上传时设置contenttype会覆盖默认值
        // objectMetadata.setContentType("image/jpeg");

        PutObjectRequest putObjectRequest =
                new PutObjectRequest(bucketName, key, input, objectMetadata);
        // 设置存储类型, 默认是标准(Standard), 低频(standard_ia)
        putObjectRequest.setStorageClass(StorageClass.Standard_IA);
        try {
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            // putobjectResult会返回文件的etag
            String etag = putObjectResult.getETag();
        } catch (CosServiceException e) {
            e.printStackTrace();
        } catch (CosClientException e) {
            e.printStackTrace();
        }
    }


    public static COSClient getCOSClient() {
        return cosClient;
    }

    public static void shutdown() {
        cosClient.shutdown();
    }

}
