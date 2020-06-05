package com.prakash.App30daysofkotlin.view.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.prakash.App30daysofkotlin.R;
import com.prakash.App30daysofkotlin.model.MyListData;
import com.prakash.App30daysofkotlin.view.database.DbHelper;
import com.prakash.App30daysofkotlin.view.ui.adapter.MyListAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DailyExpensesActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    MenuItem amtMenu;

    @BindView(R.id.add_btn)
    Button addButton;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.empty_view)
    RelativeLayout emptyView;

    @BindView(R.id.recycler_view_list)
    RecyclerView recyclerView;

    String dateString = "";
    String uiDateString = "";
    int dailyTotal = 0;

    DbHelper dbhelper;
    ArrayList<MyListData> myListData = new ArrayList<MyListData>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_expenses);
        ButterKnife.bind(this);
        dbhelper = new DbHelper(this);

        setToolbar();
        Intent intent = getIntent();
        getIntentValues(intent);
        setListeners();

    }


    /**
     * onCreateOptionMenu is called at point onCreate call finishes
     * set initial value in onCreateOptionMenu
     * update values using onPrepareOptionMenu
     * MenuItem is decalred at top (without binding)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.daily_toolbar_menu, menu);
        amtMenu = menu.findItem(R.id.ddamt_item);
        amtMenu.setTitle("dummy");
        return true;
    }

    private void setToolbar() {
        toolbar.setTitle("title here");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        amtMenu.setTitle(String.valueOf(dailyTotal));
        return true;
    }



    public void setListeners() {

        //swipe to refresh
        swipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                displayListValues();

                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    public void displayListValues() {

        getData();

        // set value to recyclerview
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_list);
        MyListAdapter adapter = new MyListAdapter(this, myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


    //get value from db
    private void getData() {

        //reset defaults
        myListData.clear();
        dailyTotal = 0;

        //get values n calculate dailyTotal
        myListData.addAll( dbhelper.getAllDateCashHistory(dateString));

       dailyTotal = dbhelper.getAllDateCashHistoryTotal(dateString);

       //check for data
        if(myListData.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        updateDailyTotal();
    }


    public void updateDailyTotal(){
        invalidateOptionsMenu();
        //Toast.makeText(this,"daily total: "+ dailyTotal.toString(),Toast.LENGTH_SHORT).show();
    }



    @OnClick(R.id.add_btn)
    public void addButton() {
        Intent i = new Intent(this, NewExpensesActivity.class);
        i.putExtra("date", dateString);
        startActivity(i);
        overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_left_to_right);
    }


    public void getIntentValues(Intent intent) {
        uiDateString = "" + intent.getStringExtra("uidate");
        dateString = "" + intent.getStringExtra("date");
        setIntentValues();
    }

    public void setIntentValues() {
        toolbar.setTitle(uiDateString);
    }



    @Override
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(R.anim.back_left_to_right, R.anim.back_right_to_left);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.back_left_to_right, R.anim.back_right_to_left);
    }

    @Override
    protected void onResume() {
        displayListValues();
        super.onResume();
    }

}
