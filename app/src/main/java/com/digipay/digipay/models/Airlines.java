package com.digipay.digipay.models;

import java.io.Serializable;


public class Airlines implements Serializable{
    private String airlinesId,airlinesCode,airlinesName,airlinesType,connectionStatus;

    public String getAirlinesId() {
        return airlinesId;
    }

    public void setAirlinesId(String airlinesId) {
        this.airlinesId = airlinesId;
    }

    public String getAirlinesCode() {
        return airlinesCode;
    }

    public void setAirlinesCode(String airlinesCode) {
        this.airlinesCode = airlinesCode;
    }

    public String getAirlinesName() {
        return airlinesName;
    }

    public void setAirlinesName(String airlinesName) {
        this.airlinesName = airlinesName;
    }

    public String getAirlinesType() {
        return airlinesType;
    }

    public void setAirlinesType(String airlinesType) {
        this.airlinesType = airlinesType;
    }

    public String getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }
}
