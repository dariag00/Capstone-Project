package com.kloso.capstoneproject.ui.create.expense;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kloso.capstoneproject.Constants;
import com.kloso.capstoneproject.R;
import com.kloso.capstoneproject.data.FirestoreViewModel;
import com.kloso.capstoneproject.data.model.Expense;
import com.kloso.capstoneproject.data.model.ExpenseGroup;
import com.kloso.capstoneproject.data.model.Participant;
import com.kloso.capstoneproject.ui.dialog.DatePickerFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

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
    @BindView(R.id.rv_participants_checkbox)
    RecyclerView recyclerView;

    private ExpenseGroup expenseGroup;
    private ParticipantsAdapter participantsAdapter;

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

        setUpRecyclerView();

    }

    private void setUpRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        participantsAdapter = new ParticipantsAdapter(expenseGroup.getParticipants());
        recyclerView.setAdapter(participantsAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
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

                List<Participant> expenseParticipants = participantsAdapter.getSelectedParticipants();
                expense.setPaidFor(expenseParticipants);

                expenseGroup.addExpense(expense);

                FirestoreViewModel viewModel = new ViewModelProvider(this).get(FirestoreViewModel.class);
                viewModel.updateExpenseGroup(expenseGroup);

                finish();
            } catch (ParseException e) {
                Log.e(TAG, "Error when saving the expense: ", e);
                Toast.makeText(this, "Error when saving the expense. Please, try again later.", Toast.LENGTH_SHORT).show();
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
            Log.i(TAG, "Selected participant as payer: " + selectedParticipantName);
            paidByView.setText(selectedParticipantName);
        });
        builderSingle.show();
    }

}
