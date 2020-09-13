package com.kloso.capstoneproject.ui.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.kloso.capstoneproject.R;
import com.kloso.capstoneproject.data.model.ExpenseGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BalancesListFragment extends Fragment {

    @BindView(R.id.rv_owing_list)
    RecyclerView recyclerView;

    private ExpenseGroup expenseGroup;

    public BalancesListFragment(ExpenseGroup expenseGroup){
        this.expenseGroup = expenseGroup;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_owing_list, container, false);
        ButterKnife.bind(this, rootView);

        //BalanceCalculator.calculateBalances(expenseGroup.getExpenseList(), expenseGroup.getParticipants());

        return rootView;
    }

}
