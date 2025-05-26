package br.com.kio.gridsync.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.kio.gridsync.model.ChargingStation;
import br.com.kio.gridsync.model.EnergyReport;

public class ReportService {
    private EnergyReport currentReport;
    private Map<String, EnergyReport> monthlyReports;

    public ReportService() {
        this.monthlyReports = new HashMap<>();
        startNewReport();
    }

    public void startNewReport() {
        if (currentReport != null) {
            monthlyReports.put(currentReport.getStartDate().toString(), currentReport);
        }
        this.currentReport = new EnergyReport(LocalDateTime.now(), null);
    }

    public void addChargingSession(String stationId, double energy, double revenue) {
        currentReport.addStationConsumption(stationId, energy, revenue);
    }

    public EnergyReport generateCurrentReport() {
        return currentReport;
    }

    public EnergyReport generateMonthlyReport(int year, int month) {
        String key = year + "-" + month;
        return monthlyReports.get(key);
    }

    public void printStationStatus(List<ChargingStation> stations) {
        System.out.println("\n=== Station Status ===");
        stations.forEach(station -> {
            System.out.printf("Station %s [%s]: %.2f/%.2f kW (%.1f%%) - %d devices connected%n",
                    station.getId(),
                    station.getSpeed(),
                    station.getCurrentLoad(),
                    station.getMaxCapacity(),
                    (station.getCurrentLoad() / station.getMaxCapacity()) * 100,
                    station.getConnectedDevices().size());
        });
    }
}