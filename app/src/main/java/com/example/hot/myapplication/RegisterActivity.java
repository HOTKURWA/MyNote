package com.example.hot.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    Button registerbutton;
    String Name,Password,Login;
    EditText nameedit,loginedit,passwordedit;
    TextView errorview;
    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loginedit = (EditText) findViewById(R.id.LoginTextEdit);
        passwordedit = (EditText) findViewById(R.id.PasswordTextEdit);
        nameedit = (EditText) findViewById(R.id.NameTextEdit);

        errorview = (TextView) findViewById(R.id.ErrorView);

        registerbutton = (Button) findViewById(R.id.RegisterButton);

        dbHelper = new DBHelper(this);

        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Name = nameedit.getText().toString();
                Login = loginedit.getText().toString();
                Password = passwordedit.getText().toString();

                SQLiteDatabase database = dbHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();

                if((Login.equals(""))||(Password.equals(""))||Login.equals("")) {
                    errorview.setText("Some line is empty");
                }else
                {
                    database = dbHelper.getWritableDatabase();
                    Boolean Mb=false;
                    Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

                    if (cursor.moveToFirst()) {

                        int loginIndex = cursor.getColumnIndex(DBHelper.KEY_LOGIN);

                        do {
                            if(Login.equals(cursor.getString(loginIndex))) {
                                Mb=true;
                                errorview.setText("Login is already using");
                            }
                        } while (cursor.moveToNext());
                    }   cursor.close();

                    if(Mb==false) {
                        contentValues.put(DBHelper.KEY_NAME, Name);
                        contentValues.put(DBHelper.KEY_LOGIN, Login);
                        contentValues.put(DBHelper.KEY_PASSWORD, Password);

                        database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);

                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        dbHelper.close();
                    }
                }
            }
        });

    }
}