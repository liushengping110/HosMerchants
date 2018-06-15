package wizrole.hosmerchants.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import wizrole.hosmerchants.merchants.model.getsecondtypename.TwoTypeList;

/**
 * Created by liushengping on 2018/1/27.
 * 何人执笔？
 */

public class SingleStoreFragAdapter extends FragmentPagerAdapter {

    public List<Fragment> fragments;
    public FragmentManager fm;
    public List<TwoTypeList> strings;
    public SingleStoreFragAdapter( List<TwoTypeList> strings,List<Fragment> fragments,FragmentManager fm) {
        super(fm);
        this.fragments=fragments;
        this.fm=fm;
        this.strings=strings;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=fragments.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("two_type_id",strings.get(position).getTwoTypeId());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return strings.get(position).getTwoTypeName();
//        switch (position) {
//            case 0:
//                return "tab—1";
//            case 1:
//                return "tab—2";
//            case 2:
//                return "tab—3";
//            case 3:
//                return "tab—4";
//            case 4:
//                return "tab—5";
//            case 5:
//                return "tab—6";
//            default:
//                return "tab—x";
//        }
    }
}
