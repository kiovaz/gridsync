package br.com.kio.gridsync.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.com.kio.gridsync.model.Billing;

public class BillingService {
    private List<Billing> billingRecords;

    public BillingService() {
        this.billingRecords = new ArrayList<>();
    }

    public void addBillingRecord(Billing billing) {
        if (billing != null) {
            billingRecords.add(billing);
        }
    }

    public List<Billing> getBillingRecords() {
        return new ArrayList<>(billingRecords);
    }

    public double calculateTotalRevenue() {
        return billingRecords.stream()
                .mapToDouble(Billing::getTotalCost)
                .sum();
    }

    public List<Billing> getBillingsForDevice(String deviceCode) {
        return billingRecords.stream()
                .filter(b -> b.getDeviceCode().equals(deviceCode))
                .collect(Collectors.toList());
    }
}