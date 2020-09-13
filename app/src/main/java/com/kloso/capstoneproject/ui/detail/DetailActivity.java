package com.kloso.capstoneproject.ui.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.kloso.capstoneproject.Constants;
import com.kloso.capstoneproject.CreateExpenseActivity;
import com.kloso.capstoneproject.R;
import com.kloso.capstoneproject.data.FirestoreViewModel;
import com.kloso.capstoneproject.data.model.ExpenseGroup;
import com.kloso.capstoneproject.ui.ViewAnimation;
import com.kloso.capstoneproject.ui.create.group.CreateGroupActivity;
import com.kloso.capstoneproject.ui.create.group.ParticipantGroupAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class DetailActivity extends AppCompatActivity {

    private ExpenseGroup expenseGroup;
    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.detail_viewpager)
    ViewPager viewPager;
    @BindView(R.id.detail_tablayout)
    TabLayout tabLayout;

    @BindView(R.id.fb_detail_menu)
    FloatingActionButton mainFab;
    @BindView(R.id.fb_detail_add_participant)
    FloatingActionButton addParticipantFab;
    @BindView(R.id.fb_detail_add)
    FloatingActionButton addExpenseFab;
    @BindView(R.id.add_parcitipant_container)
    LinearLayout addParticipantContainer;
    @BindView(R.id.add_expense_container)
    LinearLayout addExpenseContainer;
    @BindView(R.id.tv_add_expense)
    TextView addExpenseTextView;
    @BindView(R.id.tv_add_participant)
    TextView addParticipantTextView;

    private boolean isFabOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Intent previousIntent = getIntent();
        expenseGroup = (ExpenseGroup) previousIntent.getSerializableExtra(Constants.EXPENSE_GROUP);
        toolbarLayout.setTitle(expenseGroup.getName());

        FirestoreViewModel firestoreViewModel = new ViewModelProvider(this).get(FirestoreViewModel.class);
        firestoreViewModel.getExpenseGroupLiveData(expenseGroup.getId()).observe(this, obtainedExpenseGroup -> {
            Log.i("DeleteItemOnAdapter", "Obtained new expense group data. Setting the expense list to the view");
            this.expenseGroup = obtainedExpenseGroup;
        });

        setUpFABs();
        setUpViewPager();
    }

    private void setUpViewPager(){
        tabLayout.setupWithViewPager(viewPager);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new BalancesListFragment());
        fragmentList.add(new ExpenseListFragment(expenseGroup));
        fragmentList.add(new ParticipantsListFragment(expenseGroup));
        FragmentStatePagerAdapter pagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragmentList);
        viewPager.setAdapter(pagerAdapter);
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

        addParticipantFab.setOnClickListener(fab -> {
            Toast.makeText(this, "DeleteItemOnAdapter", Toast.LENGTH_LONG).show();
        });
        addExpenseFab.setOnClickListener(fab -> goToCreateExpenseActivity());
    }

    public void showFABMenu(){
        isFabOpen = true;
        ViewAnimation.rotateFab(mainFab, true);
        ViewAnimation.animateFabTranslation(addExpenseContainer, -getResources().getDimension(R.dimen.standard_65));
        ViewAnimation.animateFabTranslation(addParticipantContainer, -getResources().getDimension(R.dimen.standard_130));

        changeFabTextViewVisibility();
    }

    public void closeFABMenu(){
        isFabOpen = false;
        ViewAnimation.rotateFab(mainFab, false);
        ViewAnimation.animateFabTranslation(addExpenseContainer,0);
        ViewAnimation.animateFabTranslation(addParticipantContainer, 0);

        changeFabTextViewVisibility();
    }

    private void changeFabTextViewVisibility(){
        addExpenseTextView.setVisibility(isFabOpen ? View.VISIBLE : View.GONE);
        addParticipantTextView.setVisibility(isFabOpen ? View.VISIBLE : View.GONE);
    }

    private void goToCreateExpenseActivity(){
        Intent intent = new Intent(this, CreateExpenseActivity.class);
        intent.putExtra(Constants.EXPENSE_GROUP, expenseGroup);
        startActivity(intent);
    }

}
