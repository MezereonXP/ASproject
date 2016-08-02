package com.checkfile.checkfile.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ListView;


public class HeaderListView extends ListView {

    // �Ŵ�� ImageView
    private ImageView headerIV;
    private int height;

    public void setHeaderIV(ImageView headerIV) {
        this.headerIV = headerIV;
    }

    public HeaderListView(Context context) {
        super(context);
    }

    public HeaderListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * �� view ������ activity �����ʱ��ص�
     * @param hasWindowFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);

        if (hasWindowFocus){
            height = this.headerIV.getHeight();
        }
    }

    /**
     *  ��listview ������������ʱ�򣬻�Ҫ��������Ҫ���Ϲ�������ô��ʱ�ͻ���ø÷���
     * @param deltaX
     * @param deltaY
     * @param scrollX
     * @param scrollY
     * @param scrollRangeX
     * @param scrollRangeY
     * @param maxOverScrollX
     * @param maxOverScrollY
     * @param isTouchEvent
     * @return
     */
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

        // ������ͷ��ʱ��ص��÷���
        // ���� imageview �ĸ߶�������------�Ӷ��ﵽ����ͼƬ�Ŵ��Ч��
        boolean isCollpse = resizeOverScrollBy(deltaY);

        return isCollpse == false ? isCollpse: super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    private boolean resizeOverScrollBy(int deltaY) {

        if (deltaY < 0){
            if (headerIV != null){
                // ��������������ʱ�򣬻�Ҫ���ϻ������͸ı� imageview �ĸ߶�
                headerIV.getLayoutParams().height = headerIV.getHeight() - deltaY;
                headerIV.requestLayout();
            }
        } else {
            if (headerIV != null){
                headerIV.getLayoutParams().height = headerIV.getHeight() - deltaY;
                headerIV.requestLayout();
            }
        }
        return false;
    }

    /**
     * ��listview û�л������ײ��򶥲�ʱ����
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        // ��ԭͼƬ������imageview �ĳ�ʼ���߶�
        // ��ȡimageview �ĸ�������RelativeLayout��
        ViewParent parent = this.headerIV.getParent();
        if (parent != null){
            View rootView = (View)parent;
            if (rootView.getTop() < 0 && headerIV .getHeight() > height){

                headerIV.getLayoutParams().height = headerIV.getHeight() + rootView.getTop();

                // ���°ڷ��ӿؼ�
                rootView.layout(rootView.getLeft(), 0, rootView.getRight(), rootView.getBottom());

                // ���»���
                headerIV.requestLayout();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // ��������̧��
        if (ev.getAction() == MotionEvent.ACTION_UP){
            MyAnimation animation = new MyAnimation(headerIV, height);
            animation.setDuration(300);
            this.headerIV.startAnimation(animation);
        }
        return super.onTouchEvent(ev);
    }

    public class MyAnimation extends Animation{

        private ImageView imageView;
        // imageview ��ԭʼ�߶�
        private int targetHeight;
        // ��ǰ imageview �ĸ߶�
        private int currentHeight;
        // �߶Ȳ� ��ǰ�ļ�ȥԭʼ��
        private int extraHeight;

        public MyAnimation(ImageView imageView, int targetHeight){
            this.imageView = imageView;
            this.targetHeight = targetHeight;
            this.currentHeight = imageView.getHeight();
            this.extraHeight = this.currentHeight - this.targetHeight;
        }

        /**
         *  �������ڲ��ϵ�ִ�е�ʱ��ص��÷��������Ǽ�������ִ�еĹ��̣�
         * @param interpolatedTime ֵ�÷�Χ 0.0 �� 1.0��ʱ��仯����
         * @param t
         */
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);

            this.imageView.getLayoutParams().height = (int)(this.currentHeight
                    - extraHeight * interpolatedTime);

            this.imageView.requestLayout();
        }
    }
}
