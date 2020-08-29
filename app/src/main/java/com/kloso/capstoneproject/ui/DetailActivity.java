package com.kloso.capstoneproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.kloso.capstoneproject.Constants;
import com.kloso.capstoneproject.R;
import com.kloso.capstoneproject.data.model.ExpenseGroup;

public class DetailActivity extends AppCompatActivity {

    private ExpenseGroup expenseGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent previousIntent = getIntent();

        expenseGroup = (ExpenseGroup) previousIntent.getSerializableExtra(Constants.EXPENSE_GROUP);

        this.setTitle(expenseGroup.getName());

    }
}
