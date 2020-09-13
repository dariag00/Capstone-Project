package com.kloso.capstoneproject.ui.detail;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kloso.capstoneproject.R;
import com.kloso.capstoneproject.data.ExpenseGroupRepository;
import com.kloso.capstoneproject.data.FirestoreViewModel;
import com.kloso.capstoneproject.data.model.Expense;
import com.kloso.capstoneproject.data.model.ExpenseGroup;
import com.mynameismidori.currencypicker.ExtendedCurrency;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpenseListFragment extends Fragment implements ExpenseAdapter.ExpenseLongClickListener {

    @BindView(R.id.rv_expense_list)
    RecyclerView recyclerView;

    private ExpenseGroup expenseGroup;
    private ExpenseAdapter expenseAdapter;

    private final String TAG = ExpenseListFragment.class.getName();

    public ExpenseListFragment(){

    }

    public ExpenseListFragment(ExpenseGroup expenseGroup){
        this.expenseGroup = expenseGroup;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_expense_list, container, false);
        ButterKnife.bind(this, rootView);
        setUpRecyclerView();

        FirestoreViewModel firestoreViewModel = new ViewModelProvider(this).get(FirestoreViewModel.class);
        firestoreViewModel.getExpenseGroupLiveData(expenseGroup.getId()).observe(this, obtainedExpenseGroup -> {
            Log.i(TAG, "Obtained new expense group data. Setting the expense list to the view");
            this.expenseGroup = obtainedExpenseGroup;
            expenseAdapter.setExpenseList(expenseGroup.getExpenseList());
        });

        return rootView;
    }

    private void setUpRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        expenseAdapter = new ExpenseAdapter(ExtendedCurrency.getCurrencyByName(expenseGroup.getCurrencyCode()), this.getActivity(), this);
        recyclerView.setAdapter(expenseAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onExpenseClick(Expense clickedExpense, int clickedPosition) {
        showConfirmationDialog(clickedExpense, clickedPosition);
    }

    private void showConfirmationDialog(Expense clickedExpense, int position) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this.getActivity());
        alert.setTitle("Delete Expense");
        alert.setMessage("Are you sure you want to delete it?");
        alert.setPositiveButton(android.R.string.yes, (dialog, which) -> {
            delete(position);
        });
        alert.setNegativeButton(android.R.string.no, (dialog, which) -> {
            restoreItem(position);
            dialog.cancel();
        });
        alert.setOnCancelListener(dialogInterface -> {
            restoreItem(position);
        });
        alert.show();
    }

    private void delete(int position){
        expenseGroup.getExpenseList().remove(position);
        new ExpenseGroupRepository().updateExpenseGroup(expenseGroup);
        expenseAdapter.notifyItemRemoved(position);
    }

    private void restoreItem(int position){
        expenseAdapter.notifyItemChanged(position);
    }

}
