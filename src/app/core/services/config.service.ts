export class ConfigService {
  private serverApiUrl = "http://localhost:4201";
  private _deviceUrl = this.serverApiUrl + "/products";
  private _allBarcodesUrl = this.deviceUrl + "/all/barcodes";
  private _modelUrl = this.serverApiUrl + "/models";
  private _deviceSetUrl = this.serverApiUrl + "/product-sets";
  private _deviceTypeUrl = this.serverApiUrl + "/product-types";
  private _ownerUrl = this.serverApiUrl + "/owners";
  private _roomUrl = this.serverApiUrl + "/rooms";
  private _historyUrl = this.serverApiUrl + "/history";
  private _deviceHistoryUrl = this._historyUrl + "/products";
  private _inventoryUrl = this.serverApiUrl + "/inventory";
  private _reportUrl = this._inventoryUrl + "/report";

  private _barcodeGeneratorUrl = "http://barcodes4.me/barcode/c128b";

  private PAGE_SIZE = 10;

  private _inventoryRecordValue = "?idRecord=";
  private _inventoryRecordType = "&recordType=";
  private _page = "?page=";
  private _page_size = "&pageSize=" + this.PAGE_SIZE;
  private _sortType = "&sortType=";
  private _orderBy = "&orderBy=";
  private _search = "&search=";

  get deviceUrl(): string {
    return this._deviceUrl;
  }

  get allBarcodesUrl(): string {
    return this._allBarcodesUrl;
  }

  get modelUrl(): string {
    return this._modelUrl;
  }

  get deviceSetUrl(): string {
    return this._deviceSetUrl;
  }

  get deviceTypeUrl(): string {
    return this._deviceTypeUrl;
  }

  get ownerUrl(): string {
    return this._ownerUrl;
  }

  get roomUrl(): string {
    return this._roomUrl;
  }

  get historyUrl(): string {
    return this._historyUrl;
  }

  get deviceHistoryUrl(): string {
    return this._deviceHistoryUrl;
  }

  get inventoryUrl(): string {
    return this._inventoryUrl;
  }

  get reportUrl(): string {
    return this._reportUrl;
  }

  get inventoryRecordValue(): string {
    return this._inventoryRecordValue;
  }

  get inventoryRecordType(): string {
    return this._inventoryRecordType;
  }

  get page(): string {
    return this._page;
  }

  get pageSize(): string {
    return this._page_size;
  }

  get sortType(): string {
    return this._sortType;
  }

  get orderBy(): string {
    return this._orderBy;
  }

  get search(): string {
    return this._search;
  }

  get barcodeGeneratorUrl(): string {
    return this._barcodeGeneratorUrl;
  }
}
