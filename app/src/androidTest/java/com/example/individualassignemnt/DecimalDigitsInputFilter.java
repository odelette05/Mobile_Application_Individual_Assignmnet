package com.example.individualassignemnt;

import android.text.InputFilter;
import android.text.Spanned;

public class DecimalDigitsInputFilter implements InputFilter {
    private final int decimalDigits;

    public DecimalDigitsInputFilter(int decimalDigits) {
            this.decimalDigits = decimalDigits;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
        StringBuilder builder = new StringBuilder(dest);
        builder.replace(dstart, dend, source
                .subSequence(start, end).toString());
        if (!builder.toString().matches(
                "(([1-9]{1})([0-9]{0," + (decimalDigits - 1) + "})?)?(\\.[0-9]{0," + decimalDigits + "})?"
        )) {
            if (source.length() == 0)
                return dest.subSequence(dstart, dend);
            return "";
        }

        return null;
    }
}
