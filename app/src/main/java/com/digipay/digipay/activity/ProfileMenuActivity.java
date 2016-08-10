package com.digipay.digipay.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.digipay.digipay.R;
import com.digipay.digipay.fragments.MyWalletFragment;
import com.digipay.digipay.fragments.PayFragment;
import com.digipay.digipay.fragments.PayHistoryFragment;

import java.util.ArrayList;
import java.util.List;

public class ProfileMenuActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        TextView tabProfile = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabProfile.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_pay_history_tab_s, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabProfile);

        TextView tabPay = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabPay.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_pay_history_tab_s, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabPay);

        TextView tabMyWallet = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabMyWallet.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_pay_history_tab_s, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabMyWallet);

        TextView tabPayHistory = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabPayHistory.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_pay_history_tab_s, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabPayHistory);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PayFragment(), "Pay");
        adapter.addFragment(new MyWalletFragment(), "My Wallet");
        adapter.addFragment(new PayHistoryFragment(), "Pay History");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
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
    }
}
