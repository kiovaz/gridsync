package br.com.kio.gridsync.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import br.com.kio.gridsync.enums.Priority;
import br.com.kio.gridsync.model.Billing;
import br.com.kio.gridsync.model.ChargingStation;
import br.com.kio.gridsync.model.Device;
import br.com.kio.gridsync.model.FastStation;
import br.com.kio.gridsync.model.StandartStation;
import br.com.kio.gridsync.model.UltraFastStation;

public class ChargingService {
    private static final int MAX_DEVICES = 50;
    private List<Device> connectedDevices;
    private List<ChargingStation> stations;

    public ChargingService() {
        this.connectedDevices = new ArrayList<>();
        this.stations = new ArrayList<>();
        initializeStations();
    }

    private void initializeStations() {
        // estacoes iniciais basicas
        stations.add(new StandartStation("STD-001", 10.0));
        stations.add(new StandartStation("STD-002", 10.0));
        stations.add(new FastStation("FST-001", 20.0));
        stations.add(new FastStation("FST-002", 20.0));
        stations.add(new UltraFastStation("UFS-001", 50.0));
    }

    public boolean connectDevice(Device device) {
        if (connectedDevices.size() >= MAX_DEVICES) {
            System.out.println("Maximum number of devices reached.");
            return false;
        }

        // encotra a melhor estação pro dispositivo
        ChargingStation bestStation = findBestStation(device);

        if (bestStation != null && bestStation.connectDevice(device)) {
            connectedDevices.add(device);
            System.out.println("Device " + device.getCode() + " connected to station " + bestStation.getId());
            return true;
        }

        System.out.println("No available station found for device " + device.getCode());
        return false;
    }

    private ChargingStation findBestStation(Device device) {
        return stations.stream()
                .filter(station -> station.canConnectDevice(device))
                .filter(station -> station.getAvailableCapacity() >= (device.getPower() / 1000))
                .sorted(Comparator.comparing((ChargingStation station) -> device.getPriority() == Priority.HIGH
                        ? station.calculateChargingTime(device)
                        : -station.getAvailableCapacity()))
                .findFirst()
                .orElse(null); // filtragem com stream no arraylist pra encontrar melhor estacao
    }

    public Billing disconnectDevice(String deviceCode) {
        Device device = connectedDevices.stream()
                .filter(d -> d.getCode().equals(deviceCode))
                .findFirst()
                .orElse(null);

        if (device == null) {
            System.out.println("Device not found: " + deviceCode);
            return null;
        }

        ChargingStation station = stations.stream()
                .filter(s -> s.getConnectedDevices().contains(device))
                .findFirst()
                .orElse(null);

        if (station == null) {
            System.out.println("Device not connected to any station: " + deviceCode);
            return null;
        }

        // Calcula energia consumida
        double hoursConnected = java.time.Duration.between(
                device.getConnectionTime(), java.time.LocalDateTime.now()).toMinutes() / 60.0;
        double energyConsumed = (device.getPower() / 1000) * hoursConnected;

        // Cria a billing (nao a jeans)
        Billing billing = new Billing(
                device.getCode(),
                station.getId(),
                station.getSpeed(),
                device.getConnectionTime(),
                energyConsumed,
                station.calculateEnergyCost(energyConsumed));

        // Desconecta dispositivo
        station.disconnectDevice(device);
        connectedDevices.remove(device);

        return billing;
    }

    // Getters
    public List<Device> getConnectedDevices() {
        return connectedDevices;
    }

    public List<ChargingStation> getStations() {
        return stations;
    }
}