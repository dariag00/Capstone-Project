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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kloso.capstoneproject.R;
import com.kloso.capstoneproject.data.ExpenseGroupRepository;
import com.kloso.capstoneproject.data.FirestoreViewModel;
import com.kloso.capstoneproject.data.model.ExpenseGroup;
import com.kloso.capstoneproject.data.model.Participant;
import com.kloso.capstoneproject.ui.create.group.ParticipantGroupAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParticipantsListFragment extends Fragment implements ParticipantGroupAdapter.ParticipantLongClickListener {

    @BindView(R.id.rv_participants)
    RecyclerView recyclerView;

    private ExpenseGroup expenseGroup;
    private final String TAG = this.getClass().getName();
    private ParticipantGroupAdapter groupAdapter;

    public ParticipantsListFragment(ExpenseGroup expenseGroup){
        this.expenseGroup = expenseGroup;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_participants_list, container, false);
        ButterKnife.bind(this, rootView);
        setUpRecyclerView();

        FirestoreViewModel firestoreViewModel = new ViewModelProvider(this).get(FirestoreViewModel.class);
        firestoreViewModel.getExpenseGroupLiveData(expenseGroup.getId()).observe(this, obtainedExpenseGroup -> {
            Log.i(TAG, "Obtained new expense group data. Setting the expense list to the view");
            this.expenseGroup = obtainedExpenseGroup;
            groupAdapter.setParticipantList(expenseGroup.getParticipants());
        });

        return rootView;
    }

    private void setUpRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        groupAdapter = new ParticipantGroupAdapter(this);
        recyclerView.setAdapter(groupAdapter);
        groupAdapter.setParticipantList(expenseGroup.getParticipants());
    }

    @Override
    public void onParticipantClick(Participant clickedParticipant, int clickedPosition) {
        showConfirmationDialog(clickedPosition);
    }

    private void showConfirmationDialog(int position) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this.getActivity(), R.style.DialogTheme);
        alert.setTitle("Delete Participant");
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
        expenseGroup.getParticipants().remove(position);
        new ExpenseGroupRepository().updateExpenseGroup(expenseGroup);
        groupAdapter.notifyItemRemoved(position);
    }

    private void restoreItem(int position){
        groupAdapter.notifyItemChanged(position);
    }
}
