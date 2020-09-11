package com.kloso.capstoneproject.ui.main;

import android.app.Activity;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kloso.capstoneproject.R;
import com.kloso.capstoneproject.data.ExpenseGroupRepository;
import com.kloso.capstoneproject.data.model.ExpenseGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpenseGroupsAdapter extends RecyclerView.Adapter<ExpenseGroupsAdapter.ExpenseGroupViewHolder> {

    private final ExpenseGroupClickListener clickListener;
    private List<ExpenseGroup> expenseGroupList;
    private ExpenseGroup deletedItem;
    private int deletedItemPosition;
    private Activity activity;

    public ExpenseGroupsAdapter(ExpenseGroupClickListener clickListener, Activity context){
        this.clickListener = clickListener;
        this.activity = context;
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
        notifyDataSetChanged();
    }

    public void deleteItem(int position){
        showConfirmationSnackbar(position);
    }

    private void showConfirmationSnackbar(int position) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this.activity);
        alert.setTitle("Delete Group");
        alert.setMessage("Are you sure you want to delete it?");
        alert.setPositiveButton(android.R.string.yes, (dialog, which) -> {
            delete(position);
        });
        alert.setNegativeButton(android.R.string.no, (dialog, which) -> {
            restoreItem(position);
            dialog.cancel();
        });
        alert.show();
    }

    private void delete(int position){
        deletedItem = expenseGroupList.get(position);
        deletedItemPosition = position;
        expenseGroupList.remove(position);
        new ExpenseGroupRepository().deleteExpenseGroup(deletedItem);
        notifyItemRemoved(position);
    }

    private void restoreItem(int position){
        notifyItemChanged(position);
        deletedItemPosition = -1;
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
