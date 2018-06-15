package wizrole.hosmerchants.ui.pop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.util.DensityUtil;

import wizrole.hosmerchants.R;


/**
 * Created by Administrator on 2017/3/21.
 */

public abstract class PopupWindowPotting {

    private View view;
    private Activity activity;
    public Context context;
    private PopupWindow popupWindow;
    public int type;
    public PopDissListener pop_dissListener;


    public PopupWindowPotting(Activity activity,int type) {
        this.activity = activity;
        this.type=type;
        if(type==1){
            pop_dissListener=(PopDissListener)activity;
        }
        onCreate(0, 0);
    }
    public Fragment fragment;
    public PopupWindowPotting(Fragment fragment, int type) {
        this.fragment = fragment;
        this.type=type;
        if(type==2){
            pop_dissListener=(PopDissListener)fragment;
        }
        onCreate(0, 0);
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    protected void onCreate(int width, int height) {
        if(type==1){//Activity---搜索pop
            if (view == null) view = LayoutInflater.from(activity).inflate(getLayout(), null);
            WindowManager manager=activity.getWindowManager();
            int wid_final=manager.getDefaultDisplay().getWidth();
            if (height == 0) height = WindowManager.LayoutParams.MATCH_PARENT;
            popupWindow = new PopupWindow(view, wid_final, height);
        }else if(type==2){//Fragment-首页商家、好评筛选
            if (view == null) view = LayoutInflater.from(fragment.getActivity()).inflate(getLayout(), null);
            WindowManager manager=fragment.getActivity().getWindowManager();
            int wid_final=manager.getDefaultDisplay().getWidth();
            if (height == 0) height = WindowManager.LayoutParams.MATCH_PARENT;
            popupWindow= new PopupWindow(view, wid_final, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        }
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if(type==1){
                    pop_dissListener.onDiss();
                }else if(type==2){
                    pop_dissListener.onDiss();
                }
                Hide();
            }
        });
        initUI();
        setListener();
    }

    public void Show(View view) {
        if(type==1){//搜索下方pop
            popupWindow.showAsDropDown(view, 0, 40);
        }else if(type==2){
            popupWindow.showAsDropDown(view, 0, 0);
        }
    }

    public void Hide() {
        if(type==1){
            WindowManager.LayoutParams lp = (activity).getWindow().getAttributes();
            lp.alpha = 1f;
            (activity).getWindow().setAttributes(lp);
            popupWindow.dismiss();
        }else if(type==2){
            popupWindow.dismiss();
        }

    }

    protected abstract int getLayout();

    protected abstract void initUI();

    protected abstract void setListener();

    private SparseArray<View> sparseArray = new SparseArray<>();

    protected <T extends View> T $(int rid) {
        if (sparseArray.get(rid) == null) {
            View viewgrounp = view.findViewById(rid);
            sparseArray.append(rid, viewgrounp);
            return (T) viewgrounp;
        } else {
            return (T) sparseArray.get(rid);
        }
    }

    private Intent intent;
    private Toast toast;

    protected void Skip(Class cla) {
        intent = new Intent(activity, cla);
        activity.startActivity(intent);
    }

    protected void ToastShow(String result) {
        if (toast == null) {
            toast = Toast.makeText(activity, result, Toast.LENGTH_SHORT);
        } else {
            toast.setText(result);
        }
        toast.show();
    }
}