package com.DPAC.collabormate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.FindCallback;
import com.parse.ParseObject;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;

import com.parse.ParseException;

/**
 * Created by Chi Hoang on 5/6/2015.
 */

public class OverviewActivity extends ActionBarActivity  {

    private ProjectsDataSource datasource;
    private String projectName = "";
    private String[] userArray = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        ListView listView1 = (ListView) findViewById(R.id.list2);

        // Set up the listview
        ArrayList<String> userList = new ArrayList<String>();
        // Create and populate an ArrayList of objects from parse
        final ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);
        //final ListView userlv = (ListView)findViewById(android.R.id.list);
        listView1.setAdapter(listAdapter);
        final ParseQuery query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), objects.toString(), Toast.LENGTH_LONG)
                            .show();
                    for (int i = 0; i < objects.size(); i++) {
                        ParseUser u = (ParseUser)objects.get(i);

                        String email = u.getString("email").toString();

                        listAdapter.add(email);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }
            }
        });

        /*
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    foundUsers(objects);
                } else {
                    Toast.makeText(OverviewActivity.this, "no users", Toast.LENGTH_LONG);
                }
            }
        });*/


        //userArray[0] = ParseUser.getCurrentUser().getUsername();


        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        //        android.R.layout.simple_list_item_1, userArray);

        //listView1.setAdapter(listAdapter);
    }

    public void foundUsers(List<ParseUser> users) {
        for (int i = 0; i < 5; i++) {
            userArray[i] = users.get(i).getUsername();
        }

        if (ParseUser.getCurrentUser().getUsername().equals(userArray[0])) {
            return;
        } else {
            String temp = userArray[0];
            for (int j = 1; j < 5; j++) {
                if (ParseUser.getCurrentUser().getUsername().equals(userArray[j])) {
                    userArray[0] = userArray[j];
                    userArray[j] = temp;
                    return;
                }
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_projects, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_projects, menu);

        getMenuInflater().inflate(R.menu.menu_main, menu);

        Button cal_button = (Button) findViewById(R.id.calendar_button);
        cal_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OverviewActivity.this, CalendarActivity.class));
            }
        });

        Button mes_button = (Button) findViewById(R.id.message_button);
        mes_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OverviewActivity.this, MessagesActivity.class));
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.settings) {
            startActivity(new Intent(this, Preferences.class));

            return true;
        }

        if (id == R.id.help)    {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle(R.string.help)
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
            AlertDialog dialog = builder.create();

            dialog.show();
            return(true);
        }

        if (id == R.id.about)   {
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

            dialog.show();
            return(true);
        }

        if (id == R.id.logout)   {
            ParseUser.logOut();
            startActivity(new Intent(OverviewActivity.this, MainActivity.class));
            return(true);
        }

        return super.onOptionsItemSelected(item);
    }

    /*

    // Will be called via the onClick attribute
    // of the buttons in projects.xml
    public void deleteProject(View view) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<Project> adapter = (ArrayAdapter<Project>) getListAdapter();
        Project project = null;
        if (getListAdapter().getCount() > 0) {
            project = (Project) getListAdapter().getItem(0);
            datasource.deleteProject(project);
            adapter.remove(project);
        }
        adapter.notifyDataSetChanged();
    }

    public void newProject(View view){
        @SuppressWarnings("unchecked")
        final AlertDialog.Builder[] builder = {new AlertDialog.Builder(this)};
        builder[0].setTitle("New Project Name:");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder[0].setView(input);

        // Add new course
        builder[0].setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                projectName = input.getText().toString();
                ArrayAdapter<Project> adapter = (ArrayAdapter<Project>) getListAdapter();
                Project project = null;
                project = datasource.createProject(projectName);
                adapter.add(project);
                adapter.notifyDataSetChanged();
            }
        });
        //Cancel
        builder[0].setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder[0].show();
    }
    */


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
