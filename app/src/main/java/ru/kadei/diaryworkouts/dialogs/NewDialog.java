package ru.kadei.diaryworkouts.dialogs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.kadei.diaryworkouts.R;

/**
 * Created by kadei on 13.10.15.
 */
public class NewDialog extends CustomDialog {

    public static final String KEY_CONTENT_MSG = "content_msg";

    String text;

    @Override
    protected void applyArgs(Bundle args) {
        text = args.getString(KEY_CONTENT_MSG, "");
    }

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("TEST", "custom create view");
        View v = inflater.inflate(R.layout.dialog_test, container, false);
        TextView tv = (TextView) v.findViewById(android.R.id.text1);
        tv.setText(text);
        return v;
    }

    public static NewDialog createDialog(String title, String message) {
        Bundle args = new Bundle(8);
        args.putString(KEY_TITLE, title);
        args.putInt(KEY_TYPE_CONTENT, STATIC_CONTENT);
        args.putString(KEY_CONTENT_MSG, message);

        args.putInt(KEY_ACTIVE_BUTTONS, MASK_RIGHT_TOP_BTN | MASK_CENTER_BTN | MASK_LEFT_BOTTOM_BTN);
        args.putString(KEY_TEXT_RIGHT_TOP_BTN, "apply");
        args.putString(KEY_TEXT_CENTER_BTN, "cancel");
        args.putString(KEY_TEXT_LEFT_BOTTOM_BTN, "rolling back");

        NewDialog d = new NewDialog();
        d.setArguments(args);
        return d;
    }
}
