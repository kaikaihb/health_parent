package com.likai.test;

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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.UUID;

public class DemoStream {

    COSClient cosClient;
    String bucket = "health-1256102088";

    @Before
    public void init() {
        String secretId = "AKIDvd5KVbvOq34pKFwN3lDPL7qSqqlZbSYP";
        String secretKey = "lGUVSAdrpb7e4HF2cw0XqvnOwIOK7gz2";

        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region("ap-guangzhou");
        ClientConfig clientConfig = new ClientConfig(region);
        // 3 生成 cos 客户端。
        cosClient = new COSClient(cred, clientConfig);
    }

    @After
    public void destroy() {
        cosClient.shutdown();
    }

    @Test
    // 从输入流进行读取并上传到COS
    public void SimpleUploadFileFromStream() throws FileNotFoundException {

        // bucket名需包含appid
        String bucketName = bucket;

        // String key = "aaa/bbb.jpg";
        String key = "heaven.jpg";
        File localFile = new File("heaven.jpg");
        FileInputStream in = new FileInputStream(localFile);

        InputStream input = new ByteArrayInputStream(new byte[10]);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 从输入流上传必须制定content length, 否则http客户端可能会缓存所有数据，存在内存OOM的情况
        objectMetadata.setContentLength(10);
        // 默认下载时根据cos路径key的后缀返回响应的contenttype, 上传时设置contenttype会覆盖默认值
        objectMetadata.setContentType("image/jpeg");

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

        // 关闭客户端
        cosClient.shutdown();
    }

    @Test
    // 从输入流进行读取并上传到COS
    public void fun() throws IOException {
        MultipartFile uploadFile = null;
        String fileName = uploadFile.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        System.out.println(suffix);
        String key = new Date().getTime() + "." + suffix;

        // 判断文件大小（小文件上传建议不超过20M）
        byte[] bytes = uploadFile.getBytes();
        int length = bytes.length;

        // 获取文件流
        InputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 从输入流上传必须制定content length, 否则http客户端可能会缓存所有数据，存在内存OOM的情况
        objectMetadata.setContentLength(length);
        // 默认下载时根据cos路径key的后缀返回响应的contenttype, 上传时设置contenttype会覆盖默认值
        // objectMetadata.setContentType("image/jpeg");

        PutObjectRequest putObjectRequest = new PutObjectRequest("BUCKETNAME", key, byteArrayInputStream, objectMetadata);
        // 设置 Content type, 默认是 application/octet-stream
        putObjectRequest.setMetadata(objectMetadata);
        // 设置存储类型, 默认是标准(Standard), 低频(standard_ia)
        putObjectRequest.setStorageClass(StorageClass.Standard_IA);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);

        String eTag = putObjectResult.getETag();
        System.out.println(eTag);
        // 关闭客户端
        cosClient.shutdown();
        // http://{buckname}-{appid}.cosgz.myqcloud.com/image/1545012027692.jpg
        // String url = URL + key;
        // System.out.println(url);

        // 关闭客户端
        cosClient.shutdown();
    }

    @Test
    public void fun01(){
        String fileName = "heaven.jpg";
        int lastIndexOf = fileName.lastIndexOf(".");
        System.out.println(lastIndexOf);
        String fileName2 = new Date().getTime() + fileName.substring(lastIndexOf);
        System.out.println(fileName2);
    }
}
