package wizrole.hosmerchants.util.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by liushengping on 2017/11/22/022.
 * 何人执笔？
 * 网络请求工具类
 */

public class OkPotting {

    private Request request;
    private OkHttpClient client;
    private Call call;
    private static String base_url;
    private static OkPotting okPotting;
    public  HttpStatusCallBack infoFruit;
    private MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkPotting(String base_url) {
        this.base_url = base_url;
        client = new OkHttpClient();
        client.newBuilder()
                .writeTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
    }

    public void OnCancel() {
        call.cancel();
    }

    public static synchronized OkPotting getInstance(String base_url) {
        if (okPotting == null) {
            okPotting = new OkPotting(base_url);
        }
        OkPotting.base_url = base_url;
        return okPotting;
    }

    private Request get(String url) {
        request = new Request.Builder()
                .url( base_url+url)
                .get()
                .build();
        return request;
    }

    private Request post(String url, FormBody formBody) {
        request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        return request;
    }

    private Request json(String base_url, String json) {
        RequestBody formBody = new FormBody.Builder()
                .add("param", json)
                .build();
        request = new Request.Builder()
                .url(base_url)
                .post(formBody)
                .build();
        return request;
    }


    /**
     * get请求
     * @param 、url地址
     * @param cla 解析对象
     * @param infoFruit 接口返回
     */
    public void Ask(String url, Class cla, HttpStatusCallBack infoFruit) {
        call = client.newCall(get(url));
        Asynchronous(cla, infoFruit);
    }

    /**
     * post请求
     * @param url
     * @param body
     * @param cla
     * @param infoFruit
     */
    public void Ask(String url, FormBody body, Class cla, HttpStatusCallBack infoFruit) {
        call = client.newCall(post(url, body));
        Asynchronous(cla, infoFruit);
    }

    /**
     * json格式请求
     * @param url
     * @param jsonObject
     * @param cla
     * @param infoFruit
     */
    public void Ask(String url, JSONObject jsonObject, Class cla, HttpStatusCallBack infoFruit) {
        call = client.newCall(json(url, jsonObject.toString()));
        Asynchronous(cla, infoFruit);
    }

    private void Asynchronous(final Class cla, final HttpStatusCallBack infoFruit) {
        this.infoFruit = infoFruit;
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                infoFruit.onError("网络连接失败，请检查网络");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                Object o = gson.fromJson(response.body().string(), cla);
                if (o != null) {
                    handler.obtainMessage(4001,o).sendToTarget();
                } else {
                    handler.obtainMessage(4002,"数据解析异常").sendToTarget();
                }
            }
        });
    }

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 4001:
                    infoFruit.onSuccess(msg.obj);
                    break;
                case 4002:
                    infoFruit.onError("网络连接失败，请检查网络");
                    break;
            }
        }
    };
}