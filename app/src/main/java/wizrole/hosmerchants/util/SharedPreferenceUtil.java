package wizrole.hosmerchants.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.List;


/**
 * Created by liushengping on 2017/11/27/027.
 * 何人执笔？
 */

public class SharedPreferenceUtil {
    public static final String  NAME ="lock";//手势密码标签
    /**保存用户姓名*/
    public static void saveUserTel(Context context, String name){
        SharedPreferences sp=context.getSharedPreferences("user_tel", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("user_t", name);
        editor.commit();
    }
    /**获取用户姓名*/
    public static String getUserTel(Context context){
        SharedPreferences sp=context.getSharedPreferences("user_tel", Context.MODE_PRIVATE);
        return sp.getString("user_t", "");
    }
    /**
     * 清除用户姓名
     * @param context
     */
    public static void clearUserTel(Context context){
        SharedPreferences sp = context.getSharedPreferences("user_tel", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }



    /**保存用户姓名*/
    public static void saveUserPassW(Context context, String name){
        SharedPreferences sp=context.getSharedPreferences("user_pass", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("user_p", name);
        editor.commit();
    }
    /**获取用户姓名*/
    public static String getUserPassW(Context context){
        SharedPreferences sp=context.getSharedPreferences("user_pass", Context.MODE_PRIVATE);
        return sp.getString("user_p", "");
    }
    /**
     * 清除用户姓名
     * @param context
     */
    public static void clearUserPassW(Context context){
        SharedPreferences sp = context.getSharedPreferences("user_pass", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }


    /**保存用户姓名*/
    public static void saveUserName(Context context, String name){
        SharedPreferences sp=context.getSharedPreferences("user_name", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("user_n", name);
        editor.commit();
    }
    /**获取用户姓名*/
    public static String getUserName(Context context){
        SharedPreferences sp=context.getSharedPreferences("user_name", Context.MODE_PRIVATE);
        return sp.getString("user_n", "");
    }
    /**
     * 清除用户姓名
     * @param context
     */
    public static void clearUserName(Context context){
        SharedPreferences sp = context.getSharedPreferences("user_name", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /**保存用户头像*/
    public static void saveHeaderImg(Context context, String name){
        SharedPreferences sp=context.getSharedPreferences("img", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("user_img", name);
        editor.commit();
    }
    /**获取用户头像*/
    public static String getHeaderImg(Context context){
        SharedPreferences sp=context.getSharedPreferences("img", Context.MODE_PRIVATE);
        return sp.getString("user_img", "");
    }
    /**
     * 清除用户头像
     * @param context
     */
    public static void clearHeaderImg(Context context){
        SharedPreferences sp = context.getSharedPreferences("img", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }


    /**保存用户主键id*/
    public static void saveID(Context context, String name){
        SharedPreferences sp=context.getSharedPreferences("id", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("user_id", name);
        editor.commit();
    }
    /**获取用户主键id*/
    public static String getID(Context context){
        SharedPreferences sp=context.getSharedPreferences("id", Context.MODE_PRIVATE);
        return sp.getString("user_id", "");
    }
    /**
     * 清除用户主键id
     * @param context
     */
    public static void clearID(Context context){
        SharedPreferences sp = context.getSharedPreferences("id", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 存储用户详细信息
     * 存放实体类以及任意类型
     * @param context 上下文对象
     * @param obj
     */
    public static void saveUserInfor(Context context,Object obj) {
        if (obj instanceof Serializable) {// obj必须实现Serializable接口，否则会出问题
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(obj);
                String string64 = new String(Base64.encode(baos.toByteArray(),
                        0));
                SharedPreferences sp=context.getSharedPreferences("person", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor =sp.edit();
                editor.putString("pf", string64).commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException(
                    "the obj must implement Serializble");
        }

    }

    /**
     * 获取用户详细信息
     * @param context
     * @return
     */
    public static Object getUserInfor(Context context) {
        Object obj = null;
        try {
            SharedPreferences sp=context.getSharedPreferences("person", Context.MODE_PRIVATE);
            String base64 = sp.getString("pf", "");
            if (base64.equals("")) {
                return null;
            }
            byte[] base64Bytes = Base64.decode(base64.getBytes(), 1);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            obj = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
    /**
     * 清除用户信息实体类
     * @param context
     */
    public static void clearUserInfor(Context context){
        SharedPreferences sp = context.getSharedPreferences("person", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /**保存用户登陆状态*/
    public static void saveLoginState(Context context,int status){
        SharedPreferences sp=context.getSharedPreferences("isLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putInt("success", status);
        editor.commit();
    }
    /**获取用户登陆状态*/
    public static int getLoginState(Context context){
        SharedPreferences sp=context.getSharedPreferences("isLogin", Context.MODE_PRIVATE);
        return sp.getInt("success", 2);
    }
    /**
     * 清除用户登录状态
     * @param context
     */
    public static void clearLoginState(Context context){
        SharedPreferences sp = context.getSharedPreferences("isLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 获取手势密码
     * @param context
     * @param key
     * @return
     */
    public static String[] getMama(Context context,String key) {
        String regularEx = "#";
        String[] str = null;
        SharedPreferences sp = context.getSharedPreferences("mima", Context.MODE_PRIVATE);
        String values;
        values = sp.getString(key, "");
        str = values.split(regularEx);
        return str;
    }

    /**
     * 设置手势密码
     * @param context
     * @param key
     * @param values
     */
    public static void setMima(Context context,String key, String[] values) {
        String regularEx = "#";
        String str = "";
        SharedPreferences sp = context.getSharedPreferences("mima", Context.MODE_PRIVATE);
        if (values != null && values.length > 0) {
            for (String value : values) {
                str += value;
                str += regularEx;
            }
            SharedPreferences.Editor et = sp.edit();
            et.putString(key, str);
            et.commit();
        }
    }
    /**
     * 清除用户手势密码
     * @param context
     */
    public static void clearMima(Context context){
        SharedPreferences sp = context.getSharedPreferences("mima", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }


    /**保存用户信息完善标记*/
    public static void saveInforComplete(Context context, String name){
        SharedPreferences sp=context.getSharedPreferences("infor_complete", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("infor_com", name);
        editor.commit();
    }
    /**获取用户信息完善标记*/
    public static String getInforComplete(Context context){
        SharedPreferences sp=context.getSharedPreferences("infor_complete", Context.MODE_PRIVATE);
        return sp.getString("infor_com", "");
    }
    /**
     * 清除用户信息完善标记
     * @param context
     */
    public static void clearInforComplete(Context context){
        SharedPreferences sp = context.getSharedPreferences("infor_complete", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

}
