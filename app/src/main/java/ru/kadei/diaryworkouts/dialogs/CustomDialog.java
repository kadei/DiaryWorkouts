package ru.kadei.diaryworkouts.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ru.kadei.diaryworkouts.R;
import ru.kadei.diaryworkouts.activities.MainActivity;
import ru.kadei.diaryworkouts.fragments.CustomFragment;
import ru.kadei.diaryworkouts.view.FrameLayoutFixedHeight;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.RelativeLayout.ALIGN_PARENT_END;
import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;
import static android.widget.RelativeLayout.ALIGN_PARENT_START;
import static android.widget.RelativeLayout.ALIGN_PARENT_TOP;
import static android.widget.RelativeLayout.BELOW;
import static android.widget.RelativeLayout.LEFT_OF;
import static android.widget.RelativeLayout.START_OF;
import static android.widget.RelativeLayout.TRUE;
import static java.lang.System.arraycopy;

/**
 * Created by kadei on 11.10.15.
 */
public class CustomDialog extends DialogFragment {

    public static final String KEY_ACTIVE_BUTTONS = "active_button";
    public static final int MASK_RIGHT_TOP_BTN = 1;
    public static final int MASK_CENTER_BTN = 2;
    public static final int MASK_LEFT_BOTTOM_BTN = 4;

    public static final String KEY_TITLE = "title";
    public static final String KEY_TEXT_RIGHT_TOP_BTN = "right";
    public static final String KEY_TEXT_CENTER_BTN = "center";
    public static final String KEY_TEXT_LEFT_BOTTOM_BTN = "left";

    public static final String KEY_MAX_HEIGHT_CONTENT = "max_height_content";
    public static final int MAX_HEIGHT_CONTENT_DEFAULT = 240;

    public static final String KEY_TYPE_CONTENT = "type_content";
    public static final int STATIC_CONTENT = 1;
    public static final int SCROLL_CONTENT = 2;

    private String title;
    private final SparseArray<String> textButtons = new SparseArray<>(3);
    private int activeButtons;
    private int typeContent;
    private int maxHeightContent;

    private CustomFragment fragment;

    @Override
    public final void onAttach(Activity activity) {
        super.onAttach(activity);
        fragment = ((MainActivity) activity).getActiveFragment();
        onFragmentObtained(fragment);

        Bundle args = getArguments();
        if (args == null) args = new Bundle();
        applyArgsDefault(args);
        applyArgs(args);
    }

    protected void onFragmentObtained(CustomFragment customFragment) {

    }

    private void applyArgsDefault(Bundle args) {
        title = args.getString(KEY_TITLE, "");
        typeContent = args.getInt(KEY_TYPE_CONTENT, STATIC_CONTENT);

        activeButtons = args.getInt(KEY_ACTIVE_BUTTONS, MASK_RIGHT_TOP_BTN | MASK_CENTER_BTN | MASK_LEFT_BOTTOM_BTN);
        textButtons.put(R.id.dialog_right_top_button, args.getString(KEY_TEXT_RIGHT_TOP_BTN, ""));
        textButtons.put(R.id.dialog_center_button, args.getString(KEY_TEXT_CENTER_BTN, ""));
        textButtons.put(R.id.dialog_left_bottom_button, args.getString(KEY_TEXT_LEFT_BOTTOM_BTN, ""));

        maxHeightContent = args.getInt(KEY_MAX_HEIGHT_CONTENT, MAX_HEIGHT_CONTENT_DEFAULT);
        maxHeightContent = dpToPx(maxHeightContent, getDensity());
    }

    protected void applyArgs(Bundle args) {
    }

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final LinearLayout root = getContainerView(inflater, container);
        final TextView title = getTitleView(inflater, root);
        final FrameLayout containerContent = getContainerContentView(inflater, root);

        final View content = onCreateContentView(inflater, container, savedInstanceState);

        final RelativeLayout btnArea = getButtonsArea(inflater, root);

        root.addView(title);
        if (content != null)
            containerContent.addView(content);
        root.addView(containerContent);
        root.addView(btnArea);
        return root;
    }

    private LinearLayout getContainerView(LayoutInflater inflater, ViewGroup container) {
        return (LinearLayout) inflater.inflate(R.layout.dialog_root, container, false);
    }

    private TextView getTitleView(LayoutInflater inflater, LinearLayout dialogContainer) {
        TextView tv = (TextView) inflater.inflate(R.layout.dialog_title, dialogContainer, false);
        tv.setText(title);
        return tv;
    }

    private FrameLayoutFixedHeight getContainerContentView(LayoutInflater inflater, LinearLayout dialogContainer) {
        int layoutId;
        if (typeContent == STATIC_CONTENT)
            layoutId = R.layout.dialog_container_static;
        else if (typeContent == SCROLL_CONTENT)
            layoutId = R.layout.dialog_container_scroll;
        else
            throw new RuntimeException("Unexpected type content: " + Integer.toString(typeContent));

        final FrameLayoutFixedHeight fl = (FrameLayoutFixedHeight) inflater.inflate(layoutId, dialogContainer, false);
        fl.setMaxHeight(maxHeightContent);
        return fl;
    }

    private RelativeLayout getButtonsArea(LayoutInflater inflater, LinearLayout dialogContainer) {
        final RelativeLayout area = createButtonsArea();
        final Button[] buttons = createButtons(inflater, area);
        setTextFor(buttons);
        solveDirectionFor(buttons);

        for (Button btn : buttons)
            area.addView(btn);

        return area;
    }

    private RelativeLayout createButtonsArea() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        final float density = getDensity();
        lp.setMargins(dpToPx(24f, density), // left
                dpToPx(8f, density),    // top
                dpToPx(8f, density),    // right
                dpToPx(8f, density));   // bottom

        RelativeLayout rl = new RelativeLayout(getActivity());
        rl.setLayoutParams(lp);
        return rl;
    }

    private float getDensity() {
        return getActivity().getResources().getDisplayMetrics().density;
    }

    private static int dpToPx(float dp, float density) {
        int result = (int) (dp * density);
        if (result == 0) result = 1;
        return result;
    }

    private Button[] createButtons(LayoutInflater inflater, RelativeLayout area) {
        final int[] buttonIds = solveButtonIds();
        return createButtonsFor(buttonIds, inflater, area);
    }

    private int[] solveButtonIds() {
        final int[] ids = new int[]{
                R.id.dialog_right_top_button,
                R.id.dialog_center_button,
                R.id.dialog_left_bottom_button
        };
        final int size = countActiveButton();
        final int[] resultIds = new int[size];
        arraycopy(ids, 0, resultIds, 0, size);
        return resultIds;
    }

    private int countActiveButton() {
        int countButton = 0;
        for (int mask = MASK_RIGHT_TOP_BTN; mask <= MASK_LEFT_BOTTOM_BTN; mask <<= 1) {
            if ((activeButtons & mask) != 0)
                ++countButton;
        }
        return countButton;
    }

    private Button[] createButtonsFor(int[] ids, LayoutInflater inflater, RelativeLayout area) {
        final Button[] buttons = new Button[ids.length];
        int count = 0;
        for (int id : ids) {
            Button btn = (Button) inflater.inflate(R.layout.dialog_button, area, false);
            btn.setId(id);
            btn.setOnClickListener(defaultListener);

            ViewGroup.LayoutParams lp = btn.getLayoutParams();
            if (lp == null || !(lp instanceof RelativeLayout.LayoutParams))
                btn.setLayoutParams(defaultRelativeLP());

            buttons[count++] = btn;
        }
        return buttons;
    }

    private static RelativeLayout.LayoutParams defaultRelativeLP() {
        return new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
    }

    private void setTextFor(Button[] buttons) {
        final SparseArray<String> texts = textButtons;
        for (Button btn : buttons)
            btn.setText(texts.get(btn.getId()));
    }

    private void solveDirectionFor(Button[] buttons) {
        final int widest = getWidestFrom(buttons);
        final int dialogWidth = approximateDialogWidth();
        final int maxButtonWidth = (dialogWidth - 3 * dpToPx(8f, getDensity())) / 2;
        if (widest >= maxButtonWidth || countActiveButton() >= 3)
            verticalDirection(buttons);
        else
            horizontalDirection(buttons);
    }

    private int getWidestFrom(Button[] buttons) {
        int max = approximateWidth(buttons[0]);
        for (int i = 1, end = buttons.length; i < end; ++i) {
            int newW = approximateWidth(buttons[i]);
            if (newW > max)
                max = newW;
        }
        return max;
    }

    private int approximateWidth(Button btn) {
        final int len = btn.length();
        final float[] widths = new float[len];
        btn.getPaint().getTextWidths(btn.getText(), 0, len, widths);

        float sum = 0f;
        for (float w : widths)
            sum += w;

        return (int) (sum + dpToPx(32f, getDensity()));
    }

    private int approximateDialogWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels - dpToPx(48f, dm.density);
    }

    private void horizontalDirection(Button[] buttons) {
        int alignParentLeft, alignParentRight, toLeftOf;
        if (SDK_INT >= JELLY_BEAN_MR1) {
            alignParentLeft = ALIGN_PARENT_START;
            alignParentRight = ALIGN_PARENT_END;
            toLeftOf = START_OF;
        } else {
            alignParentLeft = ALIGN_PARENT_LEFT;
            alignParentRight = ALIGN_PARENT_RIGHT;
            toLeftOf = LEFT_OF;
        }

        int rightId = 0;
        for (Button btn : buttons) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) btn.getLayoutParams();
            int id = btn.getId();

            if (id == R.id.dialog_right_top_button) {
                lp.addRule(alignParentRight, TRUE);
                rightId = id;
            } else if (id == R.id.dialog_center_button) {
                lp.addRule(toLeftOf, rightId);
                final int rightMargin = dpToPx(8f, getDensity());
                lp.setMargins(0, 0, rightMargin, 0);
            } else if (id == R.id.dialog_left_bottom_button) {
                lp.addRule(alignParentLeft, TRUE);
            }
            lp.addRule(ALIGN_PARENT_TOP, TRUE);
        }
    }

    private void verticalDirection(Button[] buttons) {
        @SuppressLint("InlinedApi")
        final int alignParentRight = SDK_INT >= JELLY_BEAN_MR1
                ? ALIGN_PARENT_END : ALIGN_PARENT_RIGHT;

        final int[] ids = new int[buttons.length];
        int countIds = 0;

        for (Button btn : buttons) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) btn.getLayoutParams();
            int id = btn.getId();

            if (id == R.id.dialog_right_top_button) {
                lp.addRule(ALIGN_PARENT_TOP, TRUE);
                ids[countIds++] = id;
            } else {
                lp.addRule(BELOW, ids[countIds - 1]);
                ids[countIds++] = id;
            }
            // all buttons align right
            lp.addRule(alignParentRight, TRUE);
        }
    }

    private final View.OnClickListener defaultListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int id = v.getId();
            if (id == R.id.dialog_right_top_button)
                rightTopButtonClick();
            else if (id == R.id.dialog_center_button)
                centerButtonClick();
            else if (id == R.id.dialog_left_bottom_button)
                leftBottomButtonClick();
        }
    };

    protected void rightTopButtonClick() {
    }

    protected void centerButtonClick() {
    }

    protected void leftBottomButtonClick() {
    }

    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    protected final CustomFragment getFragment() {
        return fragment;
    }

    protected final Object getObjectFromGeneralStorage(int id) {
        return getMainActivity().getGeneralStorage().get(id);
    }

    protected final MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }
}
