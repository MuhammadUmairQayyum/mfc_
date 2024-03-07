package com.androidapp.mfc_final;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;




public class Login extends AppCompatActivity {

    EditText etNumber, etCode;

    CountryCodePicker ccp;
    TextView tvCodeSentTo, tvCountdown, tv_did_not, btnEditNumber, or;
    private LinearLayout countryCodePicker;
    private RelativeLayout linearCode;
    Button btnGetPin, btnSubmit, btnResendCode;
    ProgressBar progressbar;

    View view;
    ProgressDialog progressDialog;
    String country_code = "+92", numberOnly = "", entered_number = "";
    private static final int PERMISSION_REQUEST_CODE = 200;

    private String mVerificationId;
    final int min = 111111;
    final int max = 999999;
    private static int random;
    boolean isFromDigiKhtaa = false;
    CountDownTimer countDownTimer;
    GoogleApiClient apiClient;
    private FirebaseAuth mAuth;
    boolean bTestUser = false;

    private final String test_number = "+913501234567";
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private boolean resend = false, bResendbyCall = false;
    private String lan = "";


    Button btnTestLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        etNumber = findViewById(R.id.etNumber);
        countryCodePicker = findViewById(R.id.linearCountry);
        ImageButton btnBack = findViewById(R.id.ibBack);
        btnGetPin = findViewById(R.id.btnGetPin);

        mAuth = FirebaseAuth.getInstance();
        etCode = findViewById(R.id.etCode);

        ccp = findViewById(R.id.ccp);
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {

                country_code =  selectedCountry.getPhoneCode();
            }
        });




        tvCodeSentTo = findViewById(R.id.tvCodeSentTo);
        tvCountdown = findViewById(R.id.tvCountdown);
        btnEditNumber = findViewById(R.id.btnEditNumber);
        progressbar = findViewById(R.id.progressbar);
        btnSubmit = findViewById(R.id.btnSubmit);
        tv_did_not = findViewById(R.id.tv_did_not);
        btnResendCode = findViewById(R.id.btnResendCode);


        view = findViewById(R.id.view);
        linearCode = findViewById(R.id.linearCode);

        or = findViewById(R.id.or);



        btnGetPin.getBackground().setAlpha(50);
        btnSubmit.getBackground().setAlpha(50);

        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setCancelable(false);



        etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {



            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() >= 7) {

                    btnGetPin.setEnabled(true);
                    btnGetPin.getBackground().setAlpha(255);



                } else {

                    btnGetPin.setEnabled(false);
                    btnGetPin.getBackground().setAlpha(50);




                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6) {
                    btnSubmit.setEnabled(true);
                    btnSubmit.getBackground().setAlpha(255);


                } else {
                    btnSubmit.setEnabled(false);
                    btnSubmit.getBackground().setAlpha(50);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });



        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        btnGetPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                random = 0;
                tv_did_not.setVisibility(View.GONE);
                btnResendCode.setVisibility(View.GONE);

                etCode.setText("");
                boolean connected = internet_connection();

                if (connected) {


                    entered_number = etNumber.getText().toString().trim();
                    numberOnly = etNumber.getText().toString().trim();

                    if (entered_number.charAt(0) == '0') {
                        entered_number = entered_number.substring(1);
                        numberOnly = numberOnly.substring(1);
                    }
                    String operator = entered_number.substring(0, 2);

                 //   country_code = "+91";

                    entered_number = country_code + entered_number;

                    SendByFireBase();
//                    if (country_code.equals("+91") /*&& !operator.equals("33")*/) {
//                        if (entered_number.equals(test_number)) {
//                            bTestUser = true;
//                            Toast.makeText(Login.this, "Enter test OTP", Toast.LENGTH_SHORT).show();
//                            startTimer();
//                        } else {
//
//                        }
//                    }

                } else {


                    Toast.makeText(Login.this, "No internet connection found!\nPlease connect to internet.", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String code = etCode.getText().toString().trim();

                if (!code.isEmpty()) {

                    boolean connected = internet_connection();

                    if (connected) {

                        if (bTestUser) {
                            String test_code = "112233";
                            if (code.equals(test_code)) {
                                progressDialog.setMessage("Please wait...");
                                progressDialog.show();
                                checkNumber(entered_number);
                            } else {
                                Toast.makeText(Login.this, "Wrong Otp", Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            verifyFirebaseCode(code);

                        }
                    } else {
                        Toast.makeText(Login.this, "No Internet Connection  ", Toast.LENGTH_LONG).show();
                    }
                } else {

                    etCode.setError("Please enter code");
                }
            }
        });


        btnEditNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                random = 0;
                entered_number = "";
                country_code = "";
                numberOnly = "";
                bTestUser = false;
                bResendbyCall = false;
                resend = false;
                linearCode.setVisibility(View.INVISIBLE);
                countryCodePicker.setVisibility(View.VISIBLE);
                etNumber.setVisibility(View.VISIBLE);
                btnGetPin.setVisibility(View.VISIBLE);

                etCode.setVisibility(View.INVISIBLE);
                tvCountdown.setVisibility(View.INVISIBLE);
                progressbar.setVisibility(View.INVISIBLE);
                btnSubmit.setVisibility(View.INVISIBLE);


                tvCountdown.setVisibility(View.INVISIBLE);
                btnResendCode.setVisibility(View.INVISIBLE);

                etCode.clearFocus();
                etNumber.requestFocus();
            }
        });

        btnResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                random = 0;
                tv_did_not.setVisibility(View.GONE);
                btnResendCode.setVisibility(View.GONE);

                etCode.setText("");
                boolean connected = internet_connection();
                if (connected) {
                    entered_number = etNumber.getText().toString().trim();
                    if (entered_number.charAt(0) == '0') {
                        entered_number = entered_number.substring(1);
                    }

                    entered_number = "+"+country_code + entered_number;

                    resend = true;
                    if (entered_number.equals(test_number)) {
                        Toast.makeText(Login.this, "Enter test OTP", Toast.LENGTH_SHORT).show();
                        bTestUser = true;
                        startTimer();
                    } else {
                        SendByFireBase();

                    }


                } else {
                    Toast.makeText(Login.this, "No internet connection found!\nPlease connect to internet.", Toast.LENGTH_LONG).show();
                }
            }
        });





        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String
                                           verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {

                mVerificationId = verificationId;

                try {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Toast.makeText(Login.this, "OTP sent.", Toast.LENGTH_LONG).show();

                startTimer();
            }



            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                String code = phoneAuthCredential.getSmsCode();

                if (code != null) {
                    etCode.setText("");
                    etCode.setText(code);
                    verifyFirebaseCode(code);
                } else {



                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }

                try {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (resend) {
                    resend = false;
                    btnResendCode.setVisibility(View.VISIBLE);

//                    tv_did_not.setVisibility(View.VISIBLE);
                }

                Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        };

        etNumber.requestFocus();






    }

    public boolean internet_connection() {

        boolean response = false;
        try {

            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                response = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
            }

        } catch (Exception io) {
            io.printStackTrace();
            response = false;
        }

        return response;
    }


    private void verifyFirebaseCode(final String code) {

        try {
            if (!progressDialog.isShowing()) {
                progressDialog.setMessage("Please wait");
                progressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                signInWithPhoneAuthCredential(credential);
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {


        finish();
    }

    private void startTimer() {
        String s = country_code + "-" + numberOnly;

        tvCodeSentTo.setText(s);

        linearCode.setVisibility(View.VISIBLE);
        etCode.setVisibility(View.VISIBLE);
        tvCountdown.setVisibility(View.VISIBLE);

        btnSubmit.setVisibility(View.VISIBLE);



//        etCode.requestFocus();

        countryCodePicker.setVisibility(View.GONE);
        etNumber.setVisibility(View.GONE);
        btnGetPin.setVisibility(View.GONE);

        or.setVisibility(View.GONE);

        countDownTimer = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {

//                tvCountdown.setText(" " + getString(R.string.ResendCdeAfter) + " ");

                if (lan.equals("ur"))
                {

                    tvCountdown.setText(millisUntilFinished / 1000 + " سیکنڈز میں کوڈ دوبارہ بھیجیں ");
                }
                else if (lan.equals("as"))
                {
                    tvCountdown.setText(millisUntilFinished / 1000 + " seconds mein code dobara bhejein");
                } else {
                    tvCountdown.setText("Resend code in " + millisUntilFinished / 1000 + " seconds");
                }
            }

            public void onFinish() {

                tvCountdown.setVisibility(View.GONE);
                progressbar.setVisibility(View.GONE);
                btnResendCode.setVisibility(View.VISIBLE);
                String country_code = "+91";

//                tv_did_not.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    private void SendByFireBase() {
        progressDialog.setMessage("Sending code");
        progressDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        entered_number,
                        60,
                        TimeUnit.SECONDS,
                        Login.this,
                        mCallbacks);
            }
        }, 1000);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            if (progressDialog.isShowing()) {
                                   progressDialog.dismiss();




                                   checkNumber(entered_number);
                            }

                        } else {

                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }



                            Toast.makeText(Login.this, "Wrong Otp please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    public boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //   Log.v(TAG,"Permission is granted2");
                return true;
            } else {

                // Log.v(TAG,"Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            // Log.v(TAG,"Permission is granted2");
            return true;
        }
    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }



    public void checkNumber(String Number) {
        //progressDialog.show();

        Splash.setPREF_isLoggedIn(Login.this,"true");


        Intent intent = new Intent(Login.this,privacypolicy.class);
        startActivity(intent);


    }
    String android_id=" ";


    boolean result;






}
