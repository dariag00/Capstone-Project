package com.kloso.capstoneproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kloso.capstoneproject.data.FirestoreViewModel;
import com.kloso.capstoneproject.data.model.ExpenseGroup;
import com.kloso.capstoneproject.data.model.Participant;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddParticipantActivity extends AppCompatActivity {

    @BindView(R.id.tv_export_code)
    TextView exportCodeView;
    @BindView(R.id.et_participant_name)
    EditText participantNameView;
    @BindView(R.id.button_add_participant)
    Button addParticipantButton;

    private ExpenseGroup expenseGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_participant);

        ButterKnife.bind(this);

        Intent previousIntent = getIntent();
        expenseGroup = (ExpenseGroup) previousIntent.getSerializableExtra(Constants.EXPENSE_GROUP);

        exportCodeView.setText(this.expenseGroup.generateExportCode());

        exportCodeView.setOnLongClickListener(view -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Label", exportCodeView.getText());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Code copied to clipboard!", Toast.LENGTH_SHORT).show();
            return true;
        });

        addParticipantButton.setOnClickListener(view -> processAddParticipantAttempt());
    }

    private void processAddParticipantAttempt(){
        String name = participantNameView.getText().toString();
        if(!name.isEmpty()){
            Participant participant = new Participant(name);
            expenseGroup.addParticipant(participant);
            FirestoreViewModel firestoreViewModel = new ViewModelProvider(this).get(FirestoreViewModel.class);
            firestoreViewModel.updateExpenseGroup(expenseGroup);
            Toast.makeText(this, "Participant added successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
