package wizrole.hosmerchants.adapter;

import android.content.Context;

import java.util.List;

import wizrole.hosmerchants.R;
import wizrole.hosmerchants.adapter.base.ConcreteAdapter;
import wizrole.hosmerchants.adapter.base.ViewHolder;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.release.model.getgoodtype.CommodityList;

/**
 * Created by liushengping on 2018/1/25.
 * 何人执笔？
 */

public class GoodsSearchShowAdapter extends ConcreteAdapter<CommodityList> {

    public GoodsSearchShowAdapter(Context context, List<CommodityList> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    protected void convert(ViewHolder viewHolder, CommodityList item, int position) {
        viewHolder.setText(item.getCommodityName(),R.id.textview_detail)
                .setText(item.getCommodityAmt(), R.id.tvGoodsPrice)
                .setText(item.getCommodityContent(),R.id.tvGoodsDescription)
                .setText("88",R.id.text_monSaleNum)
                .setImageView(Constant.ip+item.getCommodityPic(),R.id.imageview_superlogo,R.drawable.img_loadfail);
    }
}
