package br.com.kio.gridsync.model;

import br.com.kio.gridsync.enums.ChargingSpeed;
import br.com.kio.gridsync.enums.ConnectorType;
import br.com.kio.gridsync.enums.DeviceType;

public class StandartStation extends ChargingStation {
    public StandartStation(String id, double maxCapacity) {
        super(id, maxCapacity, ChargingSpeed.NORMAL);
        this.connectorTypes.add(ConnectorType.TYPE_A);
        this.connectorTypes.add(ConnectorType.TYPE_B);
    }

    @Override
    public boolean canConnectDevice(Device device) {
        return device.getType() != DeviceType.ELECTRIC_VEHICLE;
    }

    @Override
    public double calculateChargingTime(Device device) {
        // Simplified calculation: time = (100 - currentBattery) * capacity / power
        double missingBattery = 100 - device.getCurrentBatteryLevel();
        return (missingBattery * 0.01 * (device.getPower() / 1000)) / (maxCapacity * 0.3); // 30% of max capacity
    }

    @Override
    public double calculateEnergyCost(double energy) {
        return energy * 0.5; // $0.5 per kWh
    }
}