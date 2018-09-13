package br.com.getnet.payment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Locale;

import br.com.getnet.payment.component.DatePickerView;
import br.com.getnet.payment.component.EditText;
import br.com.getnet.payment.component.Spinner;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private final String DEEP_PAYMENT = "getnet://pagamento/v1/payment";
    private final String DEEP_PRE_AUTH = "getnet://pagamento/v1/pre-authorization";
    private final String DEEP_REFUND = "getnet://pagamento/v1/refund";
    private final String DEEP_INFO = "getnet://pagamento/v1/getinfos";
    private final String DEEP_REPRINT = "getnet://pagamento/v1/reprint";

    private final String ARG_AMOUNT = "amount";
    private final String ARG_CURRENT_POSITION = "currencyPosition";
    private final String ARG_CURRENCY_CODE = "currencyCode";
    private final String ARG_PAYMENT_TYPE = "paymentType";
    private final String ARG_CREDIT_TYPE = "creditType";
    private final String ARG_INSTALLMENTS = "installments";
    public static final String ARG_TRANSACTION_DATE = "transactionDate";
    public static final String ARG_CV_NUMBER = "cvNumber";
    public static final String ARG_ORIGIN_TERMINAL = "originTerminal";

    private final String ARG_RESULT = "result";
    private final String ARG_RESULT_DETAILS = "resultDetails";
    private final String ARG_TYPE = "type";
    private final String ARG_INPUT_TYPE = "inputType";
    private final String ARG_NSU = "nsu";
    private final String ARG_BRAND = "brand";
    private final String ARG_EC = "ec";
    private final String ARG_NUM_SERIE = "numserie";
    private final String ARG_NUM_LOGIC = "numlogic";
    private final String ARG_VERSION = "version";
    private final String ARG_CNPJ = "cnpjEC";
    private final String ARG_NAME = "nomeEC";

    public static final String DATA_PAYMENT_CREDIT = "credit";
    public static final String DATA_PAYMENT_DEBIT = "debit";
    public static final String DATA_PAYMENT_VOUCHER = "voucher";

    public static final String DATA_CREDIT_MERCHANT = "creditMerchant";
    public static final String DATA_CREDIT_ISSUER = "creditIssuer";

    private final int REQUEST_CODE = 10033;

    @BindView(R.id.console_output_scroll)
    protected NestedScrollView outputScroll;

    @BindView(R.id.console_output)
    protected TextView output;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.payment)
    public void onPayment() {

        showFormDialog(R.layout.payment_dialog, dialog -> {
            EditText amount = dialog.findViewById(R.id.payment_amount);
            Spinner paymentType = dialog.findViewById(R.id.payment_paymentType);
            EditText installments = dialog.findViewById(R.id.payment_installments);

            Bundle bundle = new Bundle();
            addExtraPaymentType(bundle, paymentType.getSelectedItem());
            addExtraIfNotNull(bundle, ARG_AMOUNT, formatAmount(amount.getText()));
            addExtraIfNotNull(bundle, ARG_INSTALLMENTS, removeNonNumber(installments.getText()));
            bundle.putString(ARG_CURRENT_POSITION, "CURRENCY_AFTER_AMOUNT");
            bundle.putString(ARG_CURRENCY_CODE, "986");
            startIntent(bundle, DEEP_PAYMENT);
        });
    }

    public void addExtraPaymentType(Bundle bundle, String paymentTypeSelected) {
        if (getString(R.string.debit).equalsIgnoreCase(paymentTypeSelected)) {
            bundle.putString(ARG_PAYMENT_TYPE, DATA_PAYMENT_DEBIT);
        } else if (getString(R.string.voucher).equalsIgnoreCase(paymentTypeSelected)) {
            bundle.putString(ARG_PAYMENT_TYPE, DATA_PAYMENT_VOUCHER);
        } else if (getString(R.string.credit_sight).equalsIgnoreCase(paymentTypeSelected)) {
            bundle.putString(ARG_PAYMENT_TYPE, DATA_PAYMENT_CREDIT);
        } else if (getString(R.string.credit_store_financial).equalsIgnoreCase(paymentTypeSelected)) {
            bundle.putString(ARG_PAYMENT_TYPE, DATA_PAYMENT_CREDIT);
            bundle.putString(ARG_CREDIT_TYPE, DATA_CREDIT_MERCHANT);
        } else if (getString(R.string.credit_emitter_financial).equalsIgnoreCase(paymentTypeSelected)) {
            bundle.putString(ARG_PAYMENT_TYPE, DATA_PAYMENT_CREDIT);
            bundle.putString(ARG_CREDIT_TYPE, DATA_CREDIT_ISSUER);
        } else {
            bundle.remove(ARG_PAYMENT_TYPE);
            bundle.remove(ARG_CREDIT_TYPE);
        }
    }

    @OnClick(R.id.pre_auth)
    public void onPreAuth() {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_AMOUNT, "000000001200");
        bundle.putString(ARG_CURRENT_POSITION, "CURRENCY_BEFORE_AMOUNT");
        bundle.putString(ARG_CURRENCY_CODE, "986");
        startIntent(bundle, DEEP_PRE_AUTH);
    }

    @OnClick(R.id.refund)
    public void onRefund() {
        showFormDialog(R.layout.refund_dialog, dialog -> {
            EditText amount = dialog.findViewById(R.id.refund_amount);
            DatePickerView transactionDate = dialog.findViewById(R.id.refund_transactionDate);
            EditText cvNumber = dialog.findViewById(R.id.refund_cvNumber);
            EditText originTerminal = dialog.findViewById(R.id.refund_originTerminal);

            Bundle bundle = new Bundle();
            addExtraIfNotNull(bundle, ARG_AMOUNT, formatAmount(amount.getText()));
            addExtraIfNotNull(bundle, ARG_TRANSACTION_DATE, transactionDate.getText());
            addExtraIfNotNull(bundle, ARG_CV_NUMBER, cvNumber.getText());
            addExtraIfNotNull(bundle, ARG_ORIGIN_TERMINAL, originTerminal.getText());
            startIntent(bundle, DEEP_REFUND);
        });
    }

    @OnClick(R.id.info)
    public void onInfo() {
        Bundle bundle = new Bundle();
        startIntent(bundle, DEEP_INFO);
    }

    @OnClick(R.id.reprint)
    public void onReprint() {
        Bundle bundle = new Bundle();
        startIntent(bundle, DEEP_REPRINT);
    }

    private void startIntent(Bundle bundle, String deeplink) {
        logInfo("#####################################");

        logSpecial(String.format("START: URL [%s] ......", deeplink));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(deeplink));
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_CODE);

        logInfo("PARAMETERS ......");
        logInfo(bundle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            if ("0".equals(data.getStringExtra("result"))) {
                logResult("RESPONSE: ......");
                logResult(data.getExtras());
            } else {
                logError("RESPONSE: [PROBLEM] ......");
                logError(data.getExtras());
            }
        } else {
            logError("RESPONSE: [ANDROID ERROR] ......");
            if (data != null) {
                logError(data.getExtras());
            } else {
                logError("NO RESPONSE PARAMETERS");
            }
        }
        logInfo("#####################################");
    }

    private void logSpecial(String text) {
        printMessage(text, Color.parseColor("#1E90FF"));
    }

    private void logInfo(String text) {
        printMessage(text, Color.parseColor("#FFFFFF"));
    }

    private void logInfo(Bundle bundle) {

        for (String key : bundle.keySet()) {
            logInfo(String.format("%s = %s", key, bundle.get(key)));
        }
    }

    private void logError(String text) {
        printMessage(text, Color.parseColor("#8B0000"));
    }

    private void logError(Bundle bundle) {
        if (bundle != null) {
            for (String key : bundle.keySet()) {
                logError(String.format("%s = %s", key, bundle.get(key)));
            }
        }
    }

    private void logResult(String text) {
        printMessage(text, Color.parseColor("#006400"));
    }

    private void logResult(Bundle bundle) {
        for (String key : bundle.keySet()) {
            logResult(String.format("%s = %s", key, bundle.get(key)));
        }
    }

    private void printMessage(String text, int color) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(output.getText());
        final SpannableString spannableString = new SpannableString(text);
        try {
            spannableString.setSpan(new ForegroundColorSpan(color), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception ignore) {
        }
        spannableStringBuilder.append(spannableString);
        output.setText(spannableStringBuilder.append("\n"), TextView.BufferType.SPANNABLE);
        outputScroll.fullScroll(View.FOCUS_DOWN);
    }

    private String formatAmount(String amount) {
        String cleanAmount = removeNonNumber(amount);
        if (cleanAmount.isEmpty()) {
            return cleanAmount;
        } else {
            return String.format(Locale.getDefault(), "%012d", Long.valueOf(cleanAmount));
        }
    }

    private String removeNonNumber(String value) {
        return value.replaceAll("[^0-9]", "");
    }

    private void showFormDialog(@LayoutRes int layout, Callback callback) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.findViewById(R.id.button_send).setOnClickListener(v -> callback.onSubmit(dialog));

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    private void addExtraIfNotNull(Bundle bundle, String key, String value) {
        if (!TextUtils.isEmpty(value)) {
            bundle.putString(key, value);
        }
    }


    interface Callback {

        void onSubmit(Dialog dialog);

    }

}
