package com.kloso.capstoneproject.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kloso.capstoneproject.Constants;
import com.kloso.capstoneproject.R;
import com.kloso.capstoneproject.data.FirestoreViewModel;
import com.kloso.capstoneproject.data.model.ExpenseGroup;
import com.kloso.capstoneproject.ui.detail.DetailActivity;
import com.kloso.capstoneproject.ui.SwipeToDeleteCallback;
import com.kloso.capstoneproject.ui.ViewAnimation;
import com.kloso.capstoneproject.ui.create.group.CreateGroupActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity  implements ExpenseGroupsAdapter.ExpenseGroupClickListener {

    private final String TAG = MainActivity.class.getName();

    @BindView(R.id.rv_expense_group)
    RecyclerView expenseRecyclerView;
    private ExpenseGroupsAdapter adapter;

    @BindView(R.id.fb_main_menu)
    FloatingActionButton mainFab;
    @BindView(R.id.fb_main_add)
    FloatingActionButton addGroupFab;
    @BindView(R.id.fb_main_import)
    FloatingActionButton importGroupFab;

    @BindView(R.id.import_container)
    LinearLayout importContainer;
    @BindView(R.id.add_container)
    LinearLayout addContainer;
    @BindView(R.id.tv_add)
    TextView addTextView;
    @BindView(R.id.tv_import)
    TextView importTextView;

    private boolean isFabOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setUpRecyclerView();

        setUpFABs();

    }

    private void setUpRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        expenseRecyclerView.setLayoutManager(linearLayoutManager);
        expenseRecyclerView.setHasFixedSize(true);
        adapter = new ExpenseGroupsAdapter(this, this);
        expenseRecyclerView.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(this, adapter));
        itemTouchHelper.attachToRecyclerView(expenseRecyclerView);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(expenseRecyclerView.getContext(),
                linearLayoutManager.getOrientation());
        expenseRecyclerView.addItemDecoration(dividerItemDecoration);

        FirestoreViewModel firestoreViewModel = new ViewModelProvider(this).get(FirestoreViewModel.class);
        firestoreViewModel.getExpenseGroups().observe(this, expenseGroups -> {
            Log.i(TAG, "Received new Expense Groups data. Size: " + expenseGroups.size());
            adapter.setExpenseGroupList(expenseGroups);
        });
    }

    private void setUpFABs(){
        isFabOpen = false;

        mainFab.setOnClickListener(fab -> {
            if(!isFabOpen){
                showFABMenu();
            } else {
                closeFABMenu();
            }
        });

        addGroupFab.setOnClickListener(fab -> {
            Intent intent = new Intent(this, CreateGroupActivity.class);
            startActivity(intent);
        });
        importGroupFab.setOnClickListener(fab -> Toast.makeText(this, "Import group clicked", Toast.LENGTH_SHORT).show());
    }

    public void showFABMenu(){
        isFabOpen = true;
        ViewAnimation.rotateFab(mainFab, true);
        ViewAnimation.animateFabTranslation(addContainer, -getResources().getDimension(R.dimen.standard_65));
        ViewAnimation.animateFabTranslation(importContainer, -getResources().getDimension(R.dimen.standard_130));

        changeFabTextViewVisibility();
    }

    public void closeFABMenu(){
        isFabOpen = false;
        ViewAnimation.rotateFab(mainFab, false);
        ViewAnimation.animateFabTranslation(addContainer,0);
        ViewAnimation.animateFabTranslation(importContainer, 0);

        changeFabTextViewVisibility();
    }

    private void changeFabTextViewVisibility(){
        addTextView.setVisibility(isFabOpen ? View.VISIBLE : View.GONE);
        importTextView.setVisibility(isFabOpen ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onBackPressed() {
        if(!isFabOpen){
            super.onBackPressed();
        } else {
            closeFABMenu();
        }
    }

    @Override
    public void onGroupClick(ExpenseGroup clickedGroup) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Constants.EXPENSE_GROUP, clickedGroup);
        startActivity(intent);
    }

}
