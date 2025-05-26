package br.com.kio.gridsync.model;

import java.time.LocalDateTime;

import br.com.kio.gridsync.enums.ChargingSpeed;

public class Billing {
    private String deviceCode;
    private String stationId;
    private ChargingSpeed speed;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double energyConsumed; // in kWh
    private double tariff;
    private double totalCost;

    public Billing(String deviceCode, String stationId, ChargingSpeed speed, 
                  LocalDateTime startTime, double energyConsumed, double tariff) {
        this.deviceCode = deviceCode;
        this.stationId = stationId;
        this.speed = speed;
        this.startTime = startTime;
        this.endTime = LocalDateTime.now();
        this.energyConsumed = energyConsumed;
        this.tariff = tariff;
        this.totalCost = energyConsumed * tariff;
    }

    // Getters
    public String getDeviceCode() { return deviceCode; }
    public String getStationId() { return stationId; }
    public ChargingSpeed getSpeed() { return speed; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public double getEnergyConsumed() { return energyConsumed; }
    public double getTariff() { return tariff; }
    public double getTotalCost() { return totalCost; }

    @Override
    public String toString() {
        return "Billing{" +
                "deviceCode='" + deviceCode + '\'' +
                ", stationId='" + stationId + '\'' +
                ", speed=" + speed +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", energyConsumed=" + energyConsumed + " kWh" +
                ", tariff=$" + tariff + "/kWh" +
                ", totalCost=$" + totalCost +
                '}';
    }
}