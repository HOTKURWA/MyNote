package com.example.hot.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class TextActivity extends AppCompatActivity {

    String Year, Month, DayOfMonth;
    DBHelper dbHelper;
    String UserName;
    Date date;

    String fLogin;

    TextInputEditText texthint;
    Button mybutton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        mybutton = (Button) findViewById(R.id.ChangeButton);
        CalendarView cw = (CalendarView) findViewById(R.id.calendarView);
        TextView HelloView = (TextView) findViewById(R.id.textView);
        texthint = (TextInputEditText) findViewById(R.id.TextHint);

        Intent getintent = getIntent();
        fLogin = getintent.getStringExtra("flogin");

        dbHelper = new DBHelper(this);

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {

            int loginIndex = cursor.getColumnIndex(DBHelper.KEY_LOGIN);
            int NameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);

            do {
                if ((fLogin.equals(cursor.getString(loginIndex)))) {
                    UserName = cursor.getString(NameIndex);
                }
            } while (cursor.moveToNext());

            HelloView.setText("Hello " + UserName);
        }   cursor.close();

        cw.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
                public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                    Year = Integer.toString(year);
                    Month = Integer.toString(month);
                    DayOfMonth = Integer.toString(dayOfMonth);
                    texthint.setText("");

                    String timeStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
                    mybutton.setVisibility(View.VISIBLE);

                if((10000*year+100*(month+1)+dayOfMonth) < Integer.parseInt(timeStamp))
                {
                    mybutton.setVisibility(View.GONE);
                }

                SQLiteDatabase database = dbHelper.getWritableDatabase();

                Cursor cursor = database.query(DBHelper.TABLE_NOTES, null, null, null, null, null, null);

                if (cursor.moveToFirst()) {

                    int loginIndex = cursor.getColumnIndex(DBHelper.KEY_LOGIN);
                    int DateIndex = cursor.getColumnIndex(DBHelper.KEY_DATE);
                    int NoteIndex = cursor.getColumnIndex(DBHelper.KEY_NOTE);

                    do {
                        if ((fLogin.equals(cursor.getString(loginIndex)))&&((Year +"-"+ Month +"-"+ DayOfMonth).equals(cursor.getString(DateIndex)))) {
                            texthint.setText(cursor.getString(NoteIndex));
                        }
                    } while (cursor.moveToNext());
                }   cursor.close();
            }
            });

            mybutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    boolean Mb=false;
                    String GetText =  texthint.getText().toString();
                    SQLiteDatabase database = dbHelper.getWritableDatabase();

                    Cursor cursor = database.query(DBHelper.TABLE_NOTES, null, null, null, null, null, null);

                    if (cursor.moveToFirst()) {

                        int loginIndex = cursor.getColumnIndex(DBHelper.KEY_LOGIN);
                        int DateIndex = cursor.getColumnIndex(DBHelper.KEY_DATE);

                        do {
                            if ((fLogin.equals(cursor.getString(loginIndex)))&&((Year +"-"+ Month +"-"+ DayOfMonth).equals(cursor.getString(DateIndex)))) {
                                Mb=true;
                                dbHelper.updateData(cursor.getString(DateIndex),cursor.getString(loginIndex),GetText);
                                //update
                            }
                        } while (cursor.moveToNext());
                    }   cursor.close();
                    if(Mb==false)
                    {
                        database = dbHelper.getWritableDatabase();
                        ContentValues contentValues = new ContentValues();

                        contentValues.put(DBHelper.KEY_DATE, Year +"-"+ Month +"-"+ DayOfMonth);
                        contentValues.put(DBHelper.KEY_LOGIN, fLogin);
                        contentValues.put(DBHelper.KEY_NOTE, GetText);

                        database.insert(DBHelper.TABLE_NOTES, null, contentValues);

                    }
                }
            });
    }
}
