package br.com.getnet.payment.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

import br.com.getnet.payment.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class Spinner extends FrameLayout {

    @BindView(R.id.spinner_textView)
    protected AppCompatTextView textView;

    @BindView(R.id.spinner)
    protected android.widget.Spinner spinner;

    public Spinner(@NonNull Context context) {
        super(context);
        init(null);
    }

    public Spinner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public Spinner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public Spinner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        View view = inflate(getContext(), R.layout.spinner, this);
        ButterKnife.bind(this, view);

        if (attrs != null) {
            TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.Spinner);
            attrText(attributes);
            attrEntries(attributes);
            attributes.recycle();
        }
    }

    private void attrEntries(TypedArray attributes) {
        CharSequence[] value = attributes.getTextArray(R.styleable.Spinner_spinner_entries);
        if (value != null) {
            ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, value);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
    }

    private void attrText(TypedArray attributes) {
        String value = attributes.getString(R.styleable.Spinner_spinner_text);
        if (value != null) {
            textView.setText(value);
        }
    }

    public int getSelectedItemPosition() {
        return spinner.getSelectedItemPosition();
    }

    public String getSelectedItem() {
        return (String) spinner.getSelectedItem();
    }
}
