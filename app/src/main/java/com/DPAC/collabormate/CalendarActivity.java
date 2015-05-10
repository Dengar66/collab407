package com.DPAC.collabormate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
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

public class CalendarActivity extends Activity implements View.OnClickListener {
    //UI References
    private EditText fromDateEtxt;
    private EditText toDateEtxt;
    private EditText task;
    private EditText desc;
    private EditText loc;
    private EditText person;

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

        task = (EditText) findViewById(R.id.task);
        task.setInputType(InputType.TYPE_CLASS_TEXT);

        desc = (EditText) findViewById(R.id.desc);
        desc.setInputType(InputType.TYPE_CLASS_TEXT);

        loc = (EditText) findViewById(R.id.loc);
        loc.setInputType(InputType.TYPE_CLASS_TEXT);

        person = (EditText) findViewById(R.id.person);
        person.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
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

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);

        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public void onClick(View view) {
        if (view == fromDateEtxt) {
            fromDatePickerDialog.show();
        } else if (view == toDateEtxt) {
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
        }

        return super.onOptionsItemSelected(item);
    }

    public void addTask(View view) {
        //Get Task data from server
        String title = task.getText().toString();
        String description = desc.getText().toString();
        String location = loc.getText().toString();
        String email = person.getText().toString();

        int startDayYear = fromDatePickerDialog.getDatePicker().getYear();
        int startDayMonth = fromDatePickerDialog.getDatePicker().getMonth();
        int startDayDay = fromDatePickerDialog.getDatePicker().getDayOfMonth();
        int startTimeHour = 0;
        int startTimeMin = 0;

        int endDayYear = toDatePickerDialog.getDatePicker().getYear();
        int endDayMonth = toDatePickerDialog.getDatePicker().getMonth();
        int endDayDay = toDatePickerDialog.getDatePicker().getDayOfMonth();
        int endTimeHour = 0;
        int endTimeMin = 0;

        long calID = 3;
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(startDayYear, startDayMonth, startDayDay, startTimeHour, startTimeHour);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(endDayYear, endDayMonth, endDayDay, endTimeHour, endTimeMin);
        endMillis = endTime.getTimeInMillis();
//Add to calendarView
        ContentValues values = new ContentValues();
        values.put(CalendarProvider.COLOR, Event.COLOR_BLUE);
        values.put(CalendarProvider.DESCRIPTION, description);
        values.put(CalendarProvider.LOCATION, location);
        values.put(CalendarProvider.EVENT, title);

        Calendar cal = Calendar.getInstance();
        double julianDay = getJulianDay(startDayYear, startDayMonth, startDayDay);
        double endDayJulian = getJulianDay(endDayYear, endDayMonth, endDayDay);

        cal.set(startDayYear, startDayMonth, startDayDay, startTimeHour, startTimeMin);
        values.put(CalendarProvider.START, cal.getTimeInMillis());
        values.put(CalendarProvider.START_DAY, julianDay);
        TimeZone tz = TimeZone.getDefault();

        cal.set(endDayYear, endDayMonth, endDayDay, endTimeHour, endTimeMin);

        values.put(CalendarProvider.END, cal.getTimeInMillis());
        values.put(CalendarProvider.END_DAY, endDayJulian);

        Uri uri = getContentResolver().insert(CalendarProvider.CONTENT_URI, values);

// get the event ID that is the last element in the Uri
        long eventID = Long.parseLong(uri.getLastPathSegment());
//
// ... do something with event ID
//
//

        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
                .putExtra(Events.TITLE, title)
                .putExtra(Events.DESCRIPTION, description)
                .putExtra(Events.EVENT_LOCATION, location)
                .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY)
                .putExtra(Intent.EXTRA_EMAIL, email);

        startActivity(intent);
    }

    private double getJulianDay(int Y, int M, int D) {
        double A = Y / 100;
        double B = A / 4;
        double C = 2 - A + B;
        double E = 365.25 * (Y + 4716);
        double F = 30.6001 * (M + 1);
        double JD = C + D + E + F - 1524.5;
        return JD;
    }

    public void viewCalendar(View view) {

    }

}