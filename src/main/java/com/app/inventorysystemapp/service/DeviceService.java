package com.app.inventorysystemapp.service;

import com.app.inventorysystemapp.controller.postModels.DevicePost;
import com.app.inventorysystemapp.exception.ResourceNotFoundException;
import com.app.inventorysystemapp.model.*;
import com.app.inventorysystemapp.repository.DeviceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DeviceService {

  private final DeviceRepository deviceRepository;
  private final DeviceSetService deviceSetService;
  private final DeviceTypeService deviceTypeService;
  private final ModelService modelService;
  private final OwnerService ownerService;
  private final RoomService roomService;

  private final String MARK = "US";

  public DeviceService(DeviceRepository deviceRepository,
                       DeviceSetService deviceSetService,
                       DeviceTypeService deviceTypeService,
                       ModelService modelService,
                       OwnerService ownerService,
                       RoomService roomService) {
    this.deviceRepository = deviceRepository;
    this.deviceSetService = deviceSetService;
    this.deviceTypeService = deviceTypeService;
    this.modelService = modelService;
    this.ownerService = ownerService;
    this.roomService = roomService;
  }

  public Page<Device> getDevices(int page, int pageSize, String orderBy, String sortType, String search) {
    Pageable paging;
    int pageNumber = page > 0 ? page : 1;

    if(orderBy != null){

      String orderValue = orderBy;

      switch(orderBy){
        case "type":
          orderValue = "model." + orderBy;
          break;
      }

      String type = "";

      if(sortType == null){
        type = "desc";
      }else{
        type = sortType;
      }

       if(type.equals("desc")){
         paging = PageRequest.of(pageNumber-1, pageSize, Sort.by(orderValue).descending());
       }else{
        paging = PageRequest.of(pageNumber-1, pageSize, Sort.by(orderValue));
       }

    }else{
      paging = PageRequest.of(pageNumber-1, pageSize);
    }

    if(search == null){
      return deviceRepository.findAll(paging);
    }else{
      return deviceRepository.findByContaining(search, paging);
    }
  }

  public Device getSingleDevice(long id) throws ResourceNotFoundException {
    return deviceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Device not found for id: " + id));
  }

  public Page<Device> getCountedModels(int page) {
    Pageable paging = PageRequest.of(page, 10);
    return deviceRepository.getCountedModels(paging);
  }

  public Device insertDevice(DevicePost device) {
    Room room = roomService.findRoomById(device.getIdRoom());
    Model model = modelService.findModelById(device.getIdModel());
    Owner owner = ownerService.findOwnerById(device.getIdOwner());
    DeviceSet deviceSet = deviceSetService.findDeviceSetById(device.getIdDeviceSet());
    String serialNumber = device.getSerialNumber();
    String comments = "";
    if(device.getComment() != null){
      comments = device.getComment();
    }

    Device newDevice = new Device(serialNumber, room, model, owner, deviceSet, comments);
    newDevice.generateBarCode();

    deviceRepository.save(newDevice);
    return newDevice;
  }

  public ResponseEntity<Device> updateDevice(long id, Device details) throws ResourceNotFoundException {
    Device device = this.getSingleDevice(id);
    device.setSerialNumber(details.getSerialNumber());
    device.setRoom(details.getRoom());
    device.setDeviceSet(details.getDeviceSet());
    device.setModel(details.getModel());
    device.setOwner(details.getOwner());
    device.setComments(details.getComments());
    device.setBarCode(details.getBarCode());
    final Device updatedDevice = deviceRepository.save(device);
    return ResponseEntity.ok(updatedDevice);
  }

  public Map<String, Boolean> deleteDevice(long id) throws ResourceNotFoundException {
    Device device = this.getSingleDevice(id);
    deviceRepository.delete(device);
    Map<String, Boolean> response = new HashMap<>();
    response.put("deleted", Boolean.TRUE);
    return response;
  }
}
