package wizrole.hosmerchants.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by liushengping on 2017/12/28/028.
 * 何人执笔？
 */

public class MyApplication extends Application{

    private static Context context;

        @Override
    public void onCreate() {
        //获取Context
        context = getApplicationContext();
        super.onCreate();
    }

    //返回
    public static Context getContextObject(){
        return context;
    }

}
