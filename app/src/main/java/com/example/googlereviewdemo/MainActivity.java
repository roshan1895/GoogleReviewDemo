package com.example.googlereviewdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;

public class MainActivity extends AppCompatActivity {
    private ReviewManager reviewManager;
    Button button;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
    private void init() {
        reviewManager = ReviewManagerFactory.create(this);
        button=findViewById(R.id.rate_app_btn);
               button .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRateApp();
            }
        });
    }


    public void showRateApp() {
        Task<ReviewInfo> request = reviewManager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ReviewInfo reviewInfo = task.getResult();

                Task<Void> ftask = reviewManager.launchReviewFlow(this, reviewInfo);
                ftask.addOnCompleteListener(task1 -> {

                });
            } else {
                // There was some problem, continue regardless of the result.
                // show native rate app dialog on error
                showRateAppFallbackDialog();
            }
        });
    }

    /**
     * Redirect user to playstore to review the app
     */
    private void showRateAppFallbackDialog()
    {      MaterialAlertDialogBuilder materialAlertDialogBuilder=new MaterialAlertDialogBuilder(this)
            .setTitle(R.string.rate_app_title)
            .setMessage(R.string.rate_app_message)
            .setPositiveButton(R.string.rate_btn_positive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    OpenPlayStoreReview();
                }
            })
            .setNegativeButton(R.string.rate_btn_negative, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                   dialog.dismiss();
                }
            })
            .setNeutralButton(R.string.rate_btn_nuetral, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                  dialog.dismiss();
                }
            });
        dialog=materialAlertDialogBuilder.create();
        dialog.show();
    }
    public void OpenPlayStoreReview() {
        final String appPackageName = getPackageName();
        Log.e("app_package_name",appPackageName+"");
//        try {
////            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
//        } catch (ActivityNotFoundException exception) {
////            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
//        }
    }
}