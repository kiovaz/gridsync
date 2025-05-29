package br.com.kio.gridsync.model;

import java.time.LocalDateTime;

import br.com.kio.gridsync.enums.DeviceType;
import br.com.kio.gridsync.enums.Priority;

public class Device {
    private String code;
    private DeviceType type;
    private double power; // watts
    private double currentBatteryLevel; // bateria de 0-100
    private Priority priority;
    private LocalDateTime connectionTime;

    public Device(String code, DeviceType type, double power, double currentBatteryLevel, Priority priority) {
        this.code = code;
        this.type = type;
        this.power = power;
        this.currentBatteryLevel = currentBatteryLevel;
        this.priority = priority;
        this.connectionTime = LocalDateTime.now(); // captura a hora exata em que foi criado e alocado para a estação
    }

    // Getters e Setters
    public String getCode() {
        return code;
    }

    public DeviceType getType() {
        return type;
    }

    public double getPower() {
        return power;
    }

    public double getCurrentBatteryLevel() {
        return currentBatteryLevel;
    }

    public Priority getPriority() {
        return priority;
    }

    public LocalDateTime getConnectionTime() {
        return connectionTime;
    }

    public void setCurrentBatteryLevel(double level) {
        if (level >= 0 && level <= 100) {
            this.currentBatteryLevel = level;
        }
    }

    @Override
    public String toString() {
        return "Device{" +
                "code='" + code + '\'' +
                ", type=" + type +
                ", power=" + power +
                ", currentBatteryLevel=" + currentBatteryLevel +
                ", priority=" + priority +
                ", connectionTime=" + connectionTime +
                '}';
    }
}