package com.app.inventorysystemapp.service;

import com.app.inventorysystemapp.model.Owner;
import com.app.inventorysystemapp.model.interfaces.IOwner;
import com.app.inventorysystemapp.repository.OwnerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OwnerService {

  private final OwnerRepository ownerRepository;
  private final HistoryService historyService;

  public OwnerService(OwnerRepository ownerRepository, HistoryService historyService) {
    this.ownerRepository = ownerRepository;
    this.historyService = historyService;
  }

  public Page<IOwner> getOwners(int page,
                                int pageSize,
                                String orderBy,
                                String sortType,
                                String search) {
    Pageable paging;
    int pageNumber = page > 0 ? page : 1;

    if(orderBy != null){

      String order = orderBy;

      switch(orderBy){
        case "name":
          order = "ownerName";
          break;
        case "surname":
          order = "ownerSurname";
        case "count":
          order = "ownerItemsCount";
          break;
      }

      String type = "";

      if(sortType == null){
        type = "desc";
      }else{
        type = sortType;
      }

      if(type.equals("desc")){
        paging = PageRequest.of(pageNumber-1, pageSize, Sort.by(order).descending());
      }else{
        paging = PageRequest.of(pageNumber-1, pageSize, Sort.by(order));
      }

    }else{
      paging = PageRequest.of(pageNumber-1, pageSize);
    }

    if(search == null) {
      return ownerRepository.findAllOwnersWithItemsCount(paging);
    }else{
      return ownerRepository.findAllOwnersWithItemsCountByContaining(search, paging);
    }
  }

  public List<Owner> getAllOwners() {
    return ownerRepository.findAll();
  }

  public Owner insertOwner(Owner owner) {
    return ownerRepository.save(owner);
  }

  public Owner findOwnerById(long id){
    return ownerRepository.findById(id).orElseThrow();
  }

  public IOwner getSingleOwner(long id) {
    return ownerRepository.findByIdWithCount(id);
  }

  public ResponseEntity<Owner> updateOwner(long id, String name) {
    Owner owner = ownerRepository.findById(id).orElseThrow();

    if(!owner.getName().equals(name)){
      historyService.insertHistory("owner", owner.getId(), "name", owner.getName(), name);
    }

    owner.setName(name);

    final Owner updatedOwner = ownerRepository.save(owner);
    return ResponseEntity.ok(updatedOwner);
  }
}
