package com.kloso.capstoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.kloso.capstoneproject.data.FirestoreViewModel;
import com.kloso.capstoneproject.data.model.Expense;
import com.kloso.capstoneproject.data.model.ExpenseGroup;
import com.kloso.capstoneproject.data.model.Participant;
import com.kloso.capstoneproject.ui.dialog.DatePickerFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateExpenseActivity extends AppCompatActivity {

    @BindView(R.id.et_ce_title)
    EditText titleView;
    @BindView(R.id.et_ce_amount)
    EditText amountView;
    @BindView(R.id.et_ce_date)
    EditText dateView;
    @BindView(R.id.et_ce_paid_by)
    EditText paidByView;
    @BindView(R.id.tv_ce_currency)
    TextView currencyView;

    private ExpenseGroup expenseGroup;

    private final String TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_expense);

        ButterKnife.bind(this);

        expenseGroup = (ExpenseGroup) getIntent().getSerializableExtra(Constants.EXPENSE_GROUP);

        dateView.setOnClickListener(view -> showDatePickerDialog());
        paidByView.setOnClickListener(view -> setUpParticipantSelector( ));

        currencyView.setText(expenseGroup.getCurrencyCode());

        this.setTitle("Create Expense");

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
            processCreateExpenseAttempt();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDatePickerDialog(){
        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance((datePicker, year, month, day) -> {
            String selectedDate = day + "/" + (month + 1) + "/" + year;
            dateView.setText(selectedDate);
        });
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void processCreateExpenseAttempt(){
        if(isValidExpenseCreation()){
            try {
                String title = titleView.getText().toString();
                double amount = Double.valueOf(amountView.getText().toString());
                String dateString = dateView.getText().toString();
                Participant selectedParticipant = expenseGroup.getParticipantByName(paidByView.getText().toString());

                Expense expense = new Expense();
                expense.setExpenseName(title);
                expense.setAmount(amount);
                expense.setPaidBy(selectedParticipant);
                expense.setExpenseDate(new SimpleDateFormat("dd/MM/yyyy").parse(dateString));

                expenseGroup.addExpense(expense);

                FirestoreViewModel viewModel = new ViewModelProvider(this).get(FirestoreViewModel.class);
                viewModel.updateExpenseGroup(expenseGroup);

                finish();
            } catch (ParseException e) {
                e.printStackTrace();
                //TODO avisar del pete
            }
        }
    }

    private boolean isValidExpenseCreation(){
        boolean isValid = true;

        if(paidByView.getText().toString().isEmpty()){
            isValid = false;
        } else if(titleView.getText().toString().isEmpty()){
            isValid = false;
        } else if (amountView.getText().toString().isEmpty()){
            isValid = false;
        }

        return isValid;
    }

    private void setUpParticipantSelector(){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(CreateExpenseActivity.this);
        builderSingle.setTitle("Select One:");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CreateExpenseActivity.this, android.R.layout.select_dialog_singlechoice);
        for(Participant participant : expenseGroup.getParticipants()){
            arrayAdapter.add(participant.getName());
        }

        builderSingle.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            String selectedParticipantName = arrayAdapter.getItem(which);
            //TODO cambiar
            Log.i(TAG, "Selected participant as payer: " + selectedParticipantName);
            paidByView.setText(selectedParticipantName);
        });
        builderSingle.show();
    }

}
