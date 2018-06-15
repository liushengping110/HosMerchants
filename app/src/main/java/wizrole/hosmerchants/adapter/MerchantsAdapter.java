package wizrole.hosmerchants.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import wizrole.hosmerchants.R;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.my.model.mystoreinfor.StoreList;
import wizrole.hosmerchants.util.image.ImageLoading;

/**
 * Created by liushengping on 2017/12/7/007.
 * 何人执笔？
 * 商家列表适配器
 */

public class MerchantsAdapter extends RecyclerView.Adapter<MerchantsAdapter.MerchantsViewHolder>{

    private List<StoreList> storeLists;
    private Context context;
    public MerchantsAdapter(Context context, List<StoreList> storeLists) {
        this.context = context;
        this.storeLists = storeLists;
    }

    @Override
    public MerchantsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MerchantsViewHolder(LayoutInflater.from(context).inflate(R.layout.recy_merchants_item,null),viewType);
    }

    @Override
    public void onBindViewHolder(MerchantsViewHolder holder, final int position) {
        ImageLoading.common(context, Constant.ip+storeLists.get(position).getStoreLogoPic(),holder.imageView_logo,R.drawable.img_loadfail);
        holder.textView_name.setText(storeLists.get(position).getStoreName());
        holder.textView_address.setText(storeLists.get(position).getStorePlace());
        holder.textView_tel.setText(storeLists.get(position).getStorePhone());
        holder.textView_sub.setText("满50减5，满100减15");
        holder.textView_qs.setText("20元起送");
        holder.textView_psf.setText("配送费￥5");
        ((MerchantsViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return storeLists.size();
    }


    public class MerchantsViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView_logo;
        TextView textView_name;
        TextView textView_address;
        TextView textView_tel;
        TextView textView_qs;
        TextView textView_sub;
        TextView textView_psf;
        int viewType;
        public MerchantsViewHolder(View itemView,final int viewType){
            super(itemView);
            this.viewType=viewType;
            imageView_logo = (ImageView) itemView.findViewById(R.id.img_merchants_logo);
            textView_name = (TextView) itemView.findViewById(R.id.text_merchants_name);
            textView_address = (TextView) itemView.findViewById(R.id.text_merchants_address);
            textView_tel = (TextView) itemView.findViewById(R.id.text_merchants_tel);
            textView_sub = (TextView) itemView.findViewById(R.id.text_merchants_sub);
            textView_qs = (TextView) itemView.findViewById(R.id.text_merchants_qs);
            textView_psf = (TextView) itemView.findViewById(R.id.text_merchants_psf);
        }
    }


    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , int viewType);
    }

    public OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
