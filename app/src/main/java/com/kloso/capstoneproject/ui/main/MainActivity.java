package com.kloso.capstoneproject.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kloso.capstoneproject.Constants;
import com.kloso.capstoneproject.R;
import com.kloso.capstoneproject.data.model.ExpenseGroup;
import com.kloso.capstoneproject.ui.DetailActivity;
import com.kloso.capstoneproject.ui.ViewAnimation;

import java.util.ArrayList;
import java.util.List;

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

        populateRecyclerView();

    }

    private void populateRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        expenseRecyclerView.setLayoutManager(linearLayoutManager);
        expenseRecyclerView.setHasFixedSize(true);
        adapter = new ExpenseGroupsAdapter(this);
        expenseRecyclerView.setAdapter(adapter);

        adapter.setExpenseGroupList(populateMockData());

        isFabOpen = false;

        mainFab.setOnClickListener(fab -> {
            if(!isFabOpen){
                showFABMenu();
            } else {
                closeFABMenu();
            }
        });
        
        addGroupFab.setOnClickListener(fab -> Toast.makeText(this, "New group clicked", Toast.LENGTH_SHORT).show());
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


    private List<ExpenseGroup> populateMockData(){
        List<ExpenseGroup> expenseGroupList = new ArrayList<>();


        ExpenseGroup expenseGroup = new ExpenseGroup();
        expenseGroup.setName("Test Group");
        expenseGroup.setDescription("Test Description");
        expenseGroupList.add(expenseGroup);

        expenseGroup = new ExpenseGroup();
        expenseGroup.setName("Test Group");
        expenseGroup.setDescription("Test Description Test Description Test Description Test Description Test Description Test Description Test Description");
        expenseGroupList.add(expenseGroup);

        return expenseGroupList;
    }
}
