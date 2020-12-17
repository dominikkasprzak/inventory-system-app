package com.app.inventorysystemapp.service;

import com.app.inventorysystemapp.controller.requestModels.DeviceRequest;
import com.app.inventorysystemapp.exception.ResourceNotFoundException;
import com.app.inventorysystemapp.model.*;
import com.app.inventorysystemapp.repository.DeletedDeviceRepository;
import com.app.inventorysystemapp.repository.DeviceRepository;
import org.hibernate.sql.Delete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService implements com.app.inventorysystemapp.service.Service {

  private final DeviceRepository deviceRepository;
  private final DeletedDeviceRepository deletedDeviceRepository;

  private final DeviceSetService deviceSetService;
  private final DeviceTypeService deviceTypeService;
  private final ModelService modelService;
  private final OwnerService ownerService;
  private final RoomService roomService;
  private final HistoryService historyService;

  private final String MARK = "US";

  public DeviceService(DeviceRepository deviceRepository,
                       DeletedDeviceRepository deletedDeviceRepository, DeviceSetService deviceSetService,
                       DeviceTypeService deviceTypeService,
                       ModelService modelService,
                       OwnerService ownerService,
                       RoomService roomService, HistoryService historyService) {
    this.deviceRepository = deviceRepository;
    this.deletedDeviceRepository = deletedDeviceRepository;
    this.deviceSetService = deviceSetService;
    this.deviceTypeService = deviceTypeService;
    this.modelService = modelService;
    this.ownerService = ownerService;
    this.roomService = roomService;
    this.historyService = historyService;
  }

  public Page<Device> getDevices(int page,
                                 int pageSize,
                                 String orderBy,
                                 String sortType,
                                 String search) {
    int pageNumber = page > 0 ? page : 1;

    Pageable paging = generatePageRequest(pageNumber, pageSize, orderBy, sortType);

    if (search == null) {
      return deviceRepository.findAll(paging);
    } else {
      return deviceRepository.findByContaining(search, paging);
    }
  }

  public Page<DeletedDevice> getDeletedDevices(int page, int pageSize, String orderBy, String sortType, String search) {
    int pageNumber = page > 0 ? page : 1;

    Pageable paging = generatePageRequest(pageNumber, pageSize, orderBy, sortType);

    if (search == null) {
      return deletedDeviceRepository.findAll(paging);
    } else {
      return deletedDeviceRepository.findByContaining(search, paging);
    }
  }

  @Override
  public String generateOrderValue(String orderBy) {
    switch (orderBy) {
      case "type":
        return "model." + orderBy;
      case "setNumber":
        return "deviceSet";
      case "date":
        return "deletedDate";
      default:
        return orderBy;
    }
  }

  public String generateSortValue(String sortType) {
    if (sortType == null) {
      return "desc";
    } else {
      return sortType;
    }
  }

  public Device getSingleDevice(long id) throws ResourceNotFoundException {
    return deviceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Device not found for id: " + id));
  }

  public Page<Device> getCountedModels(int page) {
    Pageable paging = PageRequest.of(page, 10);
    return deviceRepository.getCountedModels(paging);
  }

  public Device insertDevice(DeviceRequest device) {

    Room room = roomService.findRoomById(device.getIdRoom());
    Model model = modelService.findModelById(device.getIdModel());
    Owner owner = ownerService.findOwnerById(device.getIdOwner());
    DeviceSet deviceSet = deviceSetService.findDeviceSetById(device.getIdDeviceSet());
    String serialNumber = device.getSerialNumber();
    String comment = "";

    if (device.getComment() != null) {
      comment = device.getComment();
    }

    Device newDevice = new Device(serialNumber, room, model, owner, deviceSet, comment);
    newDevice = deviceRepository.save(newDevice);
    newDevice.generateBarCode();
    newDevice = deviceRepository.save(newDevice);

    return newDevice;
  }

  public ResponseEntity<Device> updateDevice(long id, DeviceRequest details) throws ResourceNotFoundException {
    Room room = roomService.findRoomById(details.getIdRoom());
    Model model = modelService.findModelById(details.getIdModel());
    Owner owner = ownerService.findOwnerById(details.getIdOwner());
    DeviceSet deviceSet = deviceSetService.findDeviceSetById(details.getIdDeviceSet());
    String serialNumber = details.getSerialNumber();
    String comment = details.getComment();
    Device device = this.getSingleDevice(id);

    if (device.getRoom() != room) {
      historyService.insertHistory("device", device.getId(), "room", device.getRoom().getName(), room.getName());
    }

    if (device.getModel() != model) {
      historyService.insertHistory("device", device.getId(), "model", device.getModel().getName(), model.getName());
    }

    if (device.getOwner() != owner) {
      historyService.insertHistory("device", device.getId(), "owner", device.getOwner().getName(), owner.getName());
    }

    if (device.getDeviceSet() != deviceSet) {
      historyService.insertHistory("device", device.getId(), "device_set", device.getDeviceSet().getName(), deviceSet.getName());
    }

    if (!device.getSerialNumber().equals(serialNumber)) {
      historyService.insertHistory("device", device.getId(), "serial_number", device.getSerialNumber(), serialNumber);
    }

    device.setSerialNumber(serialNumber);
    device.setRoom(room);
    device.setDeviceSet(deviceSet);
    device.setModel(model);
    device.setOwner(owner);
    device.setComments(comment);
    final Device updatedDevice = deviceRepository.save(device);
    return ResponseEntity.ok(updatedDevice);
  }

  public DeletedDevice deleteDevice(long id) {
    Device device = deviceRepository.findById(id).orElseThrow();
    DeletedDevice deletedDevice = DeletedDevice.builder()
      .deviceSet(device.getDeviceSet())
      .barCode(device.getBarCode())
      .comments(device.getComments())
      .createdDate(device.getCreatedDate())
      .model(device.getModel())
      .owner(device.getOwner())
      .room(device.getRoom())
      .serialNumber(device.getSerialNumber())
      .build();
    deviceRepository.delete(device);
    return deletedDeviceRepository.save(deletedDevice);

  }

  public List<Long> getAllBarcodes() {
    return deviceRepository.getAllBarcodes();
  }

  public Device findByBarcode(Long barcode) {
    return deviceRepository.findByBarCode(barcode);
  }

  public List<Device> getDevicesFromRoom(Room room) {
    return deviceRepository.findDeviceByRoom(room);
  }

  public Device findById(Long id) {
    return deviceRepository.findById(id).orElseThrow();
  }
}
