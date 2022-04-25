package com.hz;

import com.hz.utils.VariableName;
import com.qiniu.http.Response;
import com.qiniu.util.Auth;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.junit.Test;

import java.io.*;

/**
 * @author LingLong
 * @date 2022/3/28
 * @apiNote
 */
public class Down {
        //获取凭证
        String accessKey = VariableName.accessKey;
        String secretKey = VariableName.secretKey;
        //拿到凭证
        Auth auth = Auth.create(accessKey, secretKey);
        //获得外链域名
        String domain = VariableName.domain;

        //读取字节流输入内容
        private static byte[] readInputStream(InputStream is){

            ByteArrayOutputStream writer = new ByteArrayOutputStream();
            byte[] buff = new byte[1024 * 2];
            int len = 0;
            try {
                while((len = is.read(buff)) != -1){
                    writer.write(buff,0,len);
                }
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return writer.toByteArray();
        }

        //通过发送http  获取文件资源内容
        private static void download (String url,String filePath,String fileName){
            OkHttpClient client = new OkHttpClient();
            System.out.println(url);

            Request req = new Request.Builder().url(url).build();
            okhttp3.Response resp = null;
            //okhttp3.Response resp = null;

            try {
                resp = client.newCall(req).execute();
                System.out.println(resp.isSuccessful());
                if(resp.isSuccessful()){
                    ResponseBody body = resp.body();
                    InputStream is = body.byteStream();
                    byte[] data = readInputStream(is);
                    //判断文件夹是否存在，不存在则创建
                    File file = new File(filePath);
                    if(!file.exists() && !file.isDirectory()){
                        System.out.println("文件夹不存在=========创建");
                        file.mkdir();
                    }
                    File imgFile = new File(filePath + fileName+".jpg");
                    FileOutputStream fops = new FileOutputStream(imgFile);
                    fops.write(data);
                    fops.close();
                }
            }catch (IOException e){
                e.printStackTrace();
                System.out.println("Unexpected code"+resp);
            }

        }

        //文件下载
        public void download(String targetUrl,String fileName){
            //获取下载路径
            String downloadUrl = auth.privateDownloadUrl(targetUrl);
            //本地保存路径
            String filePath = "E:\\log\\";
            download(downloadUrl,filePath,fileName);
        }

        //测试
        @Test
        public void QiNiuDownLoad(){
            //文件名
            String fileName = "1111";
            //文件地址
            String targetUrl = domain+fileName;

            download(targetUrl,fileName);
        }

}
