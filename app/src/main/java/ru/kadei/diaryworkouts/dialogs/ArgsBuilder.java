package ru.kadei.diaryworkouts.dialogs;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.StringRes;

import static ru.kadei.diaryworkouts.dialogs.CustomDialog.KEY_ACTIVE_BUTTONS;
import static ru.kadei.diaryworkouts.dialogs.CustomDialog.KEY_MAX_HEIGHT_CONTENT;
import static ru.kadei.diaryworkouts.dialogs.CustomDialog.KEY_TEXT_NEUTRAL_BTN;
import static ru.kadei.diaryworkouts.dialogs.CustomDialog.KEY_TEXT_NEGATIVE_BTN;
import static ru.kadei.diaryworkouts.dialogs.CustomDialog.KEY_TEXT_POSITIVE_BTN;
import static ru.kadei.diaryworkouts.dialogs.CustomDialog.KEY_TITLE;
import static ru.kadei.diaryworkouts.dialogs.CustomDialog.KEY_TYPE_CONTENT;
import static ru.kadei.diaryworkouts.dialogs.CustomDialog.MASK_NEUTRAL_BTN;
import static ru.kadei.diaryworkouts.dialogs.CustomDialog.MASK_NEGATIVE_BTN;
import static ru.kadei.diaryworkouts.dialogs.CustomDialog.MASK_POSITIVE_BTN;
import static ru.kadei.diaryworkouts.dialogs.CustomDialog.SCROLL_CONTENT;
import static ru.kadei.diaryworkouts.dialogs.CustomDialog.STATIC_CONTENT;

/**
 * Created by kadei on 14.10.15.
 */
public class ArgsBuilder {

    private final Resources res;
    private final Bundle args;

    public ArgsBuilder(Context context) {
        args = new Bundle(16);
        res = context.getResources();
    }

    public ArgsBuilder setTitle(@StringRes int id) {
        return setTitle(res.getString(id));
    }

    public ArgsBuilder setTitle(String title) {
        args.putString(KEY_TITLE, title);
        return this;
    }

    public ArgsBuilder addPositiveButton(@StringRes int id) {
        return addPositiveButton(res.getString(id));
    }

    public ArgsBuilder addPositiveButton(String text) {
        addBtnFlag(MASK_POSITIVE_BTN);
        args.putString(KEY_TEXT_POSITIVE_BTN, text);
        return this;
    }

    public ArgsBuilder addNeutralButton(@StringRes int id) {
        return addNeutralButton(res.getString(id));
    }

    public ArgsBuilder addNeutralButton(String text) {
        addBtnFlag(MASK_NEUTRAL_BTN);
        args.putString(KEY_TEXT_NEUTRAL_BTN, text);
        return this;
    }

    public ArgsBuilder addNegativeButton(@StringRes int id) {
        return addNegativeButton(res.getString(id));
    }

    public ArgsBuilder addNegativeButton(String text) {
        addBtnFlag(MASK_NEGATIVE_BTN);
        args.putString(KEY_TEXT_NEGATIVE_BTN, text);
        return this;
    }

    private void addBtnFlag(int flag) {
        int val = args.getInt(KEY_ACTIVE_BUTTONS, 0);
        val |= flag;
        args.putInt(KEY_ACTIVE_BUTTONS, val);
    }

    public ArgsBuilder setMaxHeightContent(int dp) {
        args.putInt(KEY_MAX_HEIGHT_CONTENT, dp);
        return this;
    }

    public ArgsBuilder applyStaticContent() {
        args.putInt(KEY_TYPE_CONTENT, STATIC_CONTENT);
        return this;
    }

    public ArgsBuilder applyScrollContent() {
        args.putInt(KEY_TYPE_CONTENT, SCROLL_CONTENT);
        return this;
    }

    public Bundle getArgs() {
        return args;
    }
}
