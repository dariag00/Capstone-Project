package com.kloso.capstoneproject.ui.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kloso.capstoneproject.R;
import com.kloso.capstoneproject.data.model.Transaction;
import com.mynameismidori.currencypicker.ExtendedCurrency;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BalanceListAdapter extends RecyclerView.Adapter<BalanceListAdapter.BalanceViewHolder> {

    private ExtendedCurrency extendedCurrency;
    private List<Transaction> transactionList;

    public BalanceListAdapter(ExtendedCurrency extendedCurrency){
        this.extendedCurrency = extendedCurrency;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BalanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.owing_list_item, parent, false);
        return new BalanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BalanceViewHolder holder, int position) {
        holder.bind(transactionList.get(position));
    }

    @Override
    public int getItemCount() {
        return transactionList != null ? transactionList.size() : 0;
    }


    class BalanceViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_payer_name)
        TextView payerNameView;
        @BindView(R.id.tv_receiver_name)
        TextView receiverNameView;
        @BindView(R.id.tv_owing_amount)
        TextView owingAmountView;

        public BalanceViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Transaction transaction){
            payerNameView.setText(transaction.getPayer().getName());
            receiverNameView.setText(transaction.getReceiver().getName());
            owingAmountView.setText(transaction.getBalanceBigDecimal().abs().toString() + extendedCurrency.getSymbol());
        }
    }

}
