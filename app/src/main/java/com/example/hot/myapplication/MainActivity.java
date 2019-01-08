package com.example.hot.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.graphics.Color;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    Button button;
    TextView messagetext, registertext;
    Random randNumber = new Random();
    EditText loginedit,passwordedit;
    String Password,Login;
    DBHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setTitle("Your Database");

        button = (Button) findViewById(R.id.LoginButton);
        messagetext = (TextView) findViewById(R.id.MessageText);
        registertext = (TextView) findViewById(R.id.RegisterText);

        loginedit = (EditText) findViewById(R.id.LoginText);
        passwordedit = (EditText) findViewById(R.id.PasswordText);

        dbHelper = new DBHelper(this);

        registertext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login = loginedit.getText().toString();
                Password = passwordedit.getText().toString();

                SQLiteDatabase database = dbHelper.getWritableDatabase();

                Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

                    if (cursor.moveToFirst()) {

                        int loginIndex = cursor.getColumnIndex(DBHelper.KEY_LOGIN);
                        int passwordIndex = cursor.getColumnIndex(DBHelper.KEY_PASSWORD);

                        do {
                            if((Login.equals(cursor.getString(loginIndex)))&(Password.equals(cursor.getString(passwordIndex))))
                            {
                                Intent intent = new Intent(MainActivity.this,TextActivity.class);
                                intent.putExtra("flogin", Login);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                messagetext.setText("Successful login");
                            }
                        } while (cursor.moveToNext());
                    } else messagetext.setText("Wrong login or password");
                cursor.close();
            }
        });
    }

}
