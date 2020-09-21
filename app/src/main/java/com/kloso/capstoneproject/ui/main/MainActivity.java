package com.kloso.capstoneproject.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.kloso.capstoneproject.AssociateUserActivity;
import com.kloso.capstoneproject.Constants;
import com.kloso.capstoneproject.R;
import com.kloso.capstoneproject.data.ExpenseGroupRepository;
import com.kloso.capstoneproject.data.FirestoreViewModel;
import com.kloso.capstoneproject.data.model.ExpenseGroup;
import com.kloso.capstoneproject.data.model.User;
import com.kloso.capstoneproject.ui.detail.DetailActivity;
import com.kloso.capstoneproject.ui.SwipeToDeleteCallback;
import com.kloso.capstoneproject.ui.ViewAnimation;
import com.kloso.capstoneproject.ui.create.group.CreateGroupActivity;
import com.kloso.capstoneproject.ui.login.LoginActivity;

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
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Intent previousIntent = getIntent();
        user = (User) previousIntent.getSerializableExtra(Constants.USER);

        setUpRecyclerView();

        setUpFABs();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int clickedItem = item.getItemId();
        if(clickedItem == R.id.action_log_out){
            logOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        importGroupFab.setOnClickListener(fab -> showImportGroupDialog());
    }

    public void showFABMenu(){
        isFabOpen = true;
        ViewAnimation.rotateFab(mainFab, true);
        ViewAnimation.animateFabTranslationVertically(addContainer, -getResources().getDimension(R.dimen.standard_65));
        ViewAnimation.animateFabTranslationVertically(importContainer, -getResources().getDimension(R.dimen.standard_130));

        changeFabTextViewVisibility();
    }

    public void closeFABMenu(){
        isFabOpen = false;
        ViewAnimation.rotateFab(mainFab, false);
        ViewAnimation.animateFabTranslationVertically(addContainer,0);
        ViewAnimation.animateFabTranslationVertically(importContainer, 0);

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

    private void showImportGroupDialog(){

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.import_group_dialog, null);

        final EditText editText = dialogView.findViewById(R.id.et_import_code);

        new AlertDialog.Builder(this, R.style.DialogTheme)
                .setTitle("Import a group")
                .setView(dialogView)
                .setMessage("To join a previously created group you must enter the generated code that must have been previously provided by a member of the group.")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    String code = editText.getText().toString();
                    String decoded = new String(Base64.decode(code.getBytes(), Base64.DEFAULT));
                    System.out.println("CODE: " + code + " decoded: " + decoded);
                    String groupId = decoded.split("-")[0];
                    new ExpenseGroupRepository().getExpenseGroup(groupId).get().addOnCompleteListener(runnable -> {
                        if(runnable.isSuccessful()){
                            ExpenseGroup expenseGroup = runnable.getResult().toObject(ExpenseGroup.class);
                            if(!expenseGroup.isUserAlreadyAdded(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                                Intent intent = new Intent(this, AssociateUserActivity.class);
                                intent.putExtra(Constants.EXPENSE_GROUP, runnable.getResult().toObject(ExpenseGroup.class));
                                intent.putExtra(Constants.USER, user);
                                startActivity(intent);
                            } else {
                                Toast.makeText(this, "A participant with the same email is already associated with the group", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }).setNegativeButton(android.R.string.no, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                }).show();
    }

    private void logOut(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
