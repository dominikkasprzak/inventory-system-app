package com.app.inventorysystemapp.controller;

import com.app.inventorysystemapp.controller.dto.DeletedDeviceDto;
import com.app.inventorysystemapp.controller.dto.DeviceDto;
import com.app.inventorysystemapp.controller.mapper.DeviceMapper;
import com.app.inventorysystemapp.controller.requestModels.DeviceRequest;
import com.app.inventorysystemapp.exception.ResourceNotFoundException;
import com.app.inventorysystemapp.model.DeletedDevice;
import com.app.inventorysystemapp.model.Device;
import com.app.inventorysystemapp.service.DeviceService;
import org.springframework.data.domain.Page;
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
  public Page<DeviceDto> getDevices(@RequestParam(required = false) int page,
                                    @RequestParam(required = false) int pageSize,
                                    @RequestParam(required = false) String orderBy,
                                    @RequestParam(required = false) String sortType,
                                    @RequestParam(required = false) String search) {
    return DeviceMapper.mapToDeviceDtos(deviceService.getDevices(page, pageSize, orderBy, sortType, search));
  }

  @GetMapping("/devices/all")
  public List<DeviceDto> getAllDevices() {
    return deviceService.getAllDevices();
  }

  @GetMapping("/devices/deleted")
  public Page<DeletedDeviceDto> getDeletedDevices(int page,
                                                  int pageSize,
                                                  @RequestParam(required = false) String orderBy,
                                                  @RequestParam(required = false) String sortType,
                                                  @RequestParam(required = false) String search) {
    return DeviceMapper.mapToDeletedDeviceDtos(deviceService.getDeletedDevices(page, pageSize, orderBy, sortType, search));
  }

  @GetMapping("/devices/{id}")
  public Device getSingleDevice(@PathVariable long id) throws ResourceNotFoundException {
    return deviceService.getSingleDevice(id);
  }

  @GetMapping("/devices/barcode/{barcode}")
  public DeviceDto getDeviceByBarcode(@PathVariable long barcode) {
    return DeviceMapper.mapToDeviceDto(deviceService.findByBarcode(barcode));
  }

  @GetMapping("/devices/all/barcodes")
  public List<Long> getAllBarcodes() {
    return deviceService.getAllBarcodes();
  }

  @GetMapping("/devices/count/models")
  public Page<Device> countModels(@RequestParam(required = false) int page) {
    int pageNumber = page > 0 ? page : 1;
    return deviceService.getCountedModels(pageNumber - 1);
  }

  @PostMapping("/devices")
  public Device insertDevice(@RequestBody DeviceRequest device) {
    return deviceService.insertDevice(device);
  }

  @PutMapping("/devices/{id}")
  public ResponseEntity<Device> updateDevice(@PathVariable(value = "id") Long id, @RequestBody DeviceRequest details) throws ResourceNotFoundException {
    return deviceService.updateDevice(id, details);
  }

  @DeleteMapping("/devices/{id}")
  public DeletedDeviceDto deleteDevice(@PathVariable(value = "id") Long id) {
    return DeviceMapper.mapToDeletedDeviceDto(deviceService.deleteDevice(id));
  }

}
