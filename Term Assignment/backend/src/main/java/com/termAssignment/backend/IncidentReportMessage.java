package com.termAssignment.backend;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IncidentReportMessage {
    @JsonProperty("name")
    private String name;

    @JsonProperty("place")
    private String place;

    @JsonProperty("incident_report")
    private String incidentReport;

    @JsonProperty("image_data_base64")
    private String imageDataBase64;

    @JsonProperty("type")
    private String type;

    public IncidentReportMessage(String name, String place, String incidentReport, String imageDataBase64, String type) {
        this.name = name;
        this.place = place;
        this.incidentReport = incidentReport;
        this.imageDataBase64 = imageDataBase64;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getIncidentReport() {
        return incidentReport;
    }

    public void setIncidentReport(String incidentReport) {
        this.incidentReport = incidentReport;
    }

    public String getImageDataBase64() {
        return imageDataBase64;
    }

    public void setImageDataBase64(String imageDataBase64) {
        this.imageDataBase64 = imageDataBase64;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "IncidentReportMessage{" +
                "name='" + name + '\'' +
                ", place='" + place + '\'' +
                ", incidentReport='" + incidentReport + '\'' +
                ", imageDataBase64='" + imageDataBase64 + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
