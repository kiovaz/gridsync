package br.com.kio.gridsync;

import java.util.List;
import java.util.Scanner;

import br.com.kio.gridsync.enums.DeviceType;
import br.com.kio.gridsync.enums.Priority;
import br.com.kio.gridsync.model.Billing;
import br.com.kio.gridsync.model.Device;
import br.com.kio.gridsync.model.EnergyReport;
import br.com.kio.gridsync.services.BillingService;
import br.com.kio.gridsync.services.ChargingService;
import br.com.kio.gridsync.services.ReportService;
import br.com.kio.gridsync.utils.TimeUtils;

public class GridSyncSystem {
    private ChargingService chargingService;
    private BillingService billingService;
    private ReportService reportService;
    private Scanner scanner;

    public GridSyncSystem() {
        this.chargingService = new ChargingService();
        this.billingService = new BillingService();
        this.reportService = new ReportService();
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    registerNewDevice();
                    break;
                case 2:
                    disconnectDevice();
                    break;
                case 3:
                    viewConnectedDevices();
                    break;
                case 4:
                    viewStationsStatus();
                    break;
                case 5:
                    generateReport();
                    break;
                case 6:
                    viewBillingRecords();
                    break;
                case 0:
                    running = false;
                    System.out.println("Exiting GridSync System...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println("\n=== GridSync Energy Management System ===");
        System.out.println("1. Register new device");
        System.out.println("2. Disconnect device");
        System.out.println("3. View connected devices");
        System.out.println("4. View stations status");
        System.out.println("5. Generate report");
        System.out.println("6. View billing records");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private void registerNewDevice() {
        System.out.println("\n--- Register New Device ---");
        
        System.out.print("Device code: ");
        String code = scanner.nextLine();
        
        System.out.println("Device type (1-4):");
        System.out.println("1. Notebook");
        System.out.println("2. Drone");
        System.out.println("3. Cellphone");
        System.out.println("4. Electric Vehicle");
        System.out.print("Choose type: ");
        DeviceType type = DeviceType.values()[scanner.nextInt() - 1];
        
        System.out.print("Power (in watts): ");
        double power = scanner.nextDouble();
        
        System.out.print("Current battery level (%): ");
        double batteryLevel = scanner.nextDouble();
        
        System.out.println("Priority (1-3):");
        System.out.println("1. Low");
        System.out.println("2. Normal");
        System.out.println("3. High");
        System.out.print("Choose priority: ");
        Priority priority = Priority.values()[scanner.nextInt() - 1];
        scanner.nextLine(); // consume newline
        
        Device device = new Device(code, type, power, batteryLevel, priority);
        if (chargingService.connectDevice(device)) {
            System.out.println("Device registered and connected successfully!");
        } else {
            System.out.println("Failed to connect device.");
        }
    }

    private void disconnectDevice() {
        System.out.println("\n--- Disconnect Device ---");
        System.out.print("Enter device code: ");
        String code = scanner.nextLine();
        
        Billing billing = chargingService.disconnectDevice(code);
        if (billing != null) {
            billingService.addBillingRecord(billing);
            System.out.println("Device disconnected. Billing details:");
            System.out.println(billing);
        }
    }

    private void viewConnectedDevices() {
        System.out.println("\n--- Connected Devices ---");
        List<Device> devices = chargingService.getConnectedDevices();
        if (devices.isEmpty()) {
            System.out.println("No devices currently connected.");
        } else {
            devices.forEach(device -> {
                System.out.printf("%s (%s) - %.1f%% battery, %.0fW, priority: %s%n",
                        device.getCode(),
                        device.getType(),
                        device.getCurrentBatteryLevel(),
                        device.getPower(),
                        device.getPriority());
            });
        }
    }

    private void viewStationsStatus() {
        reportService.printStationStatus(chargingService.getStations());
    }

    private void generateReport() {
        System.out.println("\n--- Reports ---");
        System.out.println("1. Current session report");
        System.out.println("2. Monthly report");
        System.out.print("Choose option: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        if (choice == 1) {
            EnergyReport report = reportService.generateCurrentReport();
            System.out.println("\n=== Current Session Report ===");
            System.out.printf("Start time: %s%n", TimeUtils.formatDateTime(report.getStartDate()));
            System.out.printf("Total energy consumed: %.2f kWh%n", report.getTotalEnergyConsumed());
            System.out.printf("Total revenue: $%.2f%n", report.getTotalRevenue());
            
            System.out.println("\nConsumption by station:");
            report.getEnergyConsumedPerStation().forEach((stationId, energy) -> {
                System.out.printf("- %s: %.2f kWh%n", stationId, energy);
            });
        } else if (choice == 2) {
            System.out.print("Enter year (e.g., 2023): ");
            int year = scanner.nextInt();
            System.out.print("Enter month (1-12): ");
            int month = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            EnergyReport report = reportService.generateMonthlyReport(year, month);
            if (report != null) {
                System.out.println("\n=== Monthly Report ===");
                System.out.printf("Period: %s to %s%n", 
                        TimeUtils.formatDateTime(report.getStartDate()),
                        TimeUtils.formatDateTime(report.getEndDate()));
                System.out.printf("Total energy consumed: %.2f kWh%n", report.getTotalEnergyConsumed());
                System.out.printf("Total revenue: $%.2f%n", report.getTotalRevenue());
                
                System.out.println("\nConsumption by station:");
                report.getEnergyConsumedPerStation().forEach((stationId, energy) -> {
                    System.out.printf("- %s: %.2f kWh%n", stationId, energy);
                });
            } else {
                System.out.println("No data available for the specified month.");
            }
        } else {
            System.out.println("Invalid option.");
        }
    }

    private void viewBillingRecords() {
        System.out.println("\n--- Billing Records ---");
        System.out.println("1. View all records");
        System.out.println("2. View records for a device");
        System.out.print("Choose option: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        if (choice == 1) {
            List<Billing> records = billingService.getBillingRecords();
            if (records.isEmpty()) {
                System.out.println("No billing records available.");
            } else {
                records.forEach(System.out::println);
            }
        } else if (choice == 2) {
            System.out.print("Enter device code: ");
            String code = scanner.nextLine();
            List<Billing> records = billingService.getBillingsForDevice(code);
            if (records.isEmpty()) {
                System.out.println("No billing records for device " + code);
            } else {
                records.forEach(System.out::println);
            }
        } else {
            System.out.println("Invalid option.");
        }
    }
}