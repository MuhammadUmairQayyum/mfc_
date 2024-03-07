package com.androidapp.mfc_final;



import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

public class Splash extends AppCompatActivity {

    ImageView imv1,imv2,imv3,imv4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
  String loggedIn  = getPREF_isLoggedIn(Splash.this);
  String accepted= getPREF_isPrivacyAccepted(Splash.this);

        Animation slideInLeftAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        Animation slideInRightAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        Animation slideInTopAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);
        Animation slideInBottomAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom);

        // Assign animations to TextViews
        TextView textViewM = findViewById(R.id.textViewM);
        TextView textViewF = findViewById(R.id.textViewF);
        TextView textViewC = findViewById(R.id.textViewC);
        TextView textViewtitle = findViewById(R.id.bottomHeading);

        imv1 = findViewById(R.id.imv1);imv2 = findViewById(R.id.imv2);imv3 = findViewById(R.id.imv3);imv4 = findViewById(R.id.imv4);



        textViewM.startAnimation(slideInLeftAnimation);
        textViewF.startAnimation(slideInRightAnimation);
        textViewC.startAnimation(slideInTopAnimation);
        textViewtitle.startAnimation(slideInBottomAnimation);
        star = findViewById(R.id.star);






        ObjectAnimator animator = ObjectAnimator.ofFloat(imv1, "alpha", 0f, 1f);
        animator.setDuration(800);
        animator.setRepeatMode(ObjectAnimator.REVERSE);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.start();
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(imv2, "alpha", 0f, 1f);
        animator2.setDuration(800);
        animator2.setRepeatMode(ObjectAnimator.REVERSE);
        animator2.setRepeatCount(ObjectAnimator.INFINITE);
        animator2.start();
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(imv3, "alpha", 0f, 1f);
        animator3.setDuration(800);
        animator3.setRepeatMode(ObjectAnimator.REVERSE);
        animator3.setRepeatCount(ObjectAnimator.INFINITE);
        animator3.start();
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(imv4, "alpha", 0f, 1f);
        animator4.setDuration(800);
        animator4.setRepeatMode(ObjectAnimator.REVERSE);
        animator4.setRepeatCount(ObjectAnimator.INFINITE);
        animator4.start();


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms

                imv1.setVisibility(View.VISIBLE);
                // imv2.setVisibility(View.VISIBLE);
               // imv3.setVisibility(View.VISIBLE);
                imv4.setVisibility(View.VISIBLE);

            }
        }, 2000);




          if(accepted.contains("true"))
  {
      final Handler handlers = new Handler();
      handler.postDelayed(new Runnable() {
          @Override
          public void run() {

              Intent intent = new Intent(Splash.this, MainActivity.class);
              startActivity(intent);
              finish();
          }
      }, 3000);


    }
  else{

      if(loggedIn.contains("true"))
      {
          Intent intent = new Intent(Splash.this, privacypolicy.class);
          startActivity(intent);
          finish();
      }
      else{
          final Handler handler1 = new Handler();
          handler.postDelayed(new Runnable() {
              @Override
              public void run() {

                  Intent intent = new Intent(Splash.this,Login.class);
                  startActivity(intent);
                  finish();
              }
          }, 3000);
      }

  }

    }


    static SharedPreferences getSharedPreferences(Context ctx) {
        if (ctx != null) {
            return PreferenceManager.getDefaultSharedPreferences(ctx);
        } else
            return null;
    }
    static final String PREF_isLoggedIn = "isLoggedIn";
    public static String getPREF_isLoggedIn (Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_isLoggedIn, "");
    }

    public static void setPREF_isLoggedIn(Context ctx, String userName) {
        if (ctx != null) {
            if (userName != null) {
                SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
                editor.putString(PREF_isLoggedIn, userName);
                editor.apply();
            }
        }
    }

    static final String PREF_isPrivacyAccepted = "isPrivacyAccepted";
    public static String getPREF_isPrivacyAccepted (Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_isPrivacyAccepted, "");
    }

    public static void setPREF_isPrivacyAccepted(Context ctx, String userName) {
        if (ctx != null) {
            if (userName != null) {
                SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
                editor.putString(PREF_isPrivacyAccepted, userName);
                editor.apply();
            }
        }
    }

    ImageView star;
    private void shower() {
        // Create a new star view in a random X position above the container.
        // Make it rotate about its center as it falls to the bottom.

        // Local variables
        ViewGroup container = (ViewGroup) star.getParent();
        int containerW = container.getWidth();
        int containerH = container.getHeight();
        float starW = star.getWidth();
        float starH = star.getHeight();

        // Create the new star (an ImageView in layout holding drawable star image)
        // and add it to the container
        AppCompatImageView newStar = new AppCompatImageView(this);
        newStar.setImageResource(R.drawable.ic_star);
        newStar.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        ));
        container.addView(newStar);

        // Scale the view randomly between 10-160% of its default size
        float scaleX = (float) (Math.random() * 1.5f + 0.1f);
        newStar.setScaleX(scaleX);
        newStar.setScaleY(scaleX);
        starW *= scaleX;
        starH *= scaleX;

        // Position the view at a random place between
        // the left and right edges of the container
        float translationX = (float) (Math.random() * containerW - starW / 2);
        newStar.setTranslationX(translationX);

        // Create an animator that moves the view from a starting position right above the container
        // to an ending position right below the container. Set an accelerate interpolator to give
        // it a gravity/falling feel
        ObjectAnimator mover = ObjectAnimator.ofFloat(newStar, View.TRANSLATION_Y, -starH, containerH + starH);
        mover.setInterpolator(new AccelerateInterpolator(1f));

        // Create an animator to rotate the
        // view around its center up to three times
        ObjectAnimator rotator = ObjectAnimator.ofFloat(
                newStar, View.ROTATION,
                (float) (Math.random() * 1080)
        );
        rotator.setInterpolator(new LinearInterpolator());

        // Use an AnimatorSet to play the falling and
        // rotating animators in parallel for a duration
        // of a half-second to two seconds
        AnimatorSet set = new AnimatorSet();
        set.playTogether(mover, rotator);
        set.setDuration((long) (Math.random() * 1500 + 500));

        // When the animation is done, remove the
        // created view from the container
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                container.removeView(newStar);
            }
        });

        // Start the animation
        set.start();
    }

    private void disableViewDuringAnimation(ObjectAnimator animator, final View view) {

        // This method listens for start/end
        // events on an animation and disables
        // the given view for the entirety of that animation.
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setEnabled(true);
            }
        });
    }
}