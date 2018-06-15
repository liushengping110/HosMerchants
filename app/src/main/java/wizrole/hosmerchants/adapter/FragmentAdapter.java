package wizrole.hosmerchants.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liushengping on 2017/11/27/027.
 * 何人执笔？
 * 主页fragment+viewPager适配器
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    //fragment 集合
    private List<Fragment> mFragmentList = new ArrayList<>();

    public FragmentAdapter(FragmentManager fm , List<Fragment> list) {
        super(fm);
        this.mFragmentList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

}
