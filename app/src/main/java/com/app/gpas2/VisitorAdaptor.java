package com.app.gpas2;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class VisitorAdaptor extends RecyclerView.Adapter<VisitorAdaptor.VisitorsViewHolder> {
    //    private Context mContext;
    private List<VisitorInfo> visitorInfoList;
    private OnVisitorListener mOnVisitorListener;

    public VisitorAdaptor(List<VisitorInfo> visitorInfoList,OnVisitorListener onVisitorListener) {
//        this.mContext = mContext;
        this.visitorInfoList = visitorInfoList;
        this.mOnVisitorListener = onVisitorListener;
    }

    @Override
    public VisitorsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        View view = inflater.inflate(R.layout.visitors_list, null);
//        return new VisitorsViewHolder(view);

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.visitors_list, parent, false);
        VisitorsViewHolder viewHolder = new VisitorsViewHolder(view, mOnVisitorListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VisitorsViewHolder holder, int position) {
        VisitorInfo visitor = visitorInfoList.get(position);
        holder.textViewTitle.setText(visitor.getName());
        holder.textViewShortDesc.setText(visitor.getPurpose());
        holder.textViewRating.setText(String.valueOf(visitor.getConcernPerson()));
        holder.textViewPrice.setText(String.valueOf(visitor.getVDate()));
    }

    @Override
    public int getItemCount() {
        return visitorInfoList.size();
    }

    class VisitorsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewTitle, textViewShortDesc, textViewRating, textViewPrice;
        OnVisitorListener onVisitorListener;

        //        ImageView imageView;
//
        public VisitorsViewHolder(View itemView, OnVisitorListener onVisitorListener) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc);
            textViewRating = itemView.findViewById(R.id.textViewRating);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
//            imageView = itemView.findViewById(R.id.imageView);

            this.onVisitorListener = onVisitorListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onVisitorListener.onVisitorClick(getAdapterPosition());
        }
    }

    public interface OnVisitorListener {

        void onVisitorClick(int position);

    }
}
