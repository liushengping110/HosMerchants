package wizrole.hosmerchants.my.model.checkemail;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import rx.Subscriber;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.util.http.RxJavaOkPotting;

/**
 * Created by liushengping on 2017/12/18/018.
 * 何人执笔？
 */

public class CheckEMailHttp {

    public CheckEMailBackInterface checkEMailBackInterface;

    public CheckEMailHttp(CheckEMailBackInterface checkEMailBackInterface){
        this.checkEMailBackInterface=checkEMailBackInterface;
    }

//    public void checkEMailHttp(String email){
//        JSONObject object=new JSONObject();
//        try {
//            object.put("TradeCode","Y002");
//            object.put("Email",email);
//            RxJavaOkPotting.getInstance(Constant.base_Url).Ask(Constant.base_Url, object.toString(), new Subscriber() {
//                @Override
//                public void onCompleted() {
//
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    checkEMailBackInterface.Fail(e.toString());
//                }
//
//                @Override
//                public void onNext(Object o) {
//                    if(o.equals(RxJavaOkPotting.NET_ERR)){
//                        checkEMailBackInterface.Fail("");
//                    }else{
//                        Gson gson=new Gson();
//                        CheckEMailBack back=new CheckEMailBack();
//                        back=gson.fromJson(o.toString(),CheckEMailBack.class);
//                        if(back.getResultCode().equals("0")){
//                            checkEMailBackInterface.Succ(back);
//                        }else{
//                            checkEMailBackInterface.Fail("");
//                        }
//                    }
//                }
//            });
//        }catch (JSONException e){
//            checkEMailBackInterface.Fail(e.toString());
//        }
//    }


    /**
     * 获取检验报告
     */
    public void checkEMailHttp(String email){
        JSONObject object=new JSONObject();
        try {
            object.put("TradeCode","Y002");
            object.put("Email",email);
            new MyThread(Constant.base_Url+"param="+object.toString()).start();
        }catch (JSONException e){
            checkEMailBackInterface.Fail("");
        }
    }

    class MyThread extends Thread{
        public String msg;
        public MyThread(String msg){
            this.msg=msg;
        }
        public void run() {
            get(msg);
        }
    };

    public void get(String msg){
        try{
            URL url = new URL(msg);
            //获取一个连接
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            //设置GET请求
            connection.setRequestMethod("GET");
            //设置超时时间
            connection.setConnectTimeout(40000);
            connection.setReadTimeout(50000);
            //获取服务器响应码
            int ncode = connection.getResponseCode();
            if( ncode == 200 ){
                InputStream inputStream = connection.getInputStream();
                //将流转换成String
                String content =ReadStream(inputStream);
                Gson gson=new Gson();
                CheckEMailBack back=new CheckEMailBack();
                back=gson.fromJson(content,CheckEMailBack.class);
                if(back.getResultCode().equals("0")){
                    checkEMailBackInterface.Succ(back);
                }else{
                    checkEMailBackInterface.Fail(back.getResultContent());
                }
            }else{
                checkEMailBackInterface.Fail("");
            }
        }catch(Exception e){
            e.printStackTrace();
            checkEMailBackInterface.Fail("");
        }
    }

    /**
     * 读取返回字符串
     * @param in
     * @return
     * @throws Exception
     */
    public  String ReadStream(InputStream in) throws Exception{
        int len = -1;
        byte buffer[] = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((len = in.read(buffer)) > 0 ){
            baos.write(buffer,0,len);
        }
        //关闭流
        in.close();
        String content = new String(baos.toByteArray());
        return content;
    }

}
