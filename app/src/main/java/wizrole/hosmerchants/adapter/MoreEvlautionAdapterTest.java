package wizrole.hosmerchants.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import wizrole.hosmerchants.R;
import wizrole.hosmerchants.activity.ImageLookActivity;
import wizrole.hosmerchants.adapter.base.ConcreteAdapter;
import wizrole.hosmerchants.adapter.base.ViewHolder;
import wizrole.hosmerchants.admin.model.evaluation.CommentList;
import wizrole.hosmerchants.base.Constant;
import wizrole.hosmerchants.util.image.ImageLoading;

/**
 * Created by liushengping on 2018/1/11/011.
 * 何人执笔？
 */
public class MoreEvlautionAdapterTest extends BaseAdapter implements EvlauationImageAdapter.GridItemListener{

    public Context context;
    public List<CommentList> commentLists;
    public LayoutInflater layoutInflater;
    public MoreEvlautionAdapterTest(Context context,List<CommentList> commentLists){
        this.context=context;
        this.commentLists=commentLists;
        layoutInflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return commentLists.size();
    }

    @Override
    public Object getItem(int position) {
        return commentLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void ItemClick(int position_cate, int position) {
        Intent intent=new Intent(context, ImageLookActivity.class);
        intent.putExtra("url",Constant.ip+commentLists.get(position_cate).getCommentPicList().get(position));
        context.startActivity(intent);
    }

    class Holder{
        ImageView img_evlau_header;
        TextView text_evlau_name;
        TextView img_evlau_content;
        TextView img_evlau_time;
        GridView grid_evlau_view;
    }
    @Override
    public View getView(int position, View itemView, ViewGroup parent) {
        Holder holder=null;
        if(itemView==null){
            holder=new Holder();
            itemView=layoutInflater.inflate(R.layout.recy_eclaution_item,null);
            holder.img_evlau_header = (ImageView) itemView.findViewById(R.id.img_evlau_header);
            holder.text_evlau_name = (TextView) itemView.findViewById(R.id.text_evlau_name);
            holder.img_evlau_time = (TextView) itemView.findViewById(R.id.img_evlau_time);
            holder.img_evlau_content = (TextView) itemView.findViewById(R.id.img_evlau_content);
            holder.grid_evlau_view = (GridView) itemView.findViewById(R.id.grid_evlau_view);
            itemView.setTag(holder);
        }else{
            holder=(Holder) itemView.getTag();
        }
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
        return itemView;
    }
}
