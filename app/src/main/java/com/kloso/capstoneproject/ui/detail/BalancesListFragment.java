package com.kloso.capstoneproject.ui.detail;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kloso.capstoneproject.R;
import com.kloso.capstoneproject.data.FirestoreViewModel;
import com.kloso.capstoneproject.data.model.ExpenseGroup;
import com.mynameismidori.currencypicker.ExtendedCurrency;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BalancesListFragment extends Fragment {

    @BindView(R.id.rv_owing_list)
    RecyclerView recyclerView;

    private ExpenseGroup expenseGroup;
    private BalanceListAdapter balanceListAdapter;

    private final String TAG = this.getClass().getName();

    public BalancesListFragment(ExpenseGroup expenseGroup){
        this.expenseGroup = expenseGroup;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_owing_list, container, false);
        ButterKnife.bind(this, rootView);

        setUpRecyclerView();
        balanceListAdapter.setTransactionList(expenseGroup.getTransactionList());

        FirestoreViewModel firestoreViewModel = new ViewModelProvider(this).get(FirestoreViewModel.class);
        firestoreViewModel.getExpenseGroupLiveData(expenseGroup.getId()).observe(getViewLifecycleOwner(), obtainedExpenseGroup -> {
            Log.i(TAG, "Obtained new expense group data. Setting the expense list to the view");
            this.expenseGroup = obtainedExpenseGroup;
            balanceListAdapter.setTransactionList(expenseGroup.getTransactionList());
        });

        return rootView;
    }

    private void setUpRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        balanceListAdapter = new BalanceListAdapter(ExtendedCurrency.getCurrencyByName(expenseGroup.getCurrencyCode()));
        recyclerView.setAdapter(balanceListAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

}
