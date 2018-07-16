package com.thxy.skytalk_client.factory.helper;

import android.text.format.DateFormat;
import android.util.Log;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.thxy.common.baseMVP.IModel;
import com.thxy.common.utils.HashUtil;
import com.thxy.skytalk_client.factory.Factory;

import java.io.File;
import java.util.Date;

/**
 * Created by Administrator on 2017/10/5.
 */

public class OssUploadHelper {
    private static final String TAG = OssUploadHelper.class.getSimpleName();
    // 与存储区域有关系
    private static final String ENDPOINT = "http://oss-cn-shenzhen.aliyuncs.com";
    // 上传的仓库名
    private static final String BUCKET_NAME = "skytalk-client";

    private static OSS getClient() {
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(
                "LTAIIeiLoNdfeMwt", "d7EZjuBLbT07oINGl4fiSGxNwLQj8r");
        return new OSSClient(Factory.getApplication(), ENDPOINT, credentialProvider);
    }

    /**
     * 上传的最终方法，成功返回则一个路径
     *
     * @param objKey 上传上去后，在服务器上的独立的KEY
     * @param path   需要上传的文件的路径
     * @return 存储的地址
     */
    private static void upload(String objKey, String path, IModel.ResultListener<String> listener) {
        // 构造一个上传请求
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME,
                objKey, path);
        try {
            // 初始化上传的Client
            OSS client = getClient();
            // 开始同步上传
            PutObjectResult result = client.putObject(request);
            // 得到一个外网可访问的地址
            String url = client.presignPublicObjectURL(BUCKET_NAME, objKey);
            // 格式打印输出
            Log.e(TAG, String.format("PublicObjectURL:%s", url));

            listener.onDataSuccess(url);
        } catch (Exception e) {
            e.printStackTrace();
            // 如果有异常则返回空
        }
    }

    /**
     * 上传普通图片
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    public static void uploadImage(String path, IModel.ResultListener<String> listener) {
        String key = getImageObjKey(path);
        upload(key, path,listener);
    }

    /**
     * 上传头像
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    public static void uploadPortrait(String path, IModel.ResultListener<String> listener) {
        String key = getPortraitObjKey(path);
        upload(key, path,listener);
    }

    /**
     * 上传音频
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    public static void uploadAudio(String path, IModel.ResultListener<String> listener) {
        String key = getAudioObjKey(path);
        upload(key, path,listener);
    }

    /**
     * 分月存储，避免一个文件夹太多
     * @return yyyyMM
     */
    private static String getDateString() {
        return DateFormat.format("yyyyMM", new Date()).toString();
    }

    // image/201703/dawewqfas243rfawr234.jpg
    private static String getImageObjKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("image/%s/%s.jpg", dateString, fileMd5);
    }

    // portrait/201703/dawewqfas243rfawr234.jpg
    private static String getPortraitObjKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("portrait/%s/%s.jpg", dateString, fileMd5);
    }

    // audio/201703/dawewqfas243rfawr234.mp3
    private static String getAudioObjKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("audio/%s/%s.mp3", dateString, fileMd5);
    }
}

