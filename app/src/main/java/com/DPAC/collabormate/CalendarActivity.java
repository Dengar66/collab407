package com.DPAC.collabormate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import com.parse.ParseUser;

public class CalendarActivity extends Activity implements View.OnClickListener {
    //UI References
    private EditText fromDateEtxt;
    private EditText toDateEtxt;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.US);

        findViewsById();

        setDateTimeField();

    }

    private void findViewsById() {
        fromDateEtxt = (EditText) findViewById(R.id.etxt_fromdate);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

        toDateEtxt = (EditText) findViewById(R.id.etxt_todate);
        toDateEtxt.setInputType(InputType.TYPE_NULL);
    }

    private void setDateTimeField() {
        fromDateEtxt.setOnClickListener(this);
        toDateEtxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);

        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public void onClick(View view) {
        if(view == fromDateEtxt) {
            fromDatePickerDialog.show();
        } else if(view == toDateEtxt) {
            toDatePickerDialog.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.about:
                // Use the Builder class for convenient dialog construction
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true);
                builder.setTitle(R.string.about)
                        .setMessage(R.string.dialog_about)
                        .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();    // User presses OK
                            }
                        })
                        .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();    // User presses Cancel
                            }
                        });
                // Create the AlertDialog object and return it
                AlertDialog dialog = builder.create();

                builder.show();
                return (true);

            case R.id.help:
                // Use the Builder class for convenient dialog construction
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setCancelable(true);
                builder1.setTitle(R.string.help)
                        .setMessage(R.string.dialog_help)
                        .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();    // User presses OK
                            }
                        })
                        .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();    // User presses Cancel
                            }
                        });
                // Create the AlertDialog object and return it
                AlertDialog dialog1 = builder1.create();

                builder1.show();
                return (true);

            case R.id.settings:
                startActivity(new Intent(this, Preferences.class));
                return (true);

            case R.id.logout:
                ParseUser.logOut();
                return(true);
        }

        return super.onOptionsItemSelected(item);
    }

    public void addTask(View view) {

        int startDayYear = fromDatePickerDialog.getDatePicker().getYear();
        int startDayMonth = fromDatePickerDialog.getDatePicker().getMonth();
        int startDayDay = fromDatePickerDialog.getDatePicker().getDayOfMonth();
        int startTimeHour = 00;
        int startTimeMin = 00;

        int endDayYear = toDatePickerDialog.getDatePicker().getYear();
        int endDayMonth = toDatePickerDialog.getDatePicker().getMonth();
        int endDayDay = toDatePickerDialog.getDatePicker().getDayOfMonth();
        int endTimeHour = 0;
        int endTimeMin = 0;

        ContentValues values = new ContentValues();
        values.put(CalendarProvider.COLOR, Event.COLOR_BLUE);
        values.put(CalendarProvider.DESCRIPTION, "Some Description");
        values.put(CalendarProvider.LOCATION, "Madison");
        values.put(CalendarProvider.EVENT, "Event name");

        Calendar cal = Calendar.getInstance();

        cal.set(startDayYear, startDayMonth, startDayDay, startTimeHour, startTimeMin);
        values.put(CalendarProvider.START, cal.getTimeInMillis());

        TimeZone tz = TimeZone.getDefault();

        double julianDay = getJulianDay(startDayYear, startDayMonth, startDayDay);
        values.put(CalendarProvider.START_DAY, julianDay);

        cal.set(endDayYear, endDayMonth, endDayDay, endTimeHour, endTimeMin);
        double endDayJulian = getJulianDay(endDayYear, endDayMonth, endDayDay);

        values.put(CalendarProvider.END, cal.getTimeInMillis());
        values.put(CalendarProvider.END_DAY, endDayJulian);

        Uri uri = getContentResolver().insert(CalendarProvider.CONTENT_URI, values);

    }

    private double getJulianDay(int Y, int M, int D) {
        double A = Y/100;
        double B = A/4;
        double C = 2-A+B;
        double E = 365.25*(Y+4716);
        double F = 30.6001*(M+1);
        double JD= C+D+E+F-1524.5;
        return JD;
    }

}