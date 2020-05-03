package com.app.gpas2;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<UserDetails> userInfoList;
    private UserAdapter.OnUserListener mOnUserListener;

    public UserAdapter(List<UserDetails> userInfoList, UserAdapter.OnUserListener onUserListener) {

        this.userInfoList = userInfoList;
        this.mOnUserListener = onUserListener;
    }

    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list, parent, false);
        UserAdapter.UserViewHolder viewHolder = new UserAdapter.UserViewHolder(view, mOnUserListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserAdapter.UserViewHolder holder, int position) {
        UserDetails users = userInfoList.get(position);
        holder.userViewTitle.setText(users.getName());
        holder.userViewDesig.setText(users.getDesignation());
        holder.userViewDept.setText(String.valueOf(users.getDepartment()));
        holder.userViewEmail.setText(String.valueOf(users.getEmail()));
    }

    @Override
    public int getItemCount() {
        return userInfoList.size();
    }

    public interface OnUserListener {

        void onUserClick(int position);
        void onDeleteClick(int position);

    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        TextView userViewTitle, userViewDesig, userViewDept, userViewEmail;
        Button userDelete;
        UserAdapter.OnUserListener onUserListener;

        public UserViewHolder(View itemView, final UserAdapter.OnUserListener onUserListener) {
            super(itemView);

            userViewTitle = itemView.findViewById(R.id.userViewTitle);
            userViewDesig = itemView.findViewById(R.id.userViewDesig);
            userViewDept = itemView.findViewById(R.id.userViewDept);
            userViewEmail = itemView.findViewById(R.id.userViewEmail);

            userDelete = itemView.findViewById(R.id.userViewEmail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onUserListener != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            onUserListener.onUserClick(getAdapterPosition());
                        }
                    }
                }
            });

            userDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onUserListener != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            onUserListener.onDeleteClick(getAdapterPosition());
                        }
                    }
                }
            });


        }
    }
}
