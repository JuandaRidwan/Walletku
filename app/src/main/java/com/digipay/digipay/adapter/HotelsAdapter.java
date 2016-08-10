package com.digipay.digipay.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.digipay.digipay.R;
import com.digipay.digipay.activity.HotelBookDetailActivity;
import com.digipay.digipay.functions.SessionManager;
import com.digipay.digipay.models.Hotels;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HotelsAdapter extends RecyclerView.Adapter<HotelsAdapter.ViewHolder> {
    private List<Hotels> hotelsList;
    private Context mContext;
    private SessionManager mSession;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHotelName, tvHotelAddress, tvHotelPrice;
        ImageView imgHotel;
        Button btnSelect, btnUnselected;

        public ViewHolder(View view) {
            super(view);
            tvHotelName = (TextView) view.findViewById(R.id.tvHotelName);
            tvHotelAddress = (TextView) view.findViewById(R.id.tvHotelAddress);
            tvHotelPrice = (TextView) view.findViewById(R.id.tvHotelPrice);
            imgHotel = (ImageView) view.findViewById(R.id.imgHotel);
            btnSelect = (Button) view.findViewById(R.id.btnSelect);
            btnUnselected = (Button) view.findViewById(R.id.btnUnselected);
            btnUnselected.setVisibility(View.GONE);

            mSession = new SessionManager(mContext);

            btnSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Hotels hotels = hotelsList.get(pos);
                    Intent i = new Intent(mContext, HotelBookDetailActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                    mSession.setHotelId(String.valueOf(hotels.getHotelId()));
                    mSession.setHotelName(String.valueOf(hotels.getName()));
                    mSession.setHotelImgLarge(String.valueOf(hotels.getLargeThumbnailURL()));
                }
            });
        }
    }

    public HotelsAdapter(Context context, List<Hotels> imgHotelsList) {
        this.mContext = context;
        this.hotelsList = imgHotelsList;
    }

    @Override
    public HotelsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_hotel_result, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Hotels hotels = hotelsList.get(position);

        viewHolder.tvHotelName.setText(hotels.getName());
        viewHolder.tvHotelAddress.setText(hotels.getAddress() + ", " + hotels.getCityName());
        viewHolder.tvHotelPrice.setText("Rp." + hotels.getPrice() + ",00");

        Picasso.with(mContext)
                .load(hotels.getThumbNailUrl())
                .placeholder(R.drawable.img_not_found)
                .resize(120, 120)
                .into(viewHolder.imgHotel);

    }

    @Override
    public int getItemCount() {
        return hotelsList.size();
    }
}
