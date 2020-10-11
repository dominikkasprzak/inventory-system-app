package com.app.inventorysystemapp.controller;

import com.app.inventorysystemapp.exception.ResourceNotFoundException;
import com.app.inventorysystemapp.model.Device;
import com.app.inventorysystemapp.service.DeviceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class DeviceController {

  private final DeviceService deviceService;

  public DeviceController(DeviceService deviceService) {
    this.deviceService = deviceService;
  }

  @GetMapping("/devices")
  public List<Device> getDevices() {
    return deviceService.getDevices();
  }

  @GetMapping("/devices/{id}")
  public Device getSingleDevice(@PathVariable long id) throws ResourceNotFoundException {
    return deviceService.getSingleDevice(id);
  }

  @PostMapping("/devices")
  public Device insertDevice(@RequestBody Device device) {
    return deviceService.insertDevice(device);
  }

  @PutMapping("/devices/{id}")
  public ResponseEntity<Device> updateDevice(@PathVariable(value = "id") Long id, @RequestBody Device details) throws ResourceNotFoundException {
    return deviceService.updateDevice(id, details);
  }

  @DeleteMapping("/devices/{id}")
  public Map<String, Boolean> deleteDevice(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
    return deviceService.deleteDevice(id);
  }
}
