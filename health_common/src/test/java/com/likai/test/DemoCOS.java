package com.likai.test;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class DemoCOS {

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

    //创建bucket
    @Test
    public void fun01() {
        //String bucket = "health-1256102088"; //存储桶名称，格式：BucketName-APPID
        CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucket);
        // 设置 bucket 的权限为 PublicRead(公有读私有写), 其他可选有私有读写, 公有读写
        createBucketRequest.setCannedAcl(CannedAccessControlList.PublicRead);
        try {
            Bucket bucketResult = cosClient.createBucket(createBucketRequest);
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }
    }

    //删除bucket
    @Test
    public void deleteBucket() {
        cosClient.deleteBucket(bucket);
    }



    //查询存储桶列表
    @Test
    public void fun02() {
        try {
            List<Bucket> buckets = cosClient.listBuckets();
            System.out.println(buckets);
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }
    }

    //上传文件
    @Test
    public void fun03() {
        // "E:\\resources\\heaven.jpg"
        try {
            // 指定要上传的文件
            File localFile = new File("E:\\resources\\heaven.jpg");
            // 指定要上传到的存储桶
            String bucketName = bucket;
            // 指定要上传到 COS 上对象键
            // String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            // String key = uuid + "_" + localFile.getName();//这样可以避免重名
            String key = localFile.getName();
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }
    }

    //查询存储桶中对象列表
    @Test
    public void fun04() {
        try {
            // String bucket = "examplebucket-1250000000";
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
            // 设置 bucket 名称
            listObjectsRequest.setBucketName(bucket);
            // prefix 表示列出的 object 的 key 以 prefix 开始
            listObjectsRequest.setPrefix("");
            // 设置最大遍历出多少个对象, 一次 listobject 最大支持1000
            listObjectsRequest.setMaxKeys(1000);
            listObjectsRequest.setDelimiter("/");
            ObjectListing objectListing = cosClient.listObjects(listObjectsRequest);
            for (COSObjectSummary cosObjectSummary : objectListing.getObjectSummaries()) {
                // 对象的路径 key
                String key = cosObjectSummary.getKey();
                // 对象的 etag
                String etag = cosObjectSummary.getETag();
                // 对象的长度
                long fileSize = cosObjectSummary.getSize();
                // 对象的存储类型
                String storageClass = cosObjectSummary.getStorageClass();
                System.out.println("key:" + key + "; etag:" + etag + "; fileSize:" + fileSize + "; storageClass:" + storageClass);
            }
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }
    }

    //下载对象
    @Test
    public void fun05() {
        try {
            // 指定对象所在的存储桶
            String bucketName = bucket;
            // 指定对象在 COS 上的对象键
            String key = "heaven.jpg";
            // 指定要下载到的本地路径
            String downDir = "E:\\resources_download\\";
            File downFile = new File(downDir + key);
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
            ObjectMetadata downObjectMeta = cosClient.getObject(getObjectRequest, downFile);
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }
    }

    //删除对象
    @Test
    public void fun06(){
        try {
            // 指定对象所在的存储桶
            String bucketName = bucket;
            // 指定对象在 COS 上的对象键
            String key = "heaven.jpg";
            cosClient.deleteObject(bucketName, key);
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }
    }

}
