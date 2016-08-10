package com.digipay.digipay.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.digipay.digipay.R;
import com.digipay.digipay.functions.SessionManager;
import com.digipay.digipay.models.HotelsDetail;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HotelsAdapterDetail extends RecyclerView.Adapter<HotelsAdapterDetail.ViewHolder> {
    private List<HotelsDetail> hotelsListDetail;
    private Context mContext;
    private SessionManager mSession;
    private boolean isSelected;

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

            isSelected = false;
            mSession = new SessionManager(mContext);

            btnSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isSelected) {
                        int pos = getAdapterPosition();
                        HotelsDetail hotels = hotelsListDetail.get(pos);
                        mSession.setRoomCodeDetail(hotels.getRoomCode());
                        btnUnselected.setVisibility(View.VISIBLE);
                        btnSelect.setVisibility(View.GONE);
                        isSelected = true;
                    } else {
                        Toast.makeText(mContext, "Anda sudah memilih kamar.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            btnUnselected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnUnselected.setVisibility(View.GONE);
                    btnSelect.setVisibility(View.VISIBLE);
                    mSession.clearSharedPrevByKey("room_code_detail");
                    isSelected = false;
                }
            });
        }
    }

    public HotelsAdapterDetail(Context context, List<HotelsDetail> imgHotelsList) {
        this.mContext = context;
        this.hotelsListDetail = imgHotelsList;
    }

    @Override
    public HotelsAdapterDetail.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_hotel_result, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        HotelsDetail hotels = hotelsListDetail.get(position);

        viewHolder.tvHotelName.setVisibility(View.GONE);
        viewHolder.tvHotelAddress.setText("Kapasitas Kamar: " + hotels.getRoomCapacity() + " Orang, " + hotels.getRoomName());
        viewHolder.tvHotelPrice.setText("Rp." + hotels.getBasicPricePerNight() + ",00");

        String smallImage = hotels.getSmallImages();
        if (smallImage != null && !smallImage.equals("")) {
            Picasso.with(mContext)
                    .load(smallImage)
                    .placeholder(R.drawable.img_not_found)
                    .resize(120, 120)
                    .into(viewHolder.imgHotel);
        }
    }

    @Override
    public int getItemCount() {
        return hotelsListDetail.size();
    }
}
