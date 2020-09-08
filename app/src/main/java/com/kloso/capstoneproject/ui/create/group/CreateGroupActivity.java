package com.kloso.capstoneproject.ui.create.group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.kloso.capstoneproject.R;
import com.kloso.capstoneproject.data.model.ExpenseGroup;
import com.kloso.capstoneproject.data.model.Participant;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;
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

        attachCurrencyButton();
        populateRecyclerView();

        participantList = new ArrayList<>();

        addParticipantButton.setOnClickListener(view -> {
            String name = participantNameView.getText().toString();
            Log.d(TAG, "Created participant with name: " + name);
            participantList.add(new Participant(name));
            groupAdapter.setParticipantList(participantList);
        });

        createButton.setOnClickListener(view -> createExpenseGroup());

    }

    private void populateRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        participantsView.setLayoutManager(linearLayoutManager);
        participantsView.setHasFixedSize(true);
        groupAdapter = new ParticipantGroupAdapter();
        participantsView.setAdapter(groupAdapter);
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
        }
    }

    private boolean validateGroup(){
        //TODO implement
        return true;
    }

}
