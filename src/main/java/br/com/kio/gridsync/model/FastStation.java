package br.com.kio.gridsync.model;

import br.com.kio.gridsync.enums.ChargingSpeed;
import br.com.kio.gridsync.enums.ConnectorType;

public class FastStation extends ChargingStation {
    public FastStation(String id, double maxCapacity) {
        super(id, maxCapacity, ChargingSpeed.FAST);
        this.connectorTypes.add(ConnectorType.TYPE_C);
        this.connectorTypes.add(ConnectorType.CCS);
    }

    @Override
    public boolean canConnectDevice(Device device) {
        return true; // pode conectar todos os dispositivos
    }

    @Override
    public double calculateChargingTime(Device device) {
        double missingBattery = 100 - device.getCurrentBatteryLevel();
        return (missingBattery * 0.01 * (device.getPower() / 1000)) / (maxCapacity * 0.6); // usa 60% da capacidade da estação para recarregar 
    }

    @Override
    public double calculateEnergyCost(double energy) {
        return energy * 0.7; // 0.7 por kWh
    }
}