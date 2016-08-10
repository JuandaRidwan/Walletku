package com.digipay.digipay.models;

import java.io.Serializable;

public class HotelsDetail implements Serializable {
    private String bigImages, CancelDetail, RoomCode, BasicPricePerNight;
    private String roomCapacity, roomName, smallImages;


    public String getCancelDetail() {
        return CancelDetail;
    }

    public void setCancelDetail(String cancelDetail) {
        CancelDetail = cancelDetail;
    }

    public String getRoomCode() {
        return RoomCode;
    }

    public void setRoomCode(String roomCode) {
        RoomCode = roomCode;
    }

    public String getBasicPricePerNight() {
        return BasicPricePerNight;
    }

    public void setBasicPricePerNight(String basicPricePerNight) {
        BasicPricePerNight = basicPricePerNight;
    }

    public String getBigImages() {
        return bigImages;
    }

    public void setBigImages(String bigImages) {
        this.bigImages = bigImages;
    }

    public String getRoomCapacity() {
        return roomCapacity;
    }

    public void setRoomCapacity(String roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getSmallImages() {
        return smallImages;
    }

    public void setSmallImages(String smallImages) {
        this.smallImages = smallImages;
    }
}
