package com.kloso.capstoneproject.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kloso.capstoneproject.R;
import com.kloso.capstoneproject.data.model.ExpenseGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpenseGroupsAdapter extends RecyclerView.Adapter<ExpenseGroupsAdapter.ExpenseGroupViewHolder> {

    private final ExpenseGroupClickListener clickListener;
    private List<ExpenseGroup> expenseGroupList;

    public ExpenseGroupsAdapter(ExpenseGroupClickListener clickListener){
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public ExpenseGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_list_layout, parent, false);
        return new ExpenseGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseGroupViewHolder holder, int position) {
        holder.bind(expenseGroupList.get(position));
    }

    @Override
    public int getItemCount() {
        return expenseGroupList != null ? expenseGroupList.size() : 0;
    }

    public void setExpenseGroupList(List<ExpenseGroup> expenseGroupList){
        this.expenseGroupList = expenseGroupList;
    }

    class ExpenseGroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_group_title)
        TextView groupName;
        @BindView(R.id.tv_group_description)
        TextView groupDescription;

        ExpenseGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(ExpenseGroup group){
            groupName.setText(group.getName());
            groupDescription.setText(group.getDescription());
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            clickListener.onGroupClick(expenseGroupList.get(clickedPosition));
        }
    }

    public interface ExpenseGroupClickListener {
        void onGroupClick(ExpenseGroup clickedGroup);
    }

}
