package wizrole.hosmerchants.adapter;

import android.content.Context;

import java.util.List;

import wizrole.hosmerchants.R;
import wizrole.hosmerchants.adapter.base.ConcreteAdapter;
import wizrole.hosmerchants.adapter.base.ViewHolder;
import wizrole.hosmerchants.my.model.storecate.StoreTypeList;
import wizrole.hosmerchants.release.model.getgoodtype.StoreCommodityType;

/**
 * Created by liushengping on 2017/12/21/021.
 * 何人执笔？
 * 商品类别列表适配器
 */

public class StoreTypeAdapter extends ConcreteAdapter<StoreTypeList> {

    public StoreTypeAdapter(Context context, List<StoreTypeList> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, StoreTypeList item, int position) {
        viewHolder.setText(item.getStoreTypeName(),R.id.store_name_item);
    }
}
