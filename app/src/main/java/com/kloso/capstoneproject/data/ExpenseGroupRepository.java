package com.kloso.capstoneproject.data;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.kloso.capstoneproject.data.model.ExpenseGroup;
import com.kloso.capstoneproject.data.model.User;

import dagger.Module;


@Module
public class ExpenseGroupRepository {

    private final String TAG = this.getClass().getName();

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    public Task<Void> saveExpenseGroupItem(ExpenseGroup expenseGroup){
        System.out.println(firebaseUser.getEmail());
        DocumentReference documentReference = firebaseFirestore.collection("expense_groups").document();
        expenseGroup.setId(documentReference.getId());
        return documentReference.set(expenseGroup);
    }

    public Query getExpenseGroups(){
        return firebaseFirestore.collection("expense_groups").whereArrayContains("associatedUsers", firebaseUser.getEmail());
    }

    public Task<Void> deleteExpenseGroup(ExpenseGroup expenseGroup){
        return firebaseFirestore.collection("expense_groups").document(expenseGroup.getId()).delete();
    }

    public Task<Void> updateExpenseGroup(ExpenseGroup expenseGroup){
        return firebaseFirestore.collection("expense_groups").document(expenseGroup.getId()).set(expenseGroup);
    }

    public DocumentReference getExpenseGroup(String expenseGroupId){
        return firebaseFirestore.collection("expense_groups").document(expenseGroupId);
    }

    public Task<Void> saveUserData(User user){
        DocumentReference documentReference = firebaseFirestore.collection("users").document(user.getEmail());
        return documentReference.set(user);
    }

    public Task<DocumentSnapshot> getUserByEmail(String email){
        DocumentReference documentReference = firebaseFirestore.collection("users").document(email);
        return documentReference.get();
    }

    public FirebaseUser getFirebaseUser(){
        return firebaseUser;
    }

}
