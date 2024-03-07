package com.androidapp.mfc_final;



import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class privacypolicy extends AppCompatActivity {

   WebView webview;
   ProgressBar pb;
   boolean isChecked =false;
   RelativeLayout rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacypolicy);
        webview = findViewById(R.id.webview);
        rv = findViewById(R.id.rlBottom);
       pb = findViewById(R.id.pb);
        Button next;
        CheckBox checkBox;
        checkBox = findViewById(R.id.checkbox);
        next = findViewById(R.id.next);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    isChecked=true;
                    next.setAlpha(1);
                }
                else {
                    isChecked=false;
                    next.setAlpha(.5f);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isChecked)
                {
                    Toast.makeText(privacypolicy.this, "Please agree to Privacy Policy first",Toast.LENGTH_LONG).show();
                }
                else{



                    ProgressDialog progressDialog = new ProgressDialog(privacypolicy.this);
                    progressDialog.setMessage("Please wait...");

                    progressDialog.setCancelable(false);

                    progressDialog.show();

                    Splash.setPREF_isPrivacyAccepted(privacypolicy.this,"true");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {



                            Intent intent = new Intent(privacypolicy.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }, 2000);



                }


            }
        });


        webview.clearCache(true);
        webview.setVisibility(View.GONE);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("https://edze.in/PrivacyPolicy.html");
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){

            }
            @Override
            public void onPageFinished(WebView view, String url){
                pb.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
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
    }



//    Umair Qayyum 08:10 PM
//if(url.startsWith("tel:") || url.startsWith("whatsapp:") || url.startsWith("sms:") || !url.startsWith("http")) {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse(url));
//        startActivity(intent);
//        return true;
//    }


}