package wizrole.hosmerchants.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import wizrole.hosmerchants.R;
import wizrole.hosmerchants.release.model.getgoodtype.StoreCommodityType;

/**
 * Created 2017/3/1.
 */
public class FoodTypeAdapter extends RecyclerView.Adapter<FoodTypeAdapter.ViewHolder>{
    private List<StoreCommodityType> foodList;
    private Context context;
    public FoodTypeAdapter(Context context, List<StoreCommodityType> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    public void setFoodList(List<StoreCommodityType> foodList) {
        this.foodList = foodList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recy_item_category,null));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textView.setText(foodList.get(position).getTypeName());
        holder.textView.setSelected(foodList.get(position).isSelected());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return foodList ==null?0: foodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textview_categoryname);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
