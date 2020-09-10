package com.kloso.capstoneproject.ui.create.group;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.kloso.capstoneproject.R;
import com.kloso.capstoneproject.data.FirestoreViewModel;
import com.kloso.capstoneproject.data.model.ExpenseGroup;
import com.kloso.capstoneproject.data.model.Participant;
import com.kloso.capstoneproject.data.model.User;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.ExtendedCurrency;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateGroupActivity extends AppCompatActivity {

    private String TAG = CreateGroupActivity.class.getName();

    @BindView(R.id.et_creation_title)
    EditText titleView;
    @BindView(R.id.et_creation_description)
    EditText descriptionView;
    @BindView(R.id.rv_participants)
    RecyclerView participantsView;
    @BindView(R.id.et_participant_name)
    EditText participantNameView;
    @BindView(R.id.button_add_participant)
    Button addParticipantButton;
    @BindView(R.id.button_currency)
    Button buttonCurrency;
    @BindView(R.id.button_create)
    Button createButton;

    private ParticipantGroupAdapter groupAdapter;
    private ExtendedCurrency selectedCurrency;
    private List<Participant> participantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        ButterKnife.bind(this);

        participantList = new ArrayList<>();

        attachCurrencyButton();
        populateRecyclerView();

        addParticipantButton.setOnClickListener(view -> {
            String name = participantNameView.getText().toString();
            Log.d(TAG, "Created participant with name: " + name);
            participantList.add(new Participant(name));
            groupAdapter.setParticipantList(participantList);
            participantNameView.setText("");
        });

        createButton.setOnClickListener(view -> createExpenseGroup());

    }

    private void populateRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        participantsView.setLayoutManager(linearLayoutManager);
        participantsView.setHasFixedSize(true);
        groupAdapter = new ParticipantGroupAdapter();
        participantsView.setAdapter(groupAdapter);

        FirestoreViewModel firestoreViewModel = new ViewModelProvider(this).get(FirestoreViewModel.class);
        firestoreViewModel.getUserByEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail()).observe(this, user -> {
            Log.i(TAG, "Recieved new User data");
            createParticipantFromUserData(user);
        });
    }

    private void createParticipantFromUserData(User user){
        Participant participant = new Participant();
        participant.setRealUser(true);
        participant.setName(user.getFullName());
        participant.setAssociatedUserId(user.getEmail());

        participantList.add(participant);
        groupAdapter.setParticipantList(participantList);
    }

    private void attachCurrencyButton(){
        CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");
        buttonCurrency.setOnClickListener(view -> {
            picker.setListener((name, code, symbol, flagDrawableResID) -> {
                selectedCurrency =  ExtendedCurrency.getCurrencyByName(name);
                Log.i(TAG, "Selected " + name + " currency. Symbol: " + selectedCurrency.getSymbol());
                picker.dismiss();
            });
            picker.show(getSupportFragmentManager(), "CURRENCY_PICKER");
        } );
    }

    private void createExpenseGroup(){
        if(validateGroup()) {
            ExpenseGroup expenseGroup = new ExpenseGroup();
            expenseGroup.setParticipants(participantList);
            expenseGroup.setName(titleView.getText().toString());
            expenseGroup.setDescription(descriptionView.getText().toString());
            expenseGroup.setCurrency(selectedCurrency);
            expenseGroup.addUser(FirebaseAuth.getInstance().getCurrentUser().getEmail());

            FirestoreViewModel firestoreViewModel = new ViewModelProvider(this).get(FirestoreViewModel.class);
            firestoreViewModel.saveExpenseGroup(expenseGroup);

            finish();
        }
    }

    private boolean validateGroup(){
        boolean validGroup = true;

        if(titleView.getText().toString().isEmpty()){
            validGroup = false;
            titleView.setError("Title is empty!");
            titleView.requestFocus();
        } else if(descriptionView.getText().toString().isEmpty()){
            validGroup = false;
            descriptionView.setError("Description is empty!");
            descriptionView.requestFocus();
        }

        return validGroup;
    }

}
