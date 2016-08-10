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

public class BookingAirplaneAdapterReturn extends RecyclerView.Adapter<BookingAirplaneAdapterReturn.MyViewHolder> {

    private List<BookingAirplane> bookingAirplaneList;
    private SessionManager mSession;
    private RecyclerView recyclerView;
    protected Context mContext;
    private BookingAirplaneTicketActivity bookTicket;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAirplaneName, tvBookingClass, tvBookingPayment, tvBookingTime, tvBookingTimeTransit, tvDirectFlag;
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
                    mSession.setFno2(bookingDetail.getFnoReturn());
                    mSession.setClass2(bookingDetail.getClassIdReturnNext());

                    recyclerView.setVisibility(View.GONE);
                    bookTicket.selectedItemReturn(bookingDetail.getAirlineNameReturn(), bookingDetail.getClassPriceReturnNext(),
                            bookingDetail.getEtdReturn(), bookingDetail.getEtaReturn(), bookingDetail.getFromReturn(),
                            bookingDetail.getToReturn(), bookingDetail.getClassReturnNext(), bookingDetail.getEtaConnectingReturn(),
                            bookingDetail.getTypeReturn());
                }
            });
        }
    }


    public BookingAirplaneAdapterReturn(Context context, List<BookingAirplane> bookingAirplaneList,
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

        String edtReturn = bookingList.getEtdReturn();
        String etaReturn = bookingList.getEtaReturn();
        String fromReturn = bookingList.getFromReturn();
        String bookingClassName = bookingList.getClassNameReturnNext();
        String bookingClass = bookingList.getClassReturnNext();
        String toReturn = bookingList.getToReturn();
        String edtTransit = bookingList.getEtdConnectingReturn();
        String etaTransit = bookingList.getEtaConnectingReturn();
        String fromTransit = bookingList.getFromConnectingReturn();
        String toTransit = bookingList.getToConnectingReturn();
        String pricePromo = bookingList.getClassPriceReturn();
        String priceBooking = bookingList.getClassPriceReturnNext();

        holder.tvAirplaneName.setText(bookingList.getAirlineNameReturn());

        if (edtReturn != null && etaReturn != null && fromReturn != null && toReturn != null) {
            holder.tvBookingTime.setText(edtReturn + " - " + etaReturn +
                    "(" + fromReturn + " - " + toReturn + ")");
        }

        if (bookingClass != null && bookingClassName != null) {
            holder.tvBookingClass.setText("Class " + bookingClass + " (" + bookingClassName + ")");
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

        holder.tvDirectFlag.setText(bookingList.getTypeReturn());
    }

    @Override
    public int getItemCount() {
        return bookingAirplaneList.size();
    }
}
