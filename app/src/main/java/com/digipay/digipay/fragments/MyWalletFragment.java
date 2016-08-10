package com.digipay.digipay.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.digipay.digipay.R;
import com.digipay.digipay.functions.SessionManager;

public class MyWalletFragment extends Fragment {

    private SessionManager mSession;
    private String statusPremium;
    private String totalSaldo, totalBonus, totalPoint;
    private Button btnTukarBonus, btnTukarPoint;
    private TextView tvTotalSaldo, tvTotalBonus, tvTitleBonus, tvTitlePoint, tvTotalPoint;

    public MyWalletFragment() {
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
        View view = inflater.inflate(R.layout.fragment_my_wallet, container, false);
        mSession = new SessionManager(getActivity());
        statusPremium = mSession.getPremium();
        totalSaldo = mSession.getTotalBalance();
        totalBonus = mSession.getTotalBonus();
        totalPoint = mSession.getTotalPoint();

        tvTotalSaldo = (TextView) view.findViewById(R.id.tvTotalSaldo);
        tvTotalBonus = (TextView) view.findViewById(R.id.tvTotalBonus);
        tvTitleBonus = (TextView) view.findViewById(R.id.tvTitleBonus);
        tvTitlePoint = (TextView) view.findViewById(R.id.tvTitlePoint);
        tvTotalPoint = (TextView) view.findViewById(R.id.tvTotalPoint);
        btnTukarBonus = (Button) view.findViewById(R.id.btnTukarBonus);
        btnTukarPoint = (Button) view.findViewById(R.id.btnTukarPoint);
//        btnTukarPoint.setVisibility(View.GONE);
        checkUser();

        btnTukarBonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalBonus.length() >= 5) {
                    // tukarkan bonus
                } else {
                    Toast.makeText(getActivity(), "Bonus tidak bisa ditukarkan, bonus anda kurang dari 5", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnTukarPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalPoint.length() >= 5) {
                    // tukarkan point
                } else {
                    Toast.makeText(getActivity(), "Point tidak bisa ditukarkan, point anda kurang dari 5", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void checkUser() {
        if (mSession != null) {
            statusPremium = mSession.getPremium();
            totalSaldo = mSession.getTotalBalance();
            tvTotalSaldo.setText("Rp." + totalSaldo);
            tvTotalPoint.setText(mSession.getTotalPoint());
            tvTotalBonus.setText("Rp." + mSession.getTotalBonus());
            if (statusPremium.equals("false")) {
                tvTotalBonus.setVisibility(View.VISIBLE);
                tvTitleBonus.setVisibility(View.VISIBLE);
                btnTukarBonus.setVisibility(View.VISIBLE);
                btnTukarPoint.setVisibility(View.GONE);
                tvTotalPoint.setVisibility(View.GONE);
                tvTitlePoint.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkUser();
    }
}
