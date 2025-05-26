package br.com.kio.gridsync.model;

import br.com.kio.gridsync.enums.ChargingSpeed;
import br.com.kio.gridsync.enums.ConnectorType;
import br.com.kio.gridsync.enums.DeviceType;

public class UltraFastStation extends ChargingStation {
    public UltraFastStation(String id, double maxCapacity) {
        super(id, maxCapacity, ChargingSpeed.ULTRA_FAST);
        this.connectorTypes.add(ConnectorType.CCS);
        this.connectorTypes.add(ConnectorType.CHADEMO);
    }

    @Override
    public boolean canConnectDevice(Device device) {
        return device.getType() == DeviceType.ELECTRIC_VEHICLE;
    }

    @Override
    public double calculateChargingTime(Device device) {
        double missingBattery = 100 - device.getCurrentBatteryLevel();
        return (missingBattery * 0.01 * (device.getPower() / 1000)) / maxCapacity;
    }

    @Override
    public double calculateEnergyCost(double energy) {
        return energy * 1.0; // $1.0 per kWh
    }
}