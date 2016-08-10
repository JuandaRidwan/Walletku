package com.digipay.digipay.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digipay.digipay.R;
import com.digipay.digipay.functions.SessionSearchBook;
import com.digipay.digipay.models.DetailOrdersAirplaneTicket;

import java.util.List;

public class DetailOrdersAirplaneTicketAdapter extends RecyclerView.Adapter<DetailOrdersAirplaneTicketAdapter.MyViewHolder> {

    private List<DetailOrdersAirplaneTicket> detailOrdersList;
    protected Context mContext;
    private SessionSearchBook mSessionBook;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTittle, tvFirstName, tvFromDepart, tvTodepart, tvBagasiDepart, tvFromReturn, tvToReturn, tvBagasiReturn;
        public LinearLayout llParent;

        public MyViewHolder(View view) {
            super(view);
            llParent = (LinearLayout) view.findViewById(R.id.llParrent);
            tvTittle = (TextView) view.findViewById(R.id.tvTittle);
            tvFirstName = (TextView) view.findViewById(R.id.tvFirstName);
            tvFromDepart = (TextView) view.findViewById(R.id.tvFromDepart);
            tvFromReturn = (TextView) view.findViewById(R.id.tvFromReturn);
            tvTodepart = (TextView) view.findViewById(R.id.tvToDepart);
            tvToReturn = (TextView) view.findViewById(R.id.tvToReturn);
            tvBagasiDepart = (TextView) view.findViewById(R.id.tvBagasiDepart);
            tvBagasiReturn = (TextView) view.findViewById(R.id.tvBagasiReturn);

            mSessionBook = new SessionSearchBook(mContext);

        }
    }

    public DetailOrdersAirplaneTicketAdapter(Context context, List<DetailOrdersAirplaneTicket> detailOrdersList) {
        this.mContext = context;
        this.detailOrdersList = detailOrdersList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_order_airplane_ticket, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DetailOrdersAirplaneTicket detailOrders = detailOrdersList.get(position);

        holder.tvTittle.setText(detailOrders.getTittle() + ".");
        holder.tvFirstName.setText(detailOrders.getFirstName());
        holder.tvFromDepart.setText("Bagasi " + mSessionBook.getFromDepart() + " - ");
        holder.tvTodepart.setText(mSessionBook.getToDepart());
    }

    @Override
    public int getItemCount() {
        return detailOrdersList.size();
    }
}

