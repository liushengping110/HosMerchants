package wizrole.hosmerchants.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import wizrole.hosmerchants.R;

/**
 * Created by liushengping on 2018/1/16/016.
 * 何人执笔？
 */

public class ActionBar extends Toolbar {
    public ActionBar(Context context) {
        super(context);
    }

    public ActionBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ActionBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.mystoreview_actionbar, null);
        LayoutParams layoutParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        addView(view, layoutParams);
    }
}
