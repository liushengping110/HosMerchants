package wizrole.hosmerchants.util.fileup;

import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import wizrole.hosmerchants.util.MD5Util;

/**
 * Created by liushengping on 2018/1/5/005.
 * 何人执笔？
 */

public class ImageUp {

    /**
     * okhttp表单提交图片上传
     * @param path文件地址
     */
    private void uploadMultiFile(String path) {
        String imageType = "multipart/form-data";
        File file = new File(path);
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("fileData", "head_image", fileBody)
                .addFormDataPart("imagetype", imageType)
                .build();
        Request request = new Request.Builder()
                .url("http://wd.s1.natapp.cc/Customer/cuploadpic/uploadpic?")
                .addHeader("sec", MD5Util.encrypt("431104c0-984c-4415-90ed-847938e5cb00"+"http://wd.s1.natapp.cc/Customer/cuploadpic/uploadpic"))
                .post(requestBody)
                .build();
        final okhttp3.OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = httpBuilder
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String msg=e.toString();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String htmlStr = response.body().string();
                Log.i("result", htmlStr);
            }
        });
    }
}
