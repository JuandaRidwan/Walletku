package com.digipay.digipay.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.digipay.digipay.R;
import com.digipay.digipay.models.PayHistory;

import java.util.List;

/**
 * Created by Ridone on 1/27/2016.
 */
public class PayHistoryAdapter extends RecyclerView.Adapter<PayHistoryAdapter.PayHistoryHolder> {

    private List<PayHistory> payList;
    private Context context;


    public PayHistoryAdapter(Context context, List<PayHistory> payList) {
        this.payList = payList;
        this.context = context;
    }


    public class PayHistoryHolder extends RecyclerView.ViewHolder {
        public TextView serialNumber, provider, amount, status, message, msisdn, nominal;
        public ImageView imgIcon;
        public CardView cv;

        public PayHistoryHolder(View view) {
            super(view);
            cv = (CardView) itemView.findViewById(R.id.cv);
            imgIcon = (ImageView) view.findViewById(R.id.imgIcon);
            serialNumber = (TextView) view.findViewById(R.id.tvSnHistory);
            nominal = (TextView) view.findViewById(R.id.tvNominal);
            provider = (TextView) view.findViewById(R.id.tvProvider);
            amount = (TextView) view.findViewById(R.id.tvAmount);
            status = (TextView) view.findViewById(R.id.tvStatus);
            message = (TextView) view.findViewById(R.id.tvMessageHistory);
            msisdn = (TextView) view.findViewById(R.id.tvNomorTujuan);
        }
    }

    public PayHistoryAdapter(List<PayHistory> payList) {
        this.payList = payList;
    }

    @Override
    public PayHistoryHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pay_history_list, viewGroup, false);
        PayHistoryHolder pvh = new PayHistoryHolder(v);


        return pvh;
    }

    @Override
    public void onBindViewHolder(PayHistoryHolder holder, int position) {
        try {
            PayHistory pay = payList.get(position);

            String paymentTipe = pay.getPaymentTipeHistory();
            if (paymentTipe.equals("topup")) {
                holder.imgIcon.setImageResource(R.drawable.ic_bayar_pulsa);
                if (pay.getMsisdnHistory().equals("null")) {
                    holder.msisdn.setText("-");
                } else {
                    holder.msisdn.setText("Nomor Tujuan: " + pay.getMsisdnHistory());
                }
            }
            if (paymentTipe.equals("pln")) {
                holder.imgIcon.setImageResource(R.drawable.ic_bayar_listrik);
                if (pay.getMsisdnHistory().equals("null")) {
                    holder.msisdn.setText("-");
                } else {
                    holder.msisdn.setText("Nomor Meter: " + pay.getMsisdnHistory());
                }
            }

            if (pay.getProvider().equals("null")) {
                holder.provider.setText("-");
            } else {
                holder.provider.setText(pay.getProvider().toUpperCase());
            }

            if (pay.getAmount().equals("null")) {
                holder.amount.setText("-");
                holder.nominal.setText("-");
            } else {
                holder.amount.setText("Harga Rp." + pay.getAmount() + ",-");
                holder.nominal.setText(" " + pay.getNominal());
            }
            if (pay.getStatus().equals("null")) {
                holder.status.setText("failed");
            } else {
                holder.status.setText(pay.getStatus());
            }

            if (pay.getSerialNumber().equals("null")) {
                holder.serialNumber.setText("-");
            } else {
                holder.serialNumber.setText(pay.getSerialNumber());
            }
            if (pay.getMessage().equals("")) {
                holder.message.setText("-");
            } else {
                holder.message.setText(pay.getDate());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return payList.size();
//        Log.e("hasil", payList.size() + " list");
//        return payList == null ? 0 : payList.size();

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
