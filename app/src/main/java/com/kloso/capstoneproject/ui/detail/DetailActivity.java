package com.kloso.capstoneproject.ui.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.kloso.capstoneproject.BalanceCalculator;
import com.kloso.capstoneproject.Constants;
import com.kloso.capstoneproject.data.model.Participant;
import com.kloso.capstoneproject.ui.create.expense.CreateExpenseActivity;
import com.kloso.capstoneproject.R;
import com.kloso.capstoneproject.data.FirestoreViewModel;
import com.kloso.capstoneproject.data.model.ExpenseGroup;
import com.kloso.capstoneproject.ui.ViewAnimation;

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

    @BindView(R.id.toolbar_chart)
    BarChart balanceChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Intent previousIntent = getIntent();
        expenseGroup = (ExpenseGroup) previousIntent.getSerializableExtra(Constants.EXPENSE_GROUP);
        toolbarLayout.setTitle(expenseGroup.getName());
        toolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);

        FirestoreViewModel firestoreViewModel = new ViewModelProvider(this).get(FirestoreViewModel.class);
        firestoreViewModel.getExpenseGroupLiveData(expenseGroup.getId()).observe(this, obtainedExpenseGroup -> {
            Log.i("DeleteItemOnAdapter", "Obtained new expense group data. Setting the expense list to the view");
            this.expenseGroup = obtainedExpenseGroup;
            BalanceCalculator.calculateBalances(expenseGroup.getExpenseList(), expenseGroup.getParticipants());
            setChartData(expenseGroup.getParticipants());
        });

        setUpFABs();
        setUpViewPager();
        setUpChart();
    }

    private void setUpViewPager(){
        tabLayout.setupWithViewPager(viewPager);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new BalancesListFragment(expenseGroup));
        fragmentList.add(new ExpenseListFragment(expenseGroup));
        fragmentList.add(new ParticipantsListFragment(expenseGroup));
        FragmentStatePagerAdapter pagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragmentList);
        viewPager.setAdapter(pagerAdapter);
    }

    private void setUpChart(){
        balanceChart.setDrawBarShadow(false);
        balanceChart.setDrawValueAboveBar(true);
        balanceChart.setMaxVisibleValueCount(60);
        balanceChart.setPinchZoom(false);
        balanceChart.getDescription().setEnabled(false);

        balanceChart.getXAxis().setDrawAxisLine(false);
        balanceChart.getXAxis().setDrawGridLines(false);

        balanceChart.getAxisLeft().setDrawLabels(false);
        balanceChart.getAxisLeft().setDrawAxisLine(false);
        balanceChart.getAxisLeft().setDrawGridLines(false);
        balanceChart.getAxisLeft().setDrawZeroLine(true);
        balanceChart.getAxisLeft().setZeroLineWidth(1f);

        balanceChart.getAxisRight().setDrawAxisLine(false);
        balanceChart.getAxisRight().setDrawGridLines(false);
        balanceChart.getAxisRight().setDrawLabels(false);

        balanceChart.getLegend().setEnabled(false);
    }

    private void setChartData(List<Participant> participants){

        ArrayList<BarEntry> values = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();
        List<String> xAxisValues = new ArrayList<>();

        int green = Color.rgb(110, 190, 102);
        int red = Color.rgb(211, 74, 88);

        for(int i = 0; i<participants.size(); i++){
            Participant participant = participants.get(i);
            System.out.println("Balance: " + participant.getNetBalance());
            BarEntry barEntry = new BarEntry(i, (float) participant.getNetBalance());
            values.add(barEntry);

            xAxisValues.add(participant.getName());

            if(participant.getNetBalance() >= 0){
                colors.add(green);
            } else {
                colors.add(red);
            }
        }

        BarDataSet set;

        balanceChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisValues));
        balanceChart.getXAxis().setLabelCount(xAxisValues.size());

        if(balanceChart.getData() != null && balanceChart.getData().getDataSetCount() > 0){
            set = (BarDataSet) balanceChart.getData().getDataSetByIndex(0);
            set.setValues(values);
            balanceChart.getData().notifyDataChanged();
            balanceChart.notifyDataSetChanged();
        } else {
            set = new BarDataSet(values, "Values");
            set.setColors(colors);
            set.setValueTextColors(colors);

            BarData data = new BarData(set);
            data.setValueTextSize(13f);
            data.setBarWidth(0.8f);

            balanceChart.setData(data);
            balanceChart.invalidate();
        }

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
