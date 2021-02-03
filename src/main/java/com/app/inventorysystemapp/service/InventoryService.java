package com.app.inventorysystemapp.service;

import com.app.inventorysystemapp.model.*;
import com.app.inventorysystemapp.repository.InventoryItemRepository;
import com.app.inventorysystemapp.repository.InventoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryService implements com.app.inventorysystemapp.service.Service {

  private final InventoryRepository inventoryRepository;
  private final InventoryItemRepository inventoryItemRepository;
  private final RoomService roomService;
  private final ProductService productService;

  public InventoryService(InventoryRepository inventoryRepository, InventoryItemRepository inventoryItemRepository, RoomService roomService, ProductService productService) {
    this.inventoryRepository = inventoryRepository;
    this.inventoryItemRepository = inventoryItemRepository;
    this.roomService = roomService;
    this.productService = productService;
  }


  public Inventory insertInventory(Long idRoom, List<Long> barcodes) {
    Inventory inventory = insertNewInventory(idRoom);

    for(int i = 0; i < barcodes.size(); i++){
      Product product = productService.findByBarcode(barcodes.get(i));
      if(product != null){
        insertInventoryItem(inventory, product);
      }
    }

    return inventory;
  }

  public Inventory insertNewInventory(Long idRoom){
    Inventory inventory = new Inventory();

    Room room = roomService.findById(idRoom);
    inventory.setRoom(room);
    return inventoryRepository.save(inventory);
  }

  public InventoryItem insertInventoryItem(Inventory inventory, Product record){
    InventoryItem inventoryItem = new InventoryItem();
    inventoryItem.setProduct(record);
    inventoryItem.setInventory(inventory);
    return inventoryItemRepository.save(inventoryItem);
  }

  public Page<Inventory> getInventories(int page, int pageSize, String orderBy, String sortType, String search) {
    int pageNumber = page > 0 ? page : 1;

    Pageable paging = generatePageRequest(pageNumber, pageSize, orderBy, sortType);

    if(search == null){
      return inventoryRepository.findAll(paging);
    }else{
      return inventoryRepository.findByContaining(search, paging);
    }
  }

  public Report getReport(long id) {
    Report report = new Report();
    Inventory inventory = inventoryRepository.findById(id).orElseThrow();

    List<Product> previousStock = productService.getDevicesFromRoom(inventory.getRoom());
    List<InventoryItem> inventoryItems = inventoryItemRepository.findByInventory(inventory);
    List<Product> actualStock = new ArrayList<>();

    for(int i = 0; i < inventoryItems.size(); i++){
      actualStock.add(inventoryItems.get(i).getProduct());
    }

    List<Product> missingRecords = getDifferentDevices(previousStock, actualStock);
    List<Product> additionalRecords = getDifferentDevices(actualStock, previousStock);

    report.setActualStock(actualStock);
    report.setPreviousStock(previousStock);
    report.setMissingRecords(missingRecords);
    report.setAdditionalRecords(additionalRecords);
    report.setDate(inventory.getDate());
    report.setRoom(inventory.getRoom().getName());

    return report;
  }

  @Override
  public String generateOrderValue(String orderBy) {
    switch(orderBy){
      case "room":
        return "room.name";
      default:
        return orderBy;
    }
  }
}
