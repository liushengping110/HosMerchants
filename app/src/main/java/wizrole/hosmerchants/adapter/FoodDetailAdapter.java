package wizrole.hosmerchants.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;
import java.util.List;

import wizrole.hosmerchants.R;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.release.model.getgoodtype.CommodityList;
import wizrole.hosmerchants.release.model.getgoodtype.StoreCommodityType;
import wizrole.hosmerchants.util.image.ImageLoading;

/**
 * Created  on 2017/3/1.
 */
public class FoodDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {
    private List<StoreCommodityType> foodTypeList;
    private List<CommodityList> foodDetailList = new ArrayList<>();
    private Context mContext;
    public FoodDetailAdapter(Context context, List<StoreCommodityType> foodList) {
        mContext = context;
        setFoodList(foodList);
    }

    public void setFoodList(List<StoreCommodityType> foodTypeList) {
        this.foodTypeList = foodTypeList;
        for(int i = 0; i< foodTypeList.size(); i++){
            if(foodTypeList !=null){
                foodDetailList.addAll(foodTypeList.get(i).getCommodityList());
            }
        }
        notifyDataSetChanged();
    }

    public List<StoreCommodityType> getFoodTypeList() {
        return foodTypeList;
    }

    @Override
    public int getItemCount() {
        return foodDetailList.size();
    }

    /**
     * 返回值相同会被默认为同一项
     * @param position
     * @return
     */
    @Override
    public long getHeaderId(int position) {
        return getSortType(position);
    }

    //获取当前饭菜的类型
    public int getSortType(int position) {
        int sort = -1;
        int sum = 0;
        for (int i = 0; i< foodTypeList.size(); i++){
            if(position>=sum){
                sort++;
            }else {
                return sort;
            }
            sum += foodTypeList.get(i).getCommodityList().size();
        }
        return sort;
    }

    /**
     * ===================================================================================================
     * header的ViewHolder
     * ===================================================================================================
     */
    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.header_detail_list, viewGroup, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        TextView textView = (TextView) viewHolder.itemView;
        textView.setText(foodTypeList.get(getSortType(position)).getTypeName());
    }


    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
    /**
     * ==================================================================================================
     * 以下为contentViewHolder
     * ==================================================================================================
     */
    @Override
    public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recy_item_detail, parent, false);
        return new ContentViewHolder(view,detailOnItemClick);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ContentViewHolder viewHolder = (ContentViewHolder) holder;

        viewHolder.text_name.setText(foodDetailList.get(position).getCommodityName());
        ImageLoading.common(mContext, Constant.ip+foodDetailList.get(position).getCommodityPic(),viewHolder.image_logo,R.drawable.img_loadfail);
        viewHolder.tvGoodsDescription.setText(foodDetailList.get(position).getCommodityContent());
        viewHolder.tvGoodsPrice.setText(foodDetailList.get(position).getCommodityAmt());
        //item点击
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(detailOnItemClick!=null){
                    detailOnItemClick.detailOnItemClick(view,position);
                }
            }
        });
    }

    private class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView text_name;//菜名
        ImageView image_logo;//菜图片
        TextView tvGoodsPrice;//单价
        TextView tvGoodsDescription;//描述
        TextView text_monSaleNum;   //月售
        DetailOnItemClick detailOnItemClick;
        public ContentViewHolder(View itemView,DetailOnItemClick detailOnItemClick) {
            super(itemView);
        text_name = (TextView) itemView.findViewById(R.id.textview_detail);
        image_logo = (ImageView) itemView.findViewById(R.id.imageview_superlogo);
        tvGoodsPrice=(TextView)itemView.findViewById(R.id.tvGoodsPrice);
        tvGoodsDescription=(TextView)itemView.findViewById(R.id.tvGoodsDescription);
        text_monSaleNum=(TextView)itemView.findViewById(R.id.text_monSaleNum);

        }
    }

    //添加  --减少  item点击
    public DetailOnItemClick detailOnItemClick;
    public void setOnItemClickListener(DetailOnItemClick onItemClickListener) {
        this.detailOnItemClick = onItemClickListener;
    }
    public interface DetailOnItemClick{
        void detailOnItemClick(View view, int position);
    }
}
