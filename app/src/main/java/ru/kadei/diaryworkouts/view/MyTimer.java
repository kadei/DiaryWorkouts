//package ru.kadei.diaryworkouts.view;
//
//import android.content.Context;
//import android.content.res.Resources;
//import android.content.res.TypedArray;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.Typeface;
//import android.graphics.drawable.Drawable;
//import android.util.AttributeSet;
//import android.view.View;
//import android.widget.ImageButton;
//
//import serega_kadei.diary.R;
//import serega_kadei.util.time.GeneratorOfTact;
//
//import static java.lang.Math.max;
//import static java.lang.System.currentTimeMillis;
//import static serega_kadei.data_base.DBH.FALSE;
//import static serega_kadei.data_base.DBH.TRUE;
//import static serega_kadei.training_data.data_packages.CurrentTrainingPackage.CURRENT_PERIOD_OF_TIMER;
//import static serega_kadei.training_data.data_packages.CurrentTrainingPackage.IS_PAUSE;
//import static serega_kadei.training_data.data_packages.CurrentTrainingPackage.TIME_OF_START_PAUSE;
//import static serega_kadei.training_data.data_packages.CurrentTrainingPackage.PERIOD_AT_STARTING;
//import static serega_kadei.training_data.data_packages.CurrentTrainingPackage.TIME_OF_START_TIMER;
//
//public class MyTimer extends View implements View.OnClickListener, GeneratorOfTact.Callback {
//    // for convert
//    final int[] start_len = new int[]{0, 8};  // start index and len for draw
//    final char[] buffer = new char[]{'0', '0', ':', '0', '0', ':', '0', '0'}; // hh:mm:ss
//
//    Paint mPaintForBlink;
//    Paint mTextPaint;
//    volatile Paint mCurrentPaint; // reference in mPaintForBink or mTextPaint
//    int mTextColor;
//    int mTextHeight;
//    int mTextWidth;
//
//    float mPosX;
//    float mPosY;
//    int mWidth;
//    int mHeight;
//
//    GeneratorOfTact mGenerator;
//
//    long mStartTime;
//    long mPeriod;
//    long mActivePeriod;
//    volatile boolean isActive = false;
//    volatile boolean isPause = false;
//    boolean mSwitcher = false;
//    long mPauseTime = 0L;
//
//    long mTick;
//    int mRange; // must be equals HOUR, MINUTE or SECOND
//    final int mType;  // must be equals TIMER or STOPWATCH
//
//    Listener mListener;
//
//    int mStartButtonID, mResetButtonID;   // button for control mTimer
//
//    ImageButton mStartButton; // for change source drawable
//    Drawable mPauseDrawable;
//    Drawable mPlayDrawable;
//
//    static final long BLINK_TICK = 500L;
//
//    static final int TIMER = 0;
//    static final int STOPWATCH = 1;
//
//    static final int MAX_VALUE = 3600000 * 99; // 99 hours
//    static final int DEFAULT_VALUE = 60000;
//    static final int DEFAULT_TICK = 200;
//
//    static final int DEFAULT_COLOR = 0xff101010;
//    static final int DEFAULT_TEXT_HEIGHT = 36;
//
//    static final int HOUR = 1;
//    static final int MINUTE = 2;
//    static final int SECOND = 3;
//
//    public MyTimer(Context context) {
//        this(context, null);
//    }
//
//    public MyTimer(Context context, AttributeSet attrs) {
//        super(context, attrs);
//
//        TypedArray ta = context.getTheme().obtainStyledAttributes(
//                attrs, R.styleable.MyTimer, 0, 0);
//
//        try {
//            mPeriod = ta.getInteger(R.styleable.MyTimer_period, DEFAULT_VALUE);
//            testValue(mPeriod);
//
//            mTick = ta.getInteger(R.styleable.MyTimer_tick, DEFAULT_TICK);
//            mTextHeight = ta.getDimensionPixelSize(R.styleable.MyTimer_fontSize,
//                    DEFAULT_TEXT_HEIGHT);
//            mTextColor = ta.getColor(R.styleable.MyTimer_fontColor, DEFAULT_COLOR);
//            mRange = ta.getInteger(R.styleable.MyTimer_range, MINUTE);
//            mType = ta.getInteger(R.styleable.MyTimer_type, TIMER);
//        } finally {
//            ta.recycle();
//        }
//
//        Typeface typeface = CustomView.obtainTypeface(context, attrs);
//
//        Resources res = getResources();
//        mPauseDrawable = res.getDrawable(R.drawable.ic_pause_black_24dp);
//        mPlayDrawable = res.getDrawable(R.drawable.ic_play_arrow_black_24dp);
//
//        // initial
//        if (mType == TIMER) {
//            valueToString(buffer, mPeriod, start_len, mRange);
//            setClickable(true);
//            setOnClickListener(this);
//        } else mPeriod = MAX_VALUE;
//
//        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mTextPaint.setTypeface(typeface);
//        mTextPaint.setColor(mTextColor);
//        mTextPaint.setTextAlign(Paint.Align.LEFT);
//        mTextPaint.setTextSize(mTextHeight);
//        mPaintForBlink = new Paint(mTextPaint);
//        int transparent = res.getColor(android.R.color.transparent);
//        mPaintForBlink.setColor(transparent);
//        mCurrentPaint = mTextPaint;
//
//        float[] widths = new float[buffer.length];
//        int size = mTextPaint.getTextWidths(buffer, start_len[0], start_len[1], widths);
//        mTextWidth = 0;
//
//        for (int i = 0; i < size; ++i) mTextWidth += (int) widths[i];
//
//        mGenerator = new GeneratorOfTact(this, mTick);
//    }
//
//    public void setControlButton(int btnStartId, int btnResetId, View root) {
//        this.mStartButtonID = btnStartId;
//        this.mResetButtonID = btnResetId;
//        mStartButton = (ImageButton) root.findViewById(btnStartId);
//        mStartButton.setOnClickListener(this);
//        ImageButton btn = (ImageButton) root.findViewById(btnResetId);
//        btn.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//
//        if (id == mStartButtonID) btnStartClick();
//        else if (id == mResetButtonID) btnResetClick();
//        else if (id == getId()) {
//            if (mListener != null) mListener.onTimerClick();
//        }
//    }
//
//    void btnStartClick() {
//        start();
//        if (mListener != null) mListener.onStartButtonClick();
//    }
//
//    void btnResetClick() {
//        reset();
//        if (mListener != null) mListener.onResetButtonClick();
//    }
//
//    /**
//     * continue stopwatch
//     */
//    public void goOn(long startTime) {
//        goOn(startTime, mPeriod, mPeriod, FALSE, 0L);
//    }
//
//    /**
//     * <b>valuesTimer See:</b>
//     * {@link serega_kadei.training_data.data_packages.CurrentTrainingPackage#valuesTimer}
//     */
//    public void goOn(long[] timerValues) {
//        goOn(timerValues[TIME_OF_START_TIMER],
//                timerValues[PERIOD_AT_STARTING],
//                timerValues[CURRENT_PERIOD_OF_TIMER],
//                timerValues[IS_PAUSE],
//                timerValues[TIME_OF_START_PAUSE]);
//    }
//
//    public void getTimerValues(long[] timerValues) {
//        timerValues[TIME_OF_START_TIMER] = mStartTime;
//        timerValues[PERIOD_AT_STARTING] = mActivePeriod;
//        timerValues[CURRENT_PERIOD_OF_TIMER] = mPeriod;
//        timerValues[IS_PAUSE] = isPause ? TRUE : FALSE;
//        timerValues[TIME_OF_START_PAUSE] = mPauseTime;
//    }
//
//    /**
//     * continue if allows.
//     */
//    void goOn(long startTime,
//              long periodAtStarting,
//              long period,
//              long isPause,
//              long pauseTime) {
//
//        if (isPause == TRUE) {
//            isActive = true;
//            mStartTime = startTime;
//            mActivePeriod = periodAtStarting;
//            mPeriod = period;
//            updateView(periodAtStarting - (pauseTime - startTime));
//            pause(pauseTime);
//            return;
//        }
//
//        long time = currentTimeMillis();
//        if (time - startTime < periodAtStarting) {
//            mStartTime = startTime;
//            mPeriod = period;
//            mActivePeriod = periodAtStarting;
//
//            // update view before start
//            if (mType == TIMER) timerTick();
//            else stopwatchTick();
//
//            if(mStartButton != null) mStartButton.setImageDrawable(mPauseDrawable);
//
//            doStart();
//        } else {
//            if (mType == TIMER) setPeriod(period);
//            else updateView(0L);
//        }
//    }
//
//    /**
//     * action on click start button
//     */
//    public void start() {
//        if (isActive) {
//            if (isPause) restart();
//            else pause(currentTimeMillis());
//        } else {
//            mStartTime = currentTimeMillis();
//            mActivePeriod = mPeriod;
//            mStartButton.setImageDrawable(mPauseDrawable);
//            doStart();
//        }
//    }
//
//    void doStart() {
//        isActive = true;
//        mGenerator.wakeIfNeed();
//    }
//
//    public void pause(long pauseTime) {
//        mStartButton.setImageDrawable(mPlayDrawable);
//        mGenerator.setTick(BLINK_TICK);
//        mPauseTime = pauseTime;
//        isPause = true;
//        mGenerator.wakeIfNeed();
//    }
//
//    void restart() {
//        mStartTime += currentTimeMillis() - mPauseTime;
//        mStartButton.setImageDrawable(mPauseDrawable);
//        mCurrentPaint = mTextPaint;
//        isPause = false;
//        mPauseTime = 0L;
//        mGenerator.setTick(mTick);
//    }
//
//    public void reset() {
//        if (mType == TIMER) {
//            stop();
//            if (isPause) {
//                isPause = false;
//                mPauseTime = 0L;
//                mCurrentPaint = mTextPaint;
//                mGenerator.setTick(mTick);
//            }
//            updateView(mPeriod);
//        }
//    }
//
//    public void stop() {
//        isActive = false;
//        mStartTime = 0L;
//        mActivePeriod = mPeriod;
//        mGenerator.pause();
//
//        // for set drawable from another thread
//        if (mStartButton != null) {
//            mStartButton.post(new Runnable() {
//                @Override
//                public void run() {
//                    mStartButton.setImageDrawable(mPlayDrawable);
//                }
//            });
//        }
//    }
//
//    public void cancel() {
//        mGenerator.stop();
//    }
//
//    public void setPeriod(long period) {
//        testValue(period);
//        mPeriod = period;
//
//        if (!isActive) {
//            updateView(period);
//        }
//    }
//
//    void updateView(long value) {
//        valueToString(buffer, value, start_len, mRange);
//        postInvalidate();
//    }
//
//    public void setTick(long tick) {
//        mTick = tick;
//        if (!isPause) mGenerator.setTick(tick);
//    }
//
//    public void setListener(Listener listener) {
//        mListener = listener;
//    }
//
//    void timerTick() {
//        long time = currentTimeMillis();
//        long end = mStartTime + mActivePeriod;
//        if (time < end) {
//            updateView(end - time);
//            if (mListener != null) mListener.onTimerTick(end - time);
//        } else {
//            timerEnd();
//        }
//    }
//
//    void timerEnd() {
//        if (isActive) {
//            stop();
//            updateView(mPeriod);
//
//            if (mListener != null) mListener.onTimerFinish();
//        }
//    }
//
//    void stopwatchTick() {
//        long time = currentTimeMillis();
//        long end = mStartTime + mActivePeriod;
//
//        if (time < end) updateView(time - mStartTime);
//        else stopwatchEnd();
//    }
//
//    void stopwatchEnd() {
//        if (isActive) {
//            stop();
//            updateView(0L);
//        }
//    }
//
//    void blink() {
//        mCurrentPaint = mSwitcher ? mTextPaint : mPaintForBlink;
//        postInvalidate();
//        mSwitcher = !mSwitcher;
//    }
//
//    @Override
//    public void tick() {
//        if (isPause) blink();
//        else {
//            if (mType == TIMER) timerTick();
//            else stopwatchTick();
//        }
//    }
//
//    // ************************** override ******************************
//    @Override
//    protected int getSuggestedMinimumHeight() {
//        return max(mTextHeight, getMinimumHeight());
//    }
//
//    @Override
//    protected int getSuggestedMinimumWidth() {
//        return max(mTextWidth, getMinimumWidth());
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int mode = MeasureSpec.getMode(widthMeasureSpec);
//        int w = MeasureSpec.getSize(widthMeasureSpec);
//        int wmin = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
//        if (mode == MeasureSpec.UNSPECIFIED) w = wmin;
//        else if (mode == MeasureSpec.AT_MOST) w = Math.min(wmin, w);
//
//        mode = MeasureSpec.getMode(heightMeasureSpec);
//        int h = MeasureSpec.getSize(heightMeasureSpec);
//        int hmin = getPaddingTop() + getPaddingBottom() + getSuggestedMinimumHeight();
//        if (mode == MeasureSpec.UNSPECIFIED) h = hmin;
//        else if (mode == MeasureSpec.AT_MOST) h = Math.min(hmin, h);
//
//        setMeasuredDimension(w, h);
//    }
//
//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        mWidth = w;
//        mHeight = h;
//        mPosX = w / 2 - mTextWidth / 2;
//        mPosY = h / 2 + mTextHeight / 3;
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        canvas.drawText(buffer, start_len[0], start_len[1], mPosX, mPosY, mCurrentPaint);
//    }
//
//    /**
//     * Callback function
//     */
//    public interface Listener {
//        void onTimerTick(long millisUntilFinished);
//
//        void onTimerFinish();
//
//        void onTimerClick();
//
//        void onStartButtonClick();
//
//        void onResetButtonClick();
//    }
//
//    private static void testValue(long value) {
//        if (value > MAX_VALUE)
//            throw new IllegalArgumentException("value = " + value + " MAX_VALUE = " + MAX_VALUE);
//    }
//
//    // convert millis value to string (format hh:mm:ss)
//    private static void valueToString(char[] buffer, long value,
//                                      int[] start_len, int range) {
//        int amount;
//        switch (range) {
//            case MyTimer.HOUR:
//                start_len[0] = 0;
//                start_len[1] = 8;    // length for hh:mm:ss
//
//                amount = (int) (value / 3600000); // amount hours
//                buffer[0] = TENS[amount];   // number of tens
//                buffer[1] = ONES[amount];   // number of units
//                buffer[2] = ':';
//
//                value %= 3600000;
//                amount = (int) (value / 60000); // amount minutes
//                buffer[3] = TENS[amount];
//                buffer[4] = ONES[amount];
//                buffer[5] = ':';
//
//                value %= 60000;
//                amount = (int) (value / 1000); // amount seconds
//                buffer[6] = TENS[amount];
//                buffer[7] = ONES[amount];
//                break;
//
//            case MyTimer.MINUTE:
//                start_len[0] = 3;
//                start_len[1] = 5;    // length for --:mm:ss
//
//                value %= 3600000;
//                amount = (int) (value / 60000); // amount minutes
//                buffer[3] = TENS[amount];
//                buffer[4] = ONES[amount];
//                buffer[5] = ':';
//
//                value %= 60000;
//                amount = (int) (value / 1000); // amount seconds
//                buffer[6] = TENS[amount];
//                buffer[7] = ONES[amount];
//                break;
//
//            case MyTimer.SECOND:
//                start_len[0] = 6;
//                start_len[1] = 2;    // length for --:--:ss
//
//                value %= 60000;
//                amount = (int) (value / 1000); // amount seconds
//                buffer[6] = TENS[amount];
//                buffer[7] = ONES[amount];
//
//            default:
//                // ignore
//        }
//    }
//
//    /**
//     * TENS[i] contains the tens digit of the number i, 0 <= i <= 99.
//     */
//    private static final char[] TENS = {
//            '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
//            '1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
//            '2', '2', '2', '2', '2', '2', '2', '2', '2', '2',
//            '3', '3', '3', '3', '3', '3', '3', '3', '3', '3',
//            '4', '4', '4', '4', '4', '4', '4', '4', '4', '4',
//            '5', '5', '5', '5', '5', '5', '5', '5', '5', '5',
//            '6', '6', '6', '6', '6', '6', '6', '6', '6', '6',
//            '7', '7', '7', '7', '7', '7', '7', '7', '7', '7',
//            '8', '8', '8', '8', '8', '8', '8', '8', '8', '8',
//            '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'
//    };
//
//    /**
//     * Ones [i] contains the tens digit of the number i, 0 <= i <= 99.
//     */
//    private static final char[] ONES = {
//            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
//            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
//            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
//            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
//            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
//            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
//            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
//            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
//            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
//            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
//    };
//}
//
//
