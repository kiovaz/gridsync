package br.com.kio.gridsync;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.kio.gridsync.enums.DeviceType;
import br.com.kio.gridsync.enums.Priority;
import br.com.kio.gridsync.model.Device;
import br.com.kio.gridsync.services.ChargingService;

public class ChargingServiceTest {
    private ChargingService chargingService;
    
    @BeforeEach
    public void setUp() {
        System.out.println("Setting up test...");
        this.chargingService = new ChargingService();
        assertNotNull(this.chargingService, "ChargingService should not be null");
    }
    
    @Test
    public void testConnectDevice() {
        Device notebook = new Device("NB-001", DeviceType.NOTEBOOK, 65, 20, Priority.NORMAL);
        assertTrue(chargingService.connectDevice(notebook), "Device should connect successfully");
        assertEquals(1, chargingService.getConnectedDevices().size(), "Should have 1 connected device");
    }
    
    @Test
    public void testDisconnectDevice() {
        Device drone = new Device("DR-001", DeviceType.DRONE, 30, 50, Priority.HIGH);
        assertTrue(chargingService.connectDevice(drone), "Device should connect first");
        assertNotNull(chargingService.disconnectDevice("DR-001"), "Disconnect should return billing info");
        assertEquals(0, chargingService.getConnectedDevices().size(), "Should have 0 connected devices after disconnect");
    }
    
    @Test
    public void testMaxDevices() {
        // Teste de capacidade máxima (50 dispositivos)
        for (int i = 0; i < 50; i++) {
            Device device = new Device("DEV-" + i, DeviceType.CELLPHONE, 5, 30, Priority.LOW);
            assertTrue(chargingService.connectDevice(device), "Device " + i + " should connect");
        }
        
        // Tentativa de conectar dispositivo extra (deve falhar)
        Device extraDevice = new Device("EXTRA", DeviceType.NOTEBOOK, 65, 20, Priority.NORMAL);
        assertFalse(chargingService.connectDevice(extraDevice), "Should not connect beyond max capacity");
    }
    
    @Test
    public void testFindBestStation() {
        Device ev = new Device("EV-001", DeviceType.ELECTRIC_VEHICLE, 5000, 30, Priority.HIGH);
        assertTrue(chargingService.connectDevice(ev), "EV should connect");
        
        // Verifica se foi conectado à estação ultra-rápida
        assertEquals("UFS-001", 
            chargingService.getStations().stream()
                .filter(s -> s.getConnectedDevices().contains(ev))
                .findFirst()
                .orElseThrow()
                .getId(),
            "EV should be connected to ultra fast station");
    }
}