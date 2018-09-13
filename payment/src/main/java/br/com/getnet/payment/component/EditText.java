package br.com.getnet.payment.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;


import br.com.getnet.payment.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;

public class EditText extends FrameLayout {

    @BindView(R.id.edit_text_input)
    protected TextInputLayout textInput;

    @BindView(R.id.edit_text)
    protected AppCompatEditText editText;

    public EditText(Context context) {
        super(context);
        init(null);
    }

    public EditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public EditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.edit_text, this);
        ButterKnife.bind(this, view);
        if (attrs != null) {
            TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.EditText);
            attrHint(attributes);
            attrText(attributes);
            attrInputType(attributes);
            attrMaxLength(attributes);
            attrHideKeyboard(attributes);
            attrRequestFocus(attributes);
            attrMask(attributes);
            attributes.recycle();
        }
        addFocusAnimation();
    }

    private void attrMask(TypedArray attributes) {
        int value = attributes.getInteger(R.styleable.EditText_editText_mask, 0);
        switch (value) {
            case 1:
                editText.addTextChangedListener(new MoneyTextWatcher(editText));
                break;
        }
    }

    public boolean isNotEmpty() {
        return !TextUtils.isEmpty(editText.getText());
    }

    private void addFocusAnimation() {
        editText.setOnTouchListener(getOnTouchListener());
        textInput.setOnTouchListener(getOnTouchListener());
    }

    private OnTouchListener getOnTouchListener() {
        return (v, event) -> {
            int width = editText.getWidth();
            ViewAnimationUtils.createCircularReveal(editText,
                    width / 2,
                    editText.getWidth() / 2,
                    width / 2,
                    width).start();
            return false;
        };
    }

    private void attrMaxLength(TypedArray attributes) {
        Integer value = attributes.getInteger(R.styleable.EditText_editText_maxLength, 0);
        if (value > 0) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(value)});
        }
    }

    private void attrInputType(TypedArray attributes) {
        int value = attributes.getInteger(R.styleable.EditText_editText_inputType, 0);
        switch (value) {
            case 1:
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case 2:
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case 3:
                editText.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case 4:
                editText.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                break;
            case 5:
                editText.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD | InputType.TYPE_CLASS_NUMBER);
                break;
        }
    }

    private void attrText(TypedArray attributes) {
        String value = attributes.getString(R.styleable.EditText_editText_text);
        if (!TextUtils.isEmpty(value)) {
            editText.setText(value);
        }
    }

    private void attrHint(TypedArray attributes) {
        String value = attributes.getString(R.styleable.EditText_editText_hint);
        if (!TextUtils.isEmpty(value)) {
            textInput.setHint(value);
        }
    }

    private void attrHideKeyboard(TypedArray attributes) {
        boolean value = attributes.getBoolean(R.styleable.EditText_editText_hideKeyboard, false);
        if (value) {
            textInput.setFocusable(false);
            textInput.setFocusableInTouchMode(false);
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
        }
    }

    private void attrRequestFocus(TypedArray attributes) {
        boolean value = attributes.getBoolean(R.styleable.EditText_editText_requestFocus, false);
        if (value) {
            textInput.requestFocus();
        }
    }

    public String getText() {
        return editText.getText().toString();
    }

    @OnFocusChange(R.id.edit_text_input)
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            editText.requestFocus();
        }
    }

    @OnEditorAction(R.id.edit_text)
    public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            return true;
        }
        return false;
    }

    public AppCompatEditText getEditText() {
        return editText;
    }
}