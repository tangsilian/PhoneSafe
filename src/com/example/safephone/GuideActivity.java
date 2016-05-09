package com.example.safephone;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;


public class GuideActivity extends Activity {
	 /** ViewPager��ImageView������ */
    private List<ImageView> imageViewContainer = null;



    /** ��һ����ѡ�е�СԲ���������Ĭ��ֵΪ0 */
    private int preDotPosition = 0;

    /** Banner������������ */
    private String[] bannerTextDescArray = { 
            "���������ף��ҾͲ��ܵ���", 
            "�����ֻ����ˣ��ٳ������ϸ������˴�ϳ�",
            "���ر�����Ӱ�������", 
    };

    /** Banner�����߳��Ƿ����ٵı�־��Ĭ�ϲ����� */
    private boolean isStop = false;

    /** Banner���л���һ��page�ļ��ʱ�� */
    private long scrollTimeOffset = 5000;

    private ViewPager viewPager;

    /** Banner������������ʾ�ؼ� */
    private TextView tvBannerTextDesc;

    /** СԲ��ĸ��ؼ� */
    private LinearLayout llDotGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.guidelayout);
        initView();
        startBannerScrollThread();
    }

    /**
     * ����Banner�����߳�
     */
    private void startBannerScrollThread() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (!isStop) {
                    //ÿ�������ӷ�һ����Ϣ�����̣߳�����viewpager����
                    SystemClock.sleep(scrollTimeOffset);

                    runOnUiThread(new Runnable() {
                        public void run() {
                            int newindex = viewPager.getCurrentItem() + 1;
                            viewPager.setCurrentItem(newindex);
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        // �����߳�
        isStop = true;
        super.onDestroy();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        llDotGroup = (LinearLayout) findViewById(R.id.ll_dot_group);
        tvBannerTextDesc = (TextView) findViewById(R.id.tv_banner_text_desc);

        imageViewContainer = new ArrayList<ImageView>();
        int[] imageIDs = new int[] { 
                R.drawable.guide_1, 
                R.drawable.guide_2, 
                R.drawable.guide_3, 
        };

        ImageView imageView = null;
        View dot = null;
        LayoutParams params = null;
        for (int id : imageIDs) {
            imageView = new ImageView(this);
            imageView.setBackgroundResource(id);
            imageViewContainer.add(imageView);

            // ÿѭ��һ������һ���㵽���в�����
            dot = new View(this);
            dot.setBackgroundResource(R.drawable.dot_bg_selector);
            params = new LayoutParams(5, 5);
            params.leftMargin = 10;
            dot.setEnabled(false);
            dot.setLayoutParams(params);
            llDotGroup.addView(dot); // �����Բ���������"��"
        }

        viewPager.setAdapter(new BannerAdapter());
        viewPager.setOnPageChangeListener(new BannerPageChangeListener());

        // ѡ�е�һ��ͼƬ����������
        tvBannerTextDesc.setText(bannerTextDescArray[0]);
        llDotGroup.getChildAt(0).setEnabled(true);
        viewPager.setCurrentItem(0);
    }

    /**
     * ViewPager��������
     */
    private class BannerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViewContainer.get(position % imageViewContainer.size()));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = imageViewContainer.get(position % imageViewContainer.size());

            // Ϊÿһ��page���ӵ���¼�
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Page �������", Toast.LENGTH_SHORT).show();
                }

            });

            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    /**
     * Banner��Page�л�������
     */
    private class BannerPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // Nothing to do
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // Nothing to do
        }

        @Override
        public void onPageSelected(int position) {
            // ȡ�����������õ��µ�page������
            int newPositon = position % imageViewContainer.size();
            // ������������ͼƬ������
            tvBannerTextDesc.setText(bannerTextDescArray[newPositon]);
            // ����һ��������Ϊ��ѡ��
            llDotGroup.getChildAt(preDotPosition).setEnabled(false);
            // �������������Ǹ��㱻ѡ��
            llDotGroup.getChildAt(newPositon).setEnabled(true);
            // ��������ֵ����һ��������λ��
            preDotPosition = newPositon;
        }

    }

}