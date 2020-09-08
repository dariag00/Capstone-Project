package com.kloso.capstoneproject.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.kloso.capstoneproject.data.model.ExpenseGroup;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class FirestoreViewModel extends ViewModel {

    private final String TAG = FirestoreViewModel.class.getName();
    private ExpenseGroupRepository expenseGroupRepository = new ExpenseGroupRepository();
    private MutableLiveData<List<ExpenseGroup>> savedExpenseGroups = new MutableLiveData<>();

    public void saveExpenseGroup(ExpenseGroup expenseGroup){
        expenseGroupRepository.saveExpenseGroupItem(expenseGroup).addOnFailureListener(runnable -> {
            Log.e(TAG, "Failed to save Expense Group");
        });
    }

    public LiveData<List<ExpenseGroup>> getExpenseGroups(){
        expenseGroupRepository.getExpenseGroups().addSnapshotListener((value, error) -> {
            if (error != null){
                Log.w(TAG, "Snapshot listener failed: ", error);
                savedExpenseGroups.setValue(null);
            } else {
                List<ExpenseGroup> expenseGroups = new ArrayList<>();
                for(QueryDocumentSnapshot document : value){
                    expenseGroups.add(document.toObject(ExpenseGroup.class));
                }

                savedExpenseGroups.setValue(expenseGroups);
            }
        });

        return savedExpenseGroups;
    }

    public void deleteExpenseGroup(ExpenseGroup expenseGroup){
        expenseGroupRepository.deleteExpenseGroup(expenseGroup).addOnFailureListener(runnable -> {
            Log.e(TAG, "Failed to delete expense group");
        });
    }


}
