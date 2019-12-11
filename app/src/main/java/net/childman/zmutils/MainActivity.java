package net.childman.zmutils;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import net.childman.libmvvm.activity.BaseActivity;
import net.childman.libmvvm.dialog.ProgressDialog;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog();
            }
        });
        findViewById(R.id.btn_test1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(progressDialog != null){
                    progressDialog.getProgressBar().setProgress(progressDialog.getProgressBar().getProgress()+5);
                }
            }
        });
    }

    private void showProgressDialog() {
        if(progressDialog == null) {
            progressDialog = new ProgressDialog.Builder(this)
                    .setMessage("Loading")
                    .setCancelable(true)
                    .setCancelOutside(false)
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            Log.d(TAG, "onCancel: ");
                        }
                    })
                    .create();
        }
        progressDialog.show();
    }
}
