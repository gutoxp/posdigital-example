package br.com.getnet.payment.component;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import br.com.getnet.payment.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DatePickerView extends FrameLayout {

    @BindView(R.id.date_picker_view_textInput)
    protected TextInputLayout textInput;

    @BindView(R.id.date_picker_view_editText)
    protected AppCompatEditText editText;

    public DatePickerView(Context context) {
        super(context);
        init(null);
    }

    public DatePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DatePickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.date_picker_view, this);
        ButterKnife.bind(this, view);

        editText.addTextChangedListener(Mask.insert(Mask.DATA_DDMMYY, editText));
        setText(Calendar.getInstance());
        if (attrs != null) {
            TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.DatePickerView);
            attrHint(attributes);
            attributes.recycle();
        }
    }

    private void attrHint(TypedArray attributes) {
        String hint = attributes.getString(R.styleable.DatePickerView_hint);
        if (!TextUtils.isEmpty(hint)) {
            textInput.setHint(hint);
        }
    }

    public String getText() {
        return editText.getText().toString();
    }

    public void setText(String text) {
        editText.setText(text);
    }

    public void setText(Calendar calendar) {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        editText.setText(sdf.format(calendar.getTime()));
    }

    @OnClick(R.id.date_picker_view_editText)
    public void onClick() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(getContext(), R.style.Theme_AppCompat_Dialog, (DatePickerDialog.OnDateSetListener) (view, year1, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year1);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setText(calendar);
        }, year, month, mDay).show();
    }

}
