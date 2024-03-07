package com.androidapp.mfc_final;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;

import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.PhoneAuthProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Button logout;
    WebView webview;
    ProgressBar pb;
    TextView call;
    ScrollView srv;
    ImageButton more;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logout= findViewById(R.id.logout);
         webview = findViewById(R.id.webview);
         call = findViewById(R.id.call);

         pb = findViewById(R.id.pb);
         srv = findViewById(R.id.srv);
         more=  findViewById(R.id.more);
         more.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 {
                     PopupMenu popupMenu = new PopupMenu(MainActivity.this, more);
                     popupMenu.inflate(R.menu.main_menu);

                     popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                         @Override
                         public boolean onMenuItemClick(MenuItem item) {
                             switch (item.getItemId()) {
//                                 case R.id.logout:
//                                     //handle menu1 click
//
//                                  logout.performClick();
//                                  return true;


                                 case R.id.clogs:


                                     if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                                         if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) MainActivity.this, Manifest.permission.READ_CALL_LOG)) {
                                             AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                                             alertBuilder.setCancelable(true);
                                             alertBuilder.setMessage("You need to allow read call logs permission first");
                                             alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                 @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                                                 public void onClick(DialogInterface dialog, int which) {
                                                     ActivityCompat.requestPermissions((Activity)MainActivity.this, new String[]{Manifest.permission.READ_CALL_LOG}, 11);
                                                 }
                                             });
                                         } else {
                                             ActivityCompat.requestPermissions((Activity)MainActivity.this, new String[]{Manifest.permission.READ_CALL_LOG}, 11);
                                         }
                                     }
                                     else{


                                     }



                                     return true;

                                 case  R.id.home:

                                 {

                                     call.setVisibility(View.GONE);

                                     srv.setVisibility(View.GONE);

                                     return true;
                                 }
                                 default:
                                     return false;
                             }


                         }
                     });


                     popupMenu.show();


                 }


             }
         });



         webview.clearCache(true);
         webview.setVisibility(View.GONE);

         webview.getSettings().setJavaScriptEnabled(true);
         webview.loadUrl("https://vrrconstructions.edze.in");
         webview.setWebViewClient(new WebViewClient(){
             @Override
             public void onPageStarted(WebView view, String url, Bitmap favicon){

             }
             @Override
             public void onPageFinished(WebView view, String url){
                 pb.setVisibility(View.GONE);


                 webview.setVisibility(View.VISIBLE);

             }


             @Override
             public boolean shouldOverrideUrlLoading( WebView view, String url) {
                 if((url.startsWith("tel:") || url.startsWith("whatsapp:") || url.startsWith("sms:") || !url.startsWith("http"))){
                     Intent intent = new Intent(Intent.ACTION_VIEW);
                     intent.setData(Uri.parse(url));
                     startActivity(intent);
                     return true;
                 }
                 return false;
             }
         });


         webview.getSettings().setLoadWithOverviewMode(true);
         webview.getSettings().setUseWideViewPort(true);
         webview.getSettings().setLoadWithOverviewMode(true);
         webview.getSettings().setUseWideViewPort(true);
         webview.getSettings().setAllowFileAccess(true);
         webview.getSettings().setAllowContentAccess(true);
         webview.getSettings().setAllowFileAccessFromFileURLs(true);
         webview.getSettings().setAllowUniversalAccessFromFileURLs(true);
         webview.getSettings().setDomStorageEnabled(true);
         webview.getSettings().setUserAgentString("Android WebView");

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Splash.setPREF_isLoggedIn(MainActivity.this,"false");
                Splash.setPREF_isPrivacyAccepted(MainActivity.this,"false");
                ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Please wait...");
                progressDialog.setTitle("Loging out");
                progressDialog.setCancelable(false);

                progressDialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(MainActivity.this, Splash.class);
                        startActivity(intent);
                        finish();

                    }
                }, 3000);


            }
        });

    }
    ProgressDialog progressDialog;
    private void fetchCallLogs() {


            int flag=1;
            call.setText(Html.fromHtml("<b>Call Logs</b>"));
            call.setText(Html.fromHtml(""));
            StringBuilder callLogs = new StringBuilder();

            ArrayList<String> calllogsBuffer = new ArrayList<String>();
            calllogsBuffer.clear();
            Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI,
                    null, null, null, null);
            int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
            int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
            int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
            int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
          //  int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);

            while (managedCursor.moveToNext()) {


                try {
                    String phNumber = managedCursor.getString(number);
                    String callType = managedCursor.getString(type);
                    String callDate = managedCursor.getString(date);


                    Date callDayTime = new Date(Long.valueOf(callDate));
                    String callDuration = managedCursor.getString(duration);
                    String dir = null;
                    int dircode = Integer.parseInt(callType);
                    switch (dircode) {
                        case CallLog.Calls.OUTGOING_TYPE:
                            dir = "OUTGOING";
                            break;
                        case CallLog.Calls.INCOMING_TYPE:
                            dir = "INCOMING";
                            break;
                        case CallLog.Calls.MISSED_TYPE:
                            dir = "MISSED";
                            break;
                    }

                    calllogsBuffer.add(" \nPhone Number: " + phNumber + " \nCall Type: "
                            + dir + " \nCall Date: " + callDayTime
                            + " \nCall duration in sec : " + callDuration + "\n");

                }
                catch (Exception io)
                {

                    io.printStackTrace();
                }

            }
            managedCursor.close();



           call.setText(calllogsBuffer.toString());

           progressDialog.dismiss();


    }
}