package com.kloso.capstoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.kloso.capstoneproject.data.FirestoreViewModel;
import com.kloso.capstoneproject.data.model.ExpenseGroup;
import com.kloso.capstoneproject.data.model.Participant;
import com.kloso.capstoneproject.data.model.User;
import com.kloso.capstoneproject.ui.create.expense.ParticipantsAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AssociateUserActivity extends AppCompatActivity {

    private ExpenseGroup expenseGroup;
    private ParticipantsAdapter participantsAdapter;
    private User user;

    @BindView(R.id.rv_participants)
    RecyclerView recyclerView;
    @BindView(R.id.et_participant_name)
    EditText participantNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_associate_user);

        ButterKnife.bind(this);

        Intent previousIntent = getIntent();
        expenseGroup = (ExpenseGroup) previousIntent.getSerializableExtra(Constants.EXPENSE_GROUP);
        user = (User) previousIntent.getSerializableExtra(Constants.USER);

        setUpRecyclerView();
    }

    private void setUpRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        participantsAdapter = new ParticipantsAdapter(expenseGroup.getParticipants(), true);
        recyclerView.setAdapter(participantsAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private boolean processAssociationAttempt(){

        String associatedUserId = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        if(participantNameText.getText().toString().isEmpty()) {
            List<Participant> expenseParticipants = participantsAdapter.getSelectedParticipants();
            if (expenseParticipants.size() == 1) {
                Participant selectedParticipant = expenseGroup.getParticipantByName(expenseParticipants.get(0).getName());
                selectedParticipant.setRealUser(true);
                selectedParticipant.setAssociatedUserId(associatedUserId);
                selectedParticipant.setProfilePictureUri(user.getProfilePicUri());

                expenseGroup.getParticipantByName(selectedParticipant.getName());

                saveGroup(expenseGroup, associatedUserId);
                return true;
            } else {
                Toast.makeText(this, "At least select a participant or create a new one", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Participant newParticipant = new Participant();
            newParticipant.setRealUser(true);
            newParticipant.setAssociatedUserId(associatedUserId);
            newParticipant.setName(participantNameText.getText().toString());
            newParticipant.setProfilePictureUri(user.getProfilePicUri());
            expenseGroup.addParticipant(newParticipant);
            saveGroup(expenseGroup, associatedUserId);
            return true;
        }
    }

    private void saveGroup(ExpenseGroup expenseGroup, String associatedUserId){
        expenseGroup.addAssociatedUser(associatedUserId);
        FirestoreViewModel firestoreViewModel = new ViewModelProvider(this).get(FirestoreViewModel.class);
        firestoreViewModel.updateExpenseGroup(expenseGroup);
        Toast.makeText(this, "Associated correctly with the group.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_expense_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int clickedItem = item.getItemId();
        if(clickedItem == R.id.action_send){
            if(processAssociationAttempt()) {
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }





}
