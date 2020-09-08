package com.kloso.capstoneproject.data;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kloso.capstoneproject.data.model.ExpenseGroup;

public class ExpenseGroupRepository {

    private final String TAG = this.getClass().getName();

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    public Task<Void> saveExpenseGroupItem(ExpenseGroup expenseGroup){
        return firebaseFirestore.collection("users").document(firebaseUser.getEmail())
                .collection("expense_groups").document(expenseGroup.getId()).set(expenseGroup);
    }

    public CollectionReference getExpenseGroups(){
        return firebaseFirestore.collection("users").document(firebaseUser.getEmail()).collection("expense_groups");
    }

    public Task<Void> deleteExpenseGroup(ExpenseGroup expenseGroup){
        return firebaseFirestore.collection("user").document(firebaseUser.getEmail())
                .collection("expense_groups").document(expenseGroup.getId()).delete();
    }

}
