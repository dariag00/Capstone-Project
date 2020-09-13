package com.kloso.capstoneproject.ui.detail;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kloso.capstoneproject.R;
import com.kloso.capstoneproject.data.model.Expense;
import com.mynameismidori.currencypicker.ExtendedCurrency;

import java.text.DateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenseList;
    private ExtendedCurrency extendedCurrency;
    private Activity context;
    private final ExpenseLongClickListener expenseLongClickListener;

    public ExpenseAdapter(ExtendedCurrency extendedCurrency, Activity context, ExpenseLongClickListener expenseLongClickListener) {
        this.extendedCurrency = extendedCurrency;
        this.context = context;
        this.expenseLongClickListener = expenseLongClickListener;
    }

    public void setExpenseList(List<Expense> expenseList) {
        this.expenseList = expenseList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_list_item, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        holder.bind(expenseList.get(position));
    }

    @Override
    public int getItemCount() {
        return expenseList != null ? expenseList.size() : 0;
    }

    class ExpenseViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        @BindView(R.id.tv_expense_name)
        TextView expenseNameView;
        @BindView(R.id.tv_expense_amount)
        TextView expenseAmountView;
        @BindView(R.id.tv_paid_by)
        TextView paidByView;
        @BindView(R.id.tv_expense_date)
        TextView expenseDate;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnLongClickListener(this);
        }

        public void bind(Expense expense) {
            expenseNameView.setText(expense.getExpenseName());
            expenseAmountView.setText(expense.getAmount().toString() + extendedCurrency.getSymbol());
            paidByView.setText(expense.getPaidBy().getName());

            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
            expenseDate.setText(dateFormat.format(expense.getExpenseDate()));
        }

        @Override
        public boolean onLongClick(View view) {
            System.out.println("ENTROOO");
            int clickedPosition = getAdapterPosition();
            expenseLongClickListener.onExpenseClick(expenseList.get(clickedPosition), clickedPosition);
            return true;
        }
    }

    public interface ExpenseLongClickListener {
        void onExpenseClick(Expense clickedExpense, int clickedPosition);
    }
}
