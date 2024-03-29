package com.app.inventorysystemapp.model.interfaces;

import java.time.LocalDateTime;

public interface IHistoryProduct {
  String getSerialNumber();
  String getInventoryNumber();
  long getBarCode();
  String getChangedAttribute();
  String getOldValue();
  String getNewValue();
  LocalDateTime getDate();
}
