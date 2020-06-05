package com.prakash.App30daysofkotlin.view.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.prakash.App30daysofkotlin.R;
import com.prakash.App30daysofkotlin.view.database.DbHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EditExpensesActivity extends AppCompatActivity {

    @BindView(R.id.nameEditText)
    EditText nameEditText;

    @BindView(R.id.amtEditText)
    EditText amtEditText;

    @BindView(R.id.name_layout)
    TextInputLayout nameLayout;

    @BindView(R.id.amt_layout)
    TextInputLayout amtLayout;

    String dateString;
    String nameString;
    int amt = 0;
    int id = 0;

    DbHelper dbhelper;
    AlertDialog.Builder builder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expenses);
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
        id = intent.getIntExtra("id", id);
        nameString = intent.getStringExtra("name");
        amt = intent.getIntExtra("amt", amt);
        dateString = "" + intent.getStringExtra("date");

        setIntentValues();
    }

    private void setIntentValues() {
        nameEditText.setText(nameString);
        amtEditText.setText(String.valueOf(amt));
    }


    @OnClick(R.id.update_button)
    public void updateButton(){

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

        updateRow();

    }

    public void updateRow(){
        boolean queryResult = false;
        queryResult = dbhelper.updateCashHistory( id,""+nameEditText.getText().toString(), Integer.parseInt(amtEditText.getText().toString()), ""+dateString );

        if(queryResult) {
            Toast.makeText(this, "Update success",Toast.LENGTH_LONG).show();
            finish();
            overridePendingTransition(
                    R.anim.back_left_to_right,
                    R.anim.back_right_to_left
            );
        } else {
            Toast.makeText(this, "Error occurred",Toast.LENGTH_LONG).show();
        }
    }

/*    public void EmptyEditTextAfterDataInsert(){

        nameEditText.getText().clear();

        amtEditText.getText().clear();

    }*/



    @Override
    public boolean onSupportNavigateUp() {
        alertDialog();
        return true;
    }

    @Override
    public void onBackPressed() {
        alertDialog();
    }

   //check for data changed
    public void alertDialog() {

        if ( !nameEditText.getText().toString().equals(nameString) || !amtEditText.getText().toString().equals(String.valueOf(amt)) ) {
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Content Changed");
            builder.setMessage("Do you want to save your edits?")
                    .setCancelable(false)
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            updateRow();
                            finish();
                            overridePendingTransition(R.anim.back_left_to_right, R.anim.back_right_to_left);
                        }
                    })
                    .setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            overridePendingTransition(R.anim.back_left_to_right, R.anim.back_right_to_left);
                        }
                    });
//                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.dismiss();
//                        }
//                    });

            //Creating dialog box
            AlertDialog alert = builder.create();
            alert.setCancelable(false);
            alert.setCanceledOnTouchOutside(false);
            alert.show();
        } else {
            finish();
            overridePendingTransition(R.anim.back_left_to_right, R.anim.back_right_to_left);
        }
    }

}
