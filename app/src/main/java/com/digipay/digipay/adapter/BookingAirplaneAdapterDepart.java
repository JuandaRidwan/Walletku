package com.digipay.digipay.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.digipay.digipay.R;
import com.digipay.digipay.activity.BookingAirplaneTicketActivity;
import com.digipay.digipay.functions.SessionManager;
import com.digipay.digipay.models.BookingAirplane;

import java.util.List;

public class BookingAirplaneAdapterDepart extends RecyclerView.Adapter<BookingAirplaneAdapterDepart.MyViewHolder> {

    private List<BookingAirplane> bookingAirplaneList;
    private SessionManager mSession;
    private RecyclerView recyclerView;
    protected Context mContext;
    private BookingAirplaneTicketActivity bookTicket;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAirplaneName, tvBookingPayment, tvBookingTime, tvBookingClass, tvBookingTimeTransit, tvDirectFlag;
        public CardView cv;
        public Button btnSelect, btnSelected;

        public MyViewHolder(View view) {
            super(view);
            cv = (CardView) view.findViewById(R.id.cv);
            tvAirplaneName = (TextView) view.findViewById(R.id.tvAirplaneName);
            tvBookingPayment = (TextView) view.findViewById(R.id.tvBookingPayment);
            tvBookingTime = (TextView) view.findViewById(R.id.tvBookingTime);
            tvBookingClass = (TextView) view.findViewById(R.id.tvBookingClass);
            tvBookingTimeTransit = (TextView) view.findViewById(R.id.tvBookingTimeTransit);
            tvDirectFlag = (TextView) view.findViewById(R.id.tvDirectFlag);
            btnSelect = (Button) view.findViewById(R.id.btnSelect);
            btnSelected = (Button) view.findViewById(R.id.btnSelected);

            mSession = new SessionManager(mContext);

            btnSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    BookingAirplane bookingDetail = bookingAirplaneList.get(pos);
                    mSession.getSessionId();
                    mSession.setFno1(bookingDetail.getFnoDepart());
                    mSession.setClass1(bookingDetail.getClassIdNext());

                    recyclerView.setVisibility(View.GONE);

                    bookTicket.selectedItemDepart(bookingDetail.getAirlineNameDepart(), bookingDetail.getClassPriceNext(),
                            bookingDetail.getEtdDepart(), bookingDetail.getEtaDepart(), bookingDetail.getFromDepart(), bookingDetail.getToDepart(),
                            bookingDetail.getClassNext(), bookingDetail.getEtaConnecting(),
                            bookingDetail.getTypeSchedule());
                }
            });
        }
    }

    public BookingAirplaneAdapterDepart(Context context, List<BookingAirplane> bookingAirplaneList,
                                        RecyclerView recyclerView, BookingAirplaneTicketActivity bookTicket) {
        this.mContext = context;
        this.bookingAirplaneList = bookingAirplaneList;
        this.recyclerView = recyclerView;
        this.bookTicket = bookTicket;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booking_airplane_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BookingAirplane bookingList = bookingAirplaneList.get(position);

        String airplaneName = bookingList.getAirlineNameDepart();
        String edtSchedule = bookingList.getEtdDepart();
        String etaSchedule = bookingList.getEtaDepart();
        String bookingClassName = bookingList.getClassNameNext();
        String bookingClass = bookingList.getClassNext();
        String fromSchedule = bookingList.getFromDepart();
        String toSchedule = bookingList.getToDepart();
        String edtTransit = bookingList.getEtdConnecting();
        String etaTransit = bookingList.getEtaConnecting();
        String fromTransit = bookingList.getFromConnecting();
        String toTransit = bookingList.getToConnecting();
        String pricePromo = bookingList.getClassPrice();
        String priceBooking = bookingList.getClassPriceNext();
        String type = bookingList.getTypeSchedule();

        if (airplaneName != null) {
            holder.tvAirplaneName.setText(airplaneName);
        }

        if (bookingClass != null && bookingClassName != null) {
            holder.tvBookingClass.setText("Class " + bookingClass + " (" + bookingClassName + ")");
        }

        if (edtSchedule != null && etaSchedule != null && fromSchedule != null && toSchedule != null) {
            holder.tvBookingTime.setText(edtSchedule + " - " + etaSchedule +
                    "(" + fromSchedule + " - " + toSchedule + ")");
        }

        if (edtTransit != null && etaTransit != null && fromTransit != null && toTransit != null) {
            holder.tvBookingTimeTransit.setText(edtTransit + " - " + etaTransit +
                    "(" + fromTransit + " - " + toTransit + ")");
        }

        if (priceBooking != null) {
            holder.tvBookingPayment.setText("Rp." + priceBooking + ",-/orang");
        } else {
            holder.tvBookingPayment.setText("Rp." + pricePromo + ",-/orang");
        }

        if (type != null) {
            holder.tvDirectFlag.setText(type);
        } else {
            holder.tvDirectFlag.setText(" - ");
        }
    }

    @Override
    public int getItemCount() {
        return bookingAirplaneList.size();
    }

}
