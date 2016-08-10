package com.digipay.digipay.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digipay.digipay.R;

public class TabFragment extends Fragment {
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 3;

    private int mPage;

    public TabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View x = inflater.inflate(R.layout.tab_layout, null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);

        viewPager.setAdapter(new MyAdapter(getChildFragmentManager(), getActivity()));

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return x;
    }

    class MyAdapter extends FragmentPagerAdapter {
        private Context context;
        //        private int tabIcons[] = {R.drawable.ic_pay_history_tab_s, R.drawable.ic_pay_history_tab_s,
//                R.drawable.ic_pay_history_tab_s, R.drawable.ic_pay_history_tab_s
//        };
        private int tabIcons[] = {R.mipmap.ic_launcher, R.mipmap.ic_launcher,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher
        };

        public MyAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

//        public MyAdapter(FragmentManager fm) {
//            super(fm);
//        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new PayFragment();
                case 1:
                    return new MyWalletFragment();
                case 2:
                    return new PayHistoryFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return int_items;

        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Pay";
                case 1:
                    return "My Wallet";
                case 2:
                    return "Pay History";
            }
            return null;
//            Drawable image = ContextCompat.getDrawable(context, tabIcons[position]);
//            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
//            SpannableString sb = new SpannableString(" ");
//            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
//            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            return sb;

        }
    }

}
