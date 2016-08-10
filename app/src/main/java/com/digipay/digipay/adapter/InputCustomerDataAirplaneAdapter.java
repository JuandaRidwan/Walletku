package com.digipay.digipay.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digipay.digipay.R;
import com.digipay.digipay.functions.SessionSearchBook;
import com.digipay.digipay.models.InputCustomerCount;

import java.util.List;

public class InputCustomerDataAirplaneAdapter extends RecyclerView.Adapter<InputCustomerDataAirplaneAdapter.MyViewHolder> {

    private List<InputCustomerCount> customerCountList;
    protected Context mContext;
    private SessionSearchBook mSessionSearchBook;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvJmlPenumpang, tvTittle, tvFirstName, tvLastName, tvJenisIdentitas, tvNomorIdentitas, tvBirthdate;
        public CardView cv;

        public MyViewHolder(View view) {
            super(view);
            cv = (CardView) view.findViewById(R.id.cv);
            tvJmlPenumpang = (TextView) view.findViewById(R.id.tvJmlPenumpang);
            tvTittle = (TextView) view.findViewById(R.id.tvTittle);
            tvFirstName = (TextView) view.findViewById(R.id.tvFirstName);
            tvLastName = (TextView) view.findViewById(R.id.tvLastName);
            tvJenisIdentitas = (TextView) view.findViewById(R.id.tvJenisIdentitas);
            tvNomorIdentitas = (TextView) view.findViewById(R.id.tvNomorIdentitas);
            tvBirthdate = (TextView) view.findViewById(R.id.tvBirthdate);

            mSessionSearchBook = new SessionSearchBook(mContext);

        }
    }

    public InputCustomerDataAirplaneAdapter(Context context, List<InputCustomerCount> customerCountList) {
        this.mContext = context;
        this.customerCountList = customerCountList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_data_airplane_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        InputCustomerCount customerCount = customerCountList.get(position);

        holder.tvJmlPenumpang.setText("Penumpang " + customerCount.getCount());
        holder.tvTittle.setText(customerCount.getTittle());
        holder.tvFirstName.setText(customerCount.getFirtsName());
        holder.tvLastName.setText(customerCount.getLastName());
        holder.tvJenisIdentitas.setText(customerCount.getJenisIdentitas());
        holder.tvNomorIdentitas.setText(customerCount.getNomorIdentitas());
        holder.tvBirthdate.setText(customerCount.getBirthdate());
    }

    @Override
    public int getItemCount() {
        return customerCountList.size();
    }
}
