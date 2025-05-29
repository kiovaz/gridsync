package br.com.kio.gridsync.model;

import java.util.ArrayList;
import java.util.List;

import br.com.kio.gridsync.enums.ChargingSpeed;
import br.com.kio.gridsync.enums.ConnectorType;

public abstract class ChargingStation {
    protected String id;
    protected double maxCapacity; // kWh
    protected List<ConnectorType> connectorTypes;
    protected ChargingSpeed speed;
    protected double currentLoad; // energia sendo usada para carregar dispositivo
    protected List<Device> connectedDevices;

    public ChargingStation(String id, double maxCapacity, ChargingSpeed speed) {
        this.id = id;
        this.maxCapacity = maxCapacity;
        this.speed = speed;
        this.currentLoad = 0;
        this.connectedDevices = new ArrayList<>();
        this.connectorTypes = new ArrayList<>();
    }

    public abstract boolean canConnectDevice(Device device);

    public abstract double calculateChargingTime(Device device);

    public abstract double calculateEnergyCost(double energy);

    public boolean connectDevice(Device device) {
        if (canConnectDevice(device) && (currentLoad + device.getPower() / 1000) <= maxCapacity) {
            connectedDevices.add(device);           // adiciona o dispositivo ao arraylist da estação
            currentLoad += device.getPower() / 1000; // converte watts pra kw
            return true;
        }
        return false;
    }

    public void disconnectDevice(Device device) {
        if (connectedDevices.remove(device)) {
            currentLoad -= device.getPower() / 1000;
        }
    }

    // Getters
    public String getId() {
        return id;
    }

    public double getMaxCapacity() {
        return maxCapacity;
    }

    public List<ConnectorType> getConnectorTypes() {
        return connectorTypes;
    }

    public ChargingSpeed getSpeed() {
        return speed;
    }

    public double getCurrentLoad() {
        return currentLoad;
    }

    public List<Device> getConnectedDevices() {
        return connectedDevices;
    }

    public double getAvailableCapacity() {
        return maxCapacity - currentLoad;
    }
}