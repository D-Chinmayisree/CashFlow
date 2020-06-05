package com.prakash.App30daysofkotlin.view.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.prakash.App30daysofkotlin.R;
import com.prakash.App30daysofkotlin.view.database.DbHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class NewExpensesActivity extends AppCompatActivity {

    @BindView(R.id.nameEditText)
    EditText nameEditText;

    @BindView(R.id.amtEditText)
    EditText amtEditText;

    @BindView(R.id.name_layout)
    TextInputLayout nameLayout;

    @BindView(R.id.amt_layout)
    TextInputLayout amtLayout;


    int selectedSpinnerInt;
    private ArrayList<String> list = new ArrayList<String>();

    String dateString;

    DbHelper dbhelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expenses);
        ButterKnife.bind(this);
        dbhelper =new DbHelper(this);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Intent intent = getIntent();
        getIntentValues(intent);
    }



    public void getIntentValues(Intent intent) {
        dateString = "" + intent.getStringExtra("date");
    }





    @OnClick(R.id.add_button)
    public void addButton(){

        nameLayout.setErrorEnabled(false);
        amtLayout.setErrorEnabled(false);

        //validate whitespace in edittext
        if(nameEditText.getText().toString().isEmpty() || nameEditText.getText().toString().equals(" ")){
            nameLayout.setError("Enter Valid Item Name");
            return;
        }
        if(amtEditText.getText().toString().isEmpty() ){
            amtLayout.setError("Amount Must Not Be Empty");
            return;
        }

        addNewRow();

    }

    public void addNewRow(){
        boolean queryResult = false;
        queryResult = dbhelper.addCashHistory(""+nameEditText.getText().toString(), Integer.parseInt(amtEditText.getText().toString()), ""+dateString );

        if(queryResult) {
            Toast.makeText(this, "expenses added",Toast.LENGTH_LONG).show();
            onBackPressed();
        } else {
            Toast.makeText(this, "error occurred",Toast.LENGTH_LONG).show();
        }
    }

/*    public void EmptyEditTextAfterDataInsert(){

        nameEditText.getText().clear();

        amtEditText.getText().clear();

    }*/



    @Override
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(R.anim.back_left_to_right,R.anim.back_right_to_left);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.back_left_to_right,R.anim.back_right_to_left);
    }

}
