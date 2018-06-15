package wizrole.hosmerchants.adapter;

import android.content.Context;

import java.util.List;

import wizrole.hosmerchants.R;
import wizrole.hosmerchants.adapter.base.ConcreteAdapter;
import wizrole.hosmerchants.adapter.base.ViewHolder;
import wizrole.hosmerchants.my.model.mystoreinfor.StoreList;

/**
 * Created by liushengping on 2017/12/18/018.
 * 何人执笔？
 */

public class MyStoreListAdapter extends ConcreteAdapter<StoreList> {

    public MyStoreListAdapter(Context context, List<StoreList> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, StoreList item, int position) {
        viewHolder.setText(item.getStoreName(), R.id.store_name_item);
    }
}
