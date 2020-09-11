package com.kloso.capstoneproject.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.kloso.capstoneproject.R;
import com.kloso.capstoneproject.ui.main.ExpenseGroupsAdapter;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback{

    private ExpenseGroupsAdapter expenseGroupsAdapter;
    private Drawable deleteIcon;
    private final ColorDrawable backgroundColor;

    public SwipeToDeleteCallback(ExpenseGroupsAdapter expenseGroupsAdapter, Context context){
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.expenseGroupsAdapter = expenseGroupsAdapter;
        backgroundColor = new ColorDrawable(Color.RED);
        deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete_white_24dp);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        expenseGroupsAdapter.deleteItem(position);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        int deleteIconTop = itemView.getTop() + (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
        int deleteIconBottom = deleteIconTop + deleteIcon.getIntrinsicHeight();
        int deleteIconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;

        if(dX > 0){
            backgroundColor.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                    itemView.getBottom());

            int deleteIconRight = itemView.getLeft() + deleteIconMargin + deleteIcon.getIntrinsicWidth();
            int deleteIconLeft = itemView.getLeft() + deleteIconMargin;

            deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);

        } else if(dX < 0){
            backgroundColor.setBounds(itemView.getRight()  + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());

            int deleteIconLeft = itemView.getRight() - deleteIconMargin - deleteIcon.getIntrinsicWidth();
            int deleteIconRight = itemView.getRight() - deleteIconMargin;

            deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);

        } else {
            backgroundColor.setBounds(0, 0, 0, 0);
        }

        backgroundColor.draw(c);
        deleteIcon.draw(c);


        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

    }
}
