package wizrole.hosmerchants.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import wizrole.hosmerchants.R;
import wizrole.hosmerchants.activity.ImageLookActivity;
import wizrole.hosmerchants.admin.model.evaluation.CommentList;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.util.image.ImageLoading;

/**
 * Created by liushengping on 2018/1/11/011.
 * 何人执笔？
 */

public class MoreEvlautionAdapter extends RecyclerView.Adapter<MoreEvlautionAdapter.EvlauationViewHolder>
implements EvlauationImageAdapter.GridItemListener{

    public Context context;
    public List<CommentList> commentLists;
    public MoreEvlautionAdapter(Context context, List<CommentList> commentLists){
        this.commentLists=commentLists;
        this.context=context;
    }

    @Override
    public EvlauationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EvlauationViewHolder(LayoutInflater.from(context).inflate(R.layout.recy_eclaution_item,null));
    }

    @Override
    public void onBindViewHolder(EvlauationViewHolder holder, final int position) {

        ImageLoading.commonRound(context, Constant.ip+commentLists.get(position).getCommentHostHeadPic(),holder.img_evlau_header);
        holder.text_evlau_name.setText(commentLists.get(position).getCommentHostName());
        holder.img_evlau_content.setText(commentLists.get(position).getCommentContent());
        holder.img_evlau_time.setText(commentLists.get(position).getCommentTime());
        if(commentLists.get(position).getCommentPicList().size()>0){
            holder.grid_evlau_view.setVisibility(View.VISIBLE);
            EvlauationImageAdapter adapter=new EvlauationImageAdapter(context,commentLists.get(position).getCommentPicList());
            holder.grid_evlau_view.setAdapter(adapter);
            adapter.setGridItemListener(position,this);
        }else{
            holder.grid_evlau_view.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * //返回View中Item的个数，这个时候，总的个数应该是ListView中Item的个数加上HeaderView和FooterView
      * @return
     */
    @Override
    public int getItemCount() {
        return commentLists.size();
    }

    @Override
    public void ItemClick(int position_cate,int position) {
        Intent intent=new Intent(context, ImageLookActivity.class);
        intent.putExtra("url",Constant.ip+commentLists.get(position_cate).getCommentPicList().get(position));
        context.startActivity(intent);
    }

    public class EvlauationViewHolder extends RecyclerView.ViewHolder {
        ImageView img_evlau_header;
        TextView text_evlau_name;
        TextView img_evlau_time;
        TextView img_evlau_content;
        GridView grid_evlau_view;

        public EvlauationViewHolder(View itemView) {
            super(itemView);
            img_evlau_header = (ImageView) itemView.findViewById(R.id.img_evlau_header);
            text_evlau_name = (TextView) itemView.findViewById(R.id.text_evlau_name);
            img_evlau_time = (TextView) itemView.findViewById(R.id.img_evlau_time);
            img_evlau_content = (TextView) itemView.findViewById(R.id.img_evlau_content);
            grid_evlau_view = (GridView) itemView.findViewById(R.id.grid_evlau_view);
        }
    }
}
