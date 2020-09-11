package com.kloso.capstoneproject.ui.create.group;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
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
    @BindView(R.id.tv_currency)
    TextView selectedCurrencyView;
    @BindView(R.id.pb_create_group)
    ProgressBar progressBar;
    @BindView(R.id.form_login)
    ScrollView formLogin;

    private ParticipantGroupAdapter groupAdapter;
    private ExtendedCurrency selectedCurrency;
    private List<Participant> participantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        ButterKnife.bind(this);

        participantList = new ArrayList<>();

        changeFormVisibility(true);

        setUpCurrencyButton();
        setUpRecyclerView();

        addParticipantButton.setOnClickListener(view -> {
            String name = participantNameView.getText().toString();
            Log.d(TAG, "Created participant with name: " + name);
            participantList.add(new Participant(name));
            groupAdapter.setParticipantList(participantList);
            participantNameView.setText("");
        });

        createButton.setOnClickListener(view -> createExpenseGroup());

    }

    private void setUpRecyclerView(){
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

    private void setUpCurrencyButton(){

        selectedCurrencyView.setText(getString(R.string.selected, ""));

        CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");
        buttonCurrency.setOnClickListener(view -> {
            picker.setListener((name, code, symbol, flagDrawableResID) -> {
                selectedCurrency =  ExtendedCurrency.getCurrencyByName(name);
                Log.i(TAG, "Selected " + name + " currency. Symbol: " + selectedCurrency.getSymbol());
                selectedCurrencyView.setText(getString(R.string.selected, selectedCurrency.getCode()) );
                picker.dismiss();
            });
            picker.show(getSupportFragmentManager(), "CURRENCY_PICKER");
        } );
    }

    private void createExpenseGroup(){

        changeFormVisibility(false);

        if(validateGroup()) {
            ExpenseGroup expenseGroup = new ExpenseGroup();
            expenseGroup.setParticipants(participantList);
            expenseGroup.setName(titleView.getText().toString());
            expenseGroup.setDescription(descriptionView.getText().toString());
            expenseGroup.setCurrencyCode(selectedCurrency.getCode());
            expenseGroup.addUser(FirebaseAuth.getInstance().getCurrentUser().getEmail());

            FirestoreViewModel firestoreViewModel = new ViewModelProvider(this).get(FirestoreViewModel.class);
            firestoreViewModel.saveExpenseGroup(expenseGroup);

            changeFormVisibility(true);
            finish();
        }
    }

    private void changeFormVisibility(boolean showLoginForm){
        formLogin.setVisibility(showLoginForm ? View.VISIBLE : View.GONE);
        progressBar.setVisibility(showLoginForm ? View.GONE :  View.VISIBLE);
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
        } else if(selectedCurrency == null){
            validGroup = false;
            Snackbar.make(findViewById(R.id.main_base_layout), "Please, select a currency", Snackbar.LENGTH_LONG).show();
        }

        return validGroup;
    }

}
