package com.hz.servise;


import com.google.gson.Gson;
import com.hz.utils.VariableName;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

/**
 * @author LingLong
 * @date 2022/3/28
 * @apiNote
 */
public class Upload {

    //本地图片简单上传，url参数传本地图片地址,imgName为图片名
    public static String upLoad(String url,String imgName){
        //创建储存空间对应的储存配置
        //导这个包  com.qiniu.storage.Configuration;
        Configuration cfg = new Configuration(Zone.huanan());
        UploadManager uploadManager = new UploadManager(cfg);

        //获取上传凭证 准备上传
        String accessKey = VariableName.accessKey;
        String secretKey = VariableName.secretKey;
        String bucket = VariableName.bucket;

        //上传地址：Windows情况下 格式是D：\\
        String localFilePath = url;
        //默认不指定文件名的情况下，以文件的hash值作为文件名
        String key = imgName;
        //拿到凭证
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        try {
            //开始上传
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            //打印返回结果
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException e) {

            Response r = e.response;
            System.out.println(r.toString());

            try {
                System.err.println(r.bodyString());
            } catch (QiniuException e1) {
                e1.printStackTrace();
            }
        }

        return null;

    }

}
