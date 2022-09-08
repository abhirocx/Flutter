package com.np.namasteyoga.ui.quiz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.np.namasteyoga.R;
import com.np.namasteyoga.datasource.response.Datum;
import com.np.namasteyoga.utils.UIUtils;

import java.util.List;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.ViewHolder> {

    List<Datum> pollDataList;
    Context mContext;
    protected ItemListener mListener;
    private int selected_item = -1;


    public QuizListAdapter(Context context, List<Datum> pollDataList, ItemListener itemListener) {
        this.pollDataList = pollDataList;
        this.mContext = context;
        this.mListener = itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvPollName;
        public ImageView ivRadio;
        Datum data;

        public ViewHolder(View v) {
            super(v);
            //  v.setOnClickListener(this);

            tvPollName = (TextView) v.findViewById(R.id.tvQuizName);
            ivRadio = (ImageView) v.findViewById(R.id.ivRadio);
        }

        public void setData(Datum item) {
            this.data = item;
            tvPollName.setText(UIUtils.INSTANCE.getCapitalized(item.getQuiz_name()));
            ivRadio.setImageResource(R.drawable.unselect_radio);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.quiz_list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    selected_item = position;
                    holder.ivRadio.setImageResource(R.drawable.selected_radio);
                    mListener.onItemClick(pollDataList.get(position));
                    notifyDataSetChanged();
                }
            }
        });
        holder.setData(pollDataList.get(position));

        if (position == selected_item) {
            holder.ivRadio.setImageResource(R.drawable.selected_radio);
        } else {
            holder.ivRadio.setImageResource(R.drawable.unselect_radio);
        }
    }

    @Override
    public int getItemCount() {
        return pollDataList.size();
    }

    public interface ItemListener {
        void onItemClick(Datum item);
    }
}