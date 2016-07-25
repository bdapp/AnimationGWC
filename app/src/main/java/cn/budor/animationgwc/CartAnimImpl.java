package cn.budor.animationgwc;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import static java.lang.Integer.MAX_VALUE;

/**
 * @Info 移动到购物车动画
 * @Auth GuoJiang
 * @Time 16-7-25 下午4:17
 * @Ver
 */
public class CartAnimImpl implements ICartAnim {
    private Context mContext;
    private Handler mHandler;
    private ICartView cartView;

    //动画层(在窗口最上层)
    private ViewGroup anim_mask_layout;

    public CartAnimImpl(Context mContext, ICartView cartView) {
        this.mContext = mContext;
        this.cartView = cartView;
        mHandler = new Handler(Looper.getMainLooper());
    }



    /**
     * 创建动画层
     * @return
     */
    public ViewGroup createAnimLayout(Context mContext){
        ViewGroup rootView = (ViewGroup)((Activity)mContext).getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setId(MAX_VALUE);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    /**
     * 添加动画视图到动画层
      * @param view
     * @param location
     * @return
     */
    public View addViewToAnimLayout(View view, int[] location){
        int x = location[0];
        int y = location[1];

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);
        return  view;
    }

    /**
     * 动画实现
     * @param v
     * @param toView
     * @param start_location
     */
    public void setTranslateAnim(final View v, View toView, int[] start_location){
        if (anim_mask_layout == null){
            anim_mask_layout = createAnimLayout(mContext);
            anim_mask_layout.addView(v);

            final View view = addViewToAnimLayout(v, start_location);
            int[] end_location = new int[2];

            //获取动画结束UI的位置
            toView.getLocationInWindow(end_location);
            int endX = end_location[0];
            int endY = end_location[1] - start_location[1];

            TranslateAnimation translateAnimationX = new TranslateAnimation(0, endX, 0, 0);
            translateAnimationX.setInterpolator(new LinearInterpolator());
            translateAnimationX.setRepeatCount(0);
            translateAnimationX.setFillAfter(true);

            TranslateAnimation translateAnimationY = new TranslateAnimation(0,0,0, endY);
            translateAnimationY.setInterpolator(new AccelerateInterpolator());
            translateAnimationY.setRepeatCount(0);
            translateAnimationY.setFillAfter(true);

            AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0.2f);

            ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0.5f, 1, 0.5f,
                    0, endX, 0, endY);
            scaleAnimation.setRepeatCount(0);
            scaleAnimation.setFillAfter(true);

            AnimationSet set = new AnimationSet(false);
            set.setFillAfter(false);
            set.addAnimation(translateAnimationY);
            set.addAnimation(translateAnimationX);
            set.addAnimation(scaleAnimation);
            set.addAnimation(alphaAnimation);
            set.setDuration(500);
            view.startAnimation(set);

            set.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    v.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    v.setVisibility(View.GONE);
                    anim_mask_layout.removeView(view);
                    anim_mask_layout = null;

                    // 发送通知前台数据更新
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            cartView.setChangeNum();
                        }
                    });
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }
    }


}
