package wizrole.hosmerchants.adapter;

import android.content.Context;

import java.util.List;

import wizrole.hosmerchants.R;
import wizrole.hosmerchants.adapter.base.ConcreteAdapter;
import wizrole.hosmerchants.adapter.base.ViewHolder;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.my.model.mystoreinfor.StoreList;

/**
 * Created by Administrator on 2018/1/22.
 */

public class MerchantsListAdapter extends ConcreteAdapter<StoreList> {

    public MerchantsListAdapter(Context context, List<StoreList> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, StoreList item, int position) {
        viewHolder.setImageView(Constant.ip+item.getStoreLogoPic(), R.id.img_merchants_logo, R.drawable.img_loadfail)
                .setText(item.getStoreName(), R.id.text_merchants_name)
                .setText(item.getStorePlace(), R.id.text_merchants_address)
                .setText(item.getStorePhone(), R.id.text_merchants_tel)
                .setText("满50减5，满100减15", R.id.text_merchants_sub)
                .setText("20元起送", R.id.text_merchants_qs)
                .setText("配送费￥5", R.id.text_merchants_psf);
    }
}
