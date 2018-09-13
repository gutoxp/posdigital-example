package br.com.getnet.payment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

public class ResponseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Parametros Recebidos");
        builder.setMessage(parseParameters());
        builder.setPositiveButton("OK", null);
        builder.setOnDismissListener(dialog -> finish());
        builder.show();
    }

    private String parseParameters() {
        StringBuilder builder = new StringBuilder("Parametros Recebidos").append("\n");
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            for (String key : bundle.keySet()) {
                builder.append(String.format("%s = %s", key, bundle.get(key))).append("\n");
            }
        }
        return builder.toString();
    }

}
