package wizrole.hosmerchants.adapter;

import android.content.Context;

import java.util.List;

import wizrole.hosmerchants.R;
import wizrole.hosmerchants.adapter.base.ConcreteAdapter;
import wizrole.hosmerchants.adapter.base.ViewHolder;
import wizrole.hosmerchants.merchants.model.getsecondtypename.TwoTypeList;
import wizrole.hosmerchants.my.model.storecate.StoreTypeList;

/**
 * Created by liushengping on 2017/12/21/021.
 * 何人执笔？
 * 商铺二级分类类别列表适配器
 */

public class StoreSecondTypeAdapter extends ConcreteAdapter<TwoTypeList> {

    public StoreSecondTypeAdapter(Context context, List<TwoTypeList> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, TwoTypeList item, int position) {
        viewHolder.setText(item.getTwoTypeName(),R.id.store_name_item)
                .setImageView(R.drawable.item_sel,R.id.store_sel_item,R.drawable.item_sel);
            if(item.isSelect()){
                viewHolder.setVil(R.id.store_sel_item);
            }else{
                viewHolder.setVilGone(R.id.store_sel_item);
            }
    }
}
