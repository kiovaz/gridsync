package br.com.kio.gridsync.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class EnergyReport {
    private LocalDateTime startDate;                // report mensal
    private LocalDateTime endDate;
    private Map<String, Double> energyConsumedPerStation;           // mapa pra procurar o nome e o valor de cada estação
    private double totalEnergyConsumed;
    private double totalRevenue;

    public EnergyReport(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.energyConsumedPerStation = new HashMap<>();
        this.totalEnergyConsumed = 0;
        this.totalRevenue = 0;
    }

    public void addStationConsumption(String stationId, double energy, double revenue) {
        energyConsumedPerStation.merge(stationId, energy, Double::sum);
        totalEnergyConsumed += energy;              //  usa o mapa pra atualizar o consumo de estação
        totalRevenue += revenue;
    }

    // Getters
    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public Map<String, Double> getEnergyConsumedPerStation() {
        return energyConsumedPerStation;
    }

    public double getTotalEnergyConsumed() {
        return totalEnergyConsumed;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }
}