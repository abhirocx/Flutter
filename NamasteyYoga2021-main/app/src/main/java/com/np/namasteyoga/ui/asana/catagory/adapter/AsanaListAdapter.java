package com.np.namasteyoga.ui.asana.catagory.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.np.namasteyoga.R;
import com.np.namasteyoga.datasource.response.AsanaListData;
import com.np.namasteyoga.interfaces.PaginationListener;
import com.np.namasteyoga.utils.RoundedCornersTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.np.namasteyoga.utils.C.ASANA_THUMB_BASE_URL;
import static com.np.namasteyoga.utils.C.ASANA_THUMB_BASE_URL_END;

public class AsanaListAdapter extends RecyclerView.Adapter<AsanaListAdapter.ViewHolder> {

    ArrayList<AsanaListData> asanaListDataList;
    Context mContext;
    private ItemListener mListener;
    private PaginationListener paginationListener;

    public AsanaListAdapter(Context context, ArrayList<AsanaListData> asanaListDataList, ItemListener itemListener,PaginationListener paginationListener) {
        this.asanaListDataList = asanaListDataList;
        this.mContext = context;
        this.mListener = itemListener;
        this.paginationListener = paginationListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView asanaName, asanaDescription, tvAsanaDuration;
        public ImageView asanaImage;
        AsanaListData data;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            asanaName =  v.findViewById(R.id.tvAsanaName);
            asanaDescription =  v.findViewById(R.id.tvAsanaDesc);
            tvAsanaDuration = v.findViewById(R.id.tvAsanaDuration);
            asanaImage =  v.findViewById(R.id.ivAsana);
        }

        public void setData(AsanaListData item) {
            this.data = item;
            asanaName.setText(item.getAasana_name());
            asanaDescription.setText(item.getAasana_description());
            tvAsanaDuration.setText(item.getAssana_video_duration());

            String asanaThumb = ASANA_THUMB_BASE_URL + item.getAssana_video_id() + ASANA_THUMB_BASE_URL_END;
            Picasso.get()
                    .load(asanaThumb)
                    .resize(0, 120)
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_place_holder)
                    .transform(new RoundedCornersTransform(10,0))
                    .into(asanaImage);
        }


        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(data);

            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.asana_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(asanaListDataList.get(position));
        if ((position + 1) >= asanaListDataList.size())
            paginationListener.page();
    }

    @Override
    public int getItemCount() {
        return asanaListDataList.size();
    }

    public interface ItemListener {
        void onItemClick(AsanaListData item);
    }
}