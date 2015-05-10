package com.DPAC.collabormate;

import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.*;


public class MainActivity extends ActionBarActivity {
    Button btn_Login = null;
    Button btn_Signup = null;
    Button btn_Forget = null;
    private EditText mUserNameEditText;
    private EditText mPasswordEditText;

    Boolean isInternetPresent = false;
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.initialize(this, "9Zwa3ybvYWhz5MLHXyuhofJRVUVgU848ln5aUeyW",
                "HMz4vwC3GmyyZPzsFuh5eBWVUkhS5m0OJpkKilKb");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        cd = new ConnectionDetector(getApplicationContext());

        btn_Login = (Button) findViewById(R.id.btn_login);
        btn_Signup = (Button) findViewById(R.id.btn_signup);
        btn_Forget = (Button) findViewById(R.id.btn_forget);
        mUserNameEditText = (EditText) findViewById(R.id.usernameEdit);
        mPasswordEditText = (EditText) findViewById(R.id.passwordEdit);

        btn_Login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                isInternetPresent = cd.isConnectingToInternet();

                if (isInternetPresent) {

                    attemptLogin();

                } else {

                    showAlertDialog(MainActivity.this, "No Internet Connection", "You don't have" +
                            " internet connection.", false);
                }

            }
        });

        btn_Signup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(in);
            }
        });

        btn_Forget.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, ForgetPassActivity.class);
                startActivity(in);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return(super.onCreateOptionsMenu(menu));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch (item.getItemId()) {
            case R.id.about:
                // Use the Builder class for convenient dialog construction
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
                AlertDialog dialogAbout = builder.create();

                dialogAbout.show();
                return(true);

            case R.id.help:
                // Use the Builder class for convenient dialog construction
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
                AlertDialog dialogHelp = builder.create();

                dialogHelp.show();
                return(true);

            case R.id.settings:
                startActivity(new Intent(MainActivity.this, Preferences.class));
                return(true);

            case R.id.logout:
                ParseUser.logOut();
                return(true);

        }

        return super.onOptionsItemSelected(item);
    }

    public void attemptLogin() {
        clearErrors();

        String username = mUserNameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            mPasswordEditText.setError(getString(R.string.error_field_required));
            focusView = mPasswordEditText;
            cancel = true;
        }

        if (TextUtils.isEmpty(username)) {
            mUserNameEditText.setError(getString(R.string.error_field_required));
            focusView = mUserNameEditText;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            login(username.toLowerCase(Locale.getDefault()), password);
        }
    }

    public void login(String lower, String pass) {
        ParseUser.logInInBackground(lower, pass, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null)
                    loginSuccessful();
                else
                    loginUnsuccessful();
            }
        });
    }

    protected void loginSuccessful() {
        Intent in = new Intent(MainActivity.this, ProjectsActivity.class);
        startActivity(in);
    }

    protected void loginUnsuccessful() {
        Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
        showAlertDialog(this, "Login", "Username or Password is invalid.", false);
    }

    private void clearErrors() {
        mUserNameEditText.setError(null);
        mPasswordEditText.setError(null);
    }

    @SuppressWarnings("deprecation")
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alert = new AlertDialog.Builder(context).create();

        //Title
        alert.setTitle(title);

        //Message
        alert.setMessage(message);

        //Icon?
        alert.setIcon(R.drawable.fail);

        //OK button
        alert.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //Show
        alert.show();
    }
}