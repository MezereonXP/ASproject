package com.checkfile.checkfile.View;



import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.checkfile.checkfile.R;

public class PullToRefreshListView extends ListView implements OnScrollListener {
	// ״̬
    private static final int TAP_TO_REFRESH = 1;//���ˢ��
    private static final int PULL_TO_REFRESH = 2;  //����ˢ�� 
    private static final int RELEASE_TO_REFRESH = 3; //�ͷ�ˢ��
    private static final int REFRESHING = 4;  //����ˢ��
    // ��ǰ����״̬
    private int mCurrentScrollState;
    // ��ǰˢ��״̬ 
    private int mRefreshState;
    //ͷ��ͼ�ĸ߶�
    private int mRefreshViewHeight;
    //ͷ��ͼ ԭʼ��top padding ����ֵ
    private int mRefreshOriginalTopPadding;
    private int mLastMotionY;
    // ������listview�Ļ�������
    private OnRefreshListener mOnRefreshListener;
    //��ͷͼƬ
    private static  int REFRESHICON = R.mipmap.goicon;
    //listview ����������
    private OnScrollListener mOnScrollListener;
    private LayoutInflater mInflater;
    private RelativeLayout mRefreshView;
    //����ˢ��ʱ���ֵĿؼ�
    private TextView mRefreshViewText;
    private ImageView mRefreshViewImage;
    private ProgressBar mRefreshViewProgress;
    private TextView mRefreshViewLastUpdated;
    // ��ͷ����Ч��
    //��Ϊ���µļ�ͷ
    private RotateAnimation mFlipAnimation;
    //��Ϊ����ļ�ͷ
    private RotateAnimation mReverseFlipAnimation;
    //�Ƿ񷴵�
    private boolean mBounceHack;

    public PullToRefreshListView(Context context) {
        super(context);
        init(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    /** 
     * ��ʼ���ؼ��ͼ�ͷ����������ֱ���ڴ����г�ʼ������������ͨ��xml�� 
     */ 

    private void init(Context context) {
        // Load all of the animations we need in code rather than through XML
    	
    	//��һ������fromDegreesΪ������ʼʱ����ת�Ƕ�    
    	//�ڶ�������toDegreesΪ������ת���ĽǶ�   
    	//����������pivotXTypeΪ������X����������λ������  
    	//���ĸ�����pivotXValueΪ��������������X����Ŀ�ʼλ��
    	//���������pivotXTypeΪ������Y����������λ������   
    	//����������pivotYValueΪ��������������Y����Ŀ�ʼλ��   	
        mFlipAnimation = new RotateAnimation(0, -180,RotateAnimation.RELATIVE_TO_SELF, 
        		0.5f,RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mFlipAnimation.setInterpolator(new LinearInterpolator());
        mFlipAnimation.setDuration(250);
        mFlipAnimation.setFillAfter(true);
                     
        mReverseFlipAnimation = new RotateAnimation(-180, 0,RotateAnimation.RELATIVE_TO_SELF, 0.5f,RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
        mReverseFlipAnimation.setDuration(250);
        mReverseFlipAnimation.setFillAfter(true);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mRefreshView = (RelativeLayout) mInflater.inflate(R.layout.pull_to_refresh_header, this, false);
		mRefreshViewText =(TextView) mRefreshView.findViewById(R.id.pull_to_refresh_text);
        mRefreshViewImage =(ImageView) mRefreshView.findViewById(R.id.pull_to_refresh_image);
        mRefreshViewProgress =(ProgressBar) mRefreshView.findViewById(R.id.pull_to_refresh_progress);
        mRefreshViewLastUpdated =(TextView) mRefreshView.findViewById(R.id.pull_to_refresh_updated_at);

        mRefreshViewImage.setMinimumHeight(50);
        mRefreshView.setOnClickListener(new OnClickRefreshListener());
        mRefreshOriginalTopPadding = mRefreshView.getPaddingTop();
        mRefreshState = TAP_TO_REFRESH;
      //Ϊlistviewͷ������һ��view  
        addHeaderView(mRefreshView);
        super.setOnScrollListener(this);
        measureView(mRefreshView);
        mRefreshViewHeight = mRefreshView.getMeasuredHeight();  
    }

//    @Override
//    protected void onAttachedToWindow() {
//        setSelection(1);
//    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        setSelection(1);
    }

    /**
     * Set the listener that will receive notifications every time the list scrolls.
     * @param l The scroll listener. 
     */
    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mOnScrollListener = l;
    }

    /**
     * Register a callback to be invoked when this list should be refreshed.
     * @param onRefreshListener The callback to run.
     */
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
    }

    /**
     * Set a text to represent when the list was last updated. 
     * @param lastUpdated Last updated at.
     */
    public void setLastUpdated(CharSequence lastUpdated) {
        if (lastUpdated != null) {
            mRefreshViewLastUpdated.setVisibility(View.VISIBLE);
            mRefreshViewLastUpdated.setText(lastUpdated);
        } else {
            mRefreshViewLastUpdated.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	//��ǰ��ָ��Yֵ
        final int y = (int) event.getY();
        mBounceHack = false;   
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
            	//����ֱ����������Ϊ����״̬
                if (!isVerticalScrollBarEnabled()) {
                    setVerticalScrollBarEnabled(true);
                }
                if (getFirstVisiblePosition() == 0 && mRefreshState != REFRESHING) {
                	// �϶�����ﵽˢ����Ҫ
                    if ((mRefreshView.getBottom() >= mRefreshViewHeight
                            || mRefreshView.getTop() >= 0)
                            && mRefreshState == RELEASE_TO_REFRESH) {  
                    	// ��״̬����Ϊ����ˢ��
                        // Initiate the refresh
                        mRefreshState = REFRESHING; //����������Ϊ������ˢ��
                        // ׼��ˢ��
                        prepareForRefresh();  
                        // ˢ��  
                        onRefresh();  
                    } else if (mRefreshView.getBottom() < mRefreshViewHeight
                            || mRefreshView.getTop() <= 0) {
                        // Abort refresh and scroll down below the refresh view
                    	//ֹͣˢ�£����ҹ�����ͷ��ˢ����ͼ����һ����ͼ
                        resetHeader();
                        setSelection(1); //��λ�ڵڶ����б���
                    }
                }
                break;
            case MotionEvent.ACTION_DOWN:
            	// ��ð���y��λ�� 
                mLastMotionY = y;  
                break;            
            case MotionEvent.ACTION_MOVE:
            	//����ͷ��ͼ��toppadding ����
                applyHeaderPadding(event);
                break;
        }
        return super.onTouchEvent(event);
    }
    // ���header���� 
    private void applyHeaderPadding(MotionEvent ev) {
    	//��ȡ�ۻ��Ķ�����
        int pointerCount = ev.getHistorySize();
        for (int p = 0; p < pointerCount; p++) {
        	//������ͷŽ�Ҫˢ��״̬
            if (mRefreshState == RELEASE_TO_REFRESH) {   
                if (isVerticalFadingEdgeEnabled()) {   
                    setVerticalScrollBarEnabled(false);
                }
                //��ʷ�ۻ��ĸ߶�
                int historicalY = (int) ev.getHistoricalY(p);
                // Calculate the padding to apply, we divide by 1.7 to
                // simulate a more resistant effect during pull.
                // ��������ı߾࣬����1.7ʹ������Ч������
                int topPadding = (int) (((historicalY - mLastMotionY)- mRefreshViewHeight) / 1.7);
                mRefreshView.setPadding(
                        mRefreshView.getPaddingLeft(),
                        topPadding,
                        mRefreshView.getPaddingRight(),
                        mRefreshView.getPaddingBottom());
            }
        }
    }

    /** 
     * Sets the header padding back to original size.
     * ��head�ı߾�����Ϊ��ʼ����ֵ 
     */ 
    private void resetHeaderPadding() {
        mRefreshView.setPadding(
                mRefreshView.getPaddingLeft(),
                mRefreshOriginalTopPadding,
                mRefreshView.getPaddingRight(),
                mRefreshView.getPaddingBottom());
    }    
    /** 
     * Resets the header to the original state.
     * ����headerΪ֮ǰ��״̬ 
     */ 
    private void resetHeader() {
        if (mRefreshState != TAP_TO_REFRESH) {
            mRefreshState = TAP_TO_REFRESH; 
            resetHeaderPadding();
            // ��ˢ��ͼ�껻�ɼ�ͷ 
            // Set refresh view text to the pull label
            mRefreshViewText.setText(R.string.pull_to_refresh_tap_label);
            // Replace refresh drawable with arrow drawable
            // ������� 
            mRefreshViewImage.setImageResource(REFRESHICON);
            // Clear the full rotation animation
            mRefreshViewImage.clearAnimation();
            // Hide progress bar and arrow.
            // ����ͼ��ͽ����� 
            mRefreshViewImage.setVisibility(View.GONE);
            mRefreshViewProgress.setVisibility(View.GONE);
        }
    }
    //������ͼ�ĸ߶�
    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0,0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {  
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);   
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);     
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
        // When the refresh view is completely visible, change the text to say
        // "Release to refresh..." and flip the arrow drawable.
    	// ��refreshview��ȫ�ɼ�ʱ����������Ϊ�ɿ�ˢ�£�ͬʱ��ת��ͷ 
    	//����ǽӴ�����״̬,���Ҳ�������ˢ�µ�״̬
        if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL&& mRefreshState != REFRESHING) {
            if (firstVisibleItem == 0) {  
            	//�����ʾ�����˵�һ���б����ʾˢ��ͼƬ
                mRefreshViewImage.setVisibility(View.VISIBLE);
                //���������listiview,����ʾ����ˢ�¶���
                if ((mRefreshView.getBottom() >= mRefreshViewHeight + 20|| mRefreshView.getTop() >= 0)
                        && mRefreshState != RELEASE_TO_REFRESH) { 
                    mRefreshViewText.setText(R.string.pull_to_refresh_release_label);
                    mRefreshViewImage.clearAnimation();
                    mRefreshViewImage.startAnimation(mFlipAnimation);
                    mRefreshState = RELEASE_TO_REFRESH;   
                  //����������벻������ع�ԭ����״̬
                } else if (mRefreshView.getBottom() < mRefreshViewHeight + 20
                        && mRefreshState != PULL_TO_REFRESH) {    
                    mRefreshViewText.setText(R.string.pull_to_refresh_pull_label);
                    if (mRefreshState != TAP_TO_REFRESH) {
                        mRefreshViewImage.clearAnimation();
                        mRefreshViewImage.startAnimation(mReverseFlipAnimation);
                    }
                    mRefreshState = PULL_TO_REFRESH;
                }
            } else {   
                mRefreshViewImage.setVisibility(View.GONE);  
                resetHeader();   
            }
          //����ǹ���״̬+ ��һ����ͼ�Ѿ���ʾ+ ����ˢ��״̬
        } else if (mCurrentScrollState == SCROLL_STATE_FLING  && firstVisibleItem == 0
                && mRefreshState != REFRESHING) {
            setSelection(1);
            mBounceHack = true;   
        } else if (mBounceHack && mCurrentScrollState == SCROLL_STATE_FLING) {
            setSelection(1);       
        }
        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(view, firstVisibleItem,visibleItemCount, totalItemCount);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        mCurrentScrollState = scrollState;
        if (mCurrentScrollState == SCROLL_STATE_IDLE) {  
            mBounceHack = false;
        }
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChanged(view, scrollState);
        }
    }
    
    public void prepareForRefresh() {
        resetHeaderPadding();   // �ָ�header�ı߾� 
        mRefreshViewImage.setVisibility(View.GONE);
        // We need this hack, otherwise it will keep the previous drawable.
     // ע����ϣ�������Ȼ��ʾ֮ǰ��ͼƬ  
        mRefreshViewImage.setImageDrawable(null);
        mRefreshViewProgress.setVisibility(View.VISIBLE);
        // Set refresh view text to the refreshing label
       mRefreshViewText.setText(R.string.pull_to_refresh_refreshing_label);
        mRefreshState = REFRESHING;
    }
    public void onRefresh() {
        if (mOnRefreshListener != null) {
            mOnRefreshListener.onRefresh();
        }
    }
    /**
     * 
     * @param lastUpdated Last updated at.
     */
    /** 
     * Resets the list to a normal state after a refresh.
     * ����listviewΪ��ͨ��listview
     * @param lastUpdated 
     * Last updated at. 
     */  
    
    public void onRefreshComplete(CharSequence lastUpdated) {
        setLastUpdated(lastUpdated);
        onRefreshComplete(); 
    }
    /** 
     * Resets the list to a normal state after a refresh.
     * ����listviewΪ��ͨ��listview��
     */
    public void onRefreshComplete() {        
        resetHeader();
        // If refresh view is visible when loading completes, scroll down to
        // the next item.
        if (mRefreshView.getBottom() > 0) {
            invalidateViews();  //�ػ���ͼ
            setSelection(1);
        }
    }
    /**
     * Invoked when the refresh view is clicked on. This is mainly used when
     * there's only a few items in the list and it's not possible to drag the
     * list.
     */
    private class OnClickRefreshListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (mRefreshState != REFRESHING) {
                prepareForRefresh();  
                onRefresh(); 
            }
        }
    }
    public interface OnRefreshListener {       
        public void onRefresh();
    }
}