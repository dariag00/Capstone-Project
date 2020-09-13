package com.kloso.capstoneproject.ui.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kloso.capstoneproject.R;
import com.kloso.capstoneproject.data.model.ExpenseGroup;
import com.kloso.capstoneproject.ui.create.group.ParticipantGroupAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParticipantsListFragment extends Fragment {

    @BindView(R.id.rv_participants)
    RecyclerView recyclerView;

    private ExpenseGroup expenseGroup;

    public ParticipantsListFragment(ExpenseGroup expenseGroup){
        this.expenseGroup = expenseGroup;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_participants_list, container, false);
        ButterKnife.bind(this, rootView);
        setUpRecyclerView();
        return rootView;
    }

    private void setUpRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        ParticipantGroupAdapter groupAdapter = new ParticipantGroupAdapter();
        recyclerView.setAdapter(groupAdapter);
        groupAdapter.setParticipantList(expenseGroup.getParticipants());
    }

}
