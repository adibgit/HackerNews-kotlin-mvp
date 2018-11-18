package com.adibsurani.hackernews.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.adibsurani.hackernews.R;

import java.util.ArrayList;
import java.util.List;

import static com.adibsurani.hackernews.helper.Constants.COMMENT;
import static com.adibsurani.hackernews.helper.Constants.NEWS;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private Context context;

    public ViewPagerAdapter(Context context, FragmentManager manager) {
        super(manager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    public void initClickTab(int Tab,
                             ImageView image_news,
                             ImageView image_comment,
                             FrameLayout layout_news,
                             ViewPager viewpager) {
        switch (Tab) {
            case NEWS:
                image_news.setColorFilter(context.getResources().getColor(R.color.colorPrimary));
                image_comment.setColorFilter(context.getResources().getColor(R.color.colorBlack));

                layout_news.setVisibility(View.VISIBLE);
                viewpager.setVisibility(View.GONE);
                break;
            case COMMENT:
                image_news.setColorFilter(context.getResources().getColor(R.color.colorBlack));
                image_comment.setColorFilter(context.getResources().getColor(R.color.colorPrimary));

                layout_news.setVisibility(View.GONE);
                viewpager.setVisibility(View.VISIBLE);
                break;
        }

    }

}
