import { Injectable } from "@angular/core";
import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { ConfigService } from "./config.service";
import { Observable, throwError } from "rxjs";
import { Device } from "app/shared/models/device.model";
import { catchError } from "rxjs/operators";

@Injectable({
  providedIn: "root",
})
export class DeviceService {
  constructor(private http: HttpClient, private config: ConfigService) {}

  getAllDevices(
    page: number,
    orderBy: string,
    sortType: string,
    searchValue?: string
  ): Observable<any> {
    var url =
      this.config.deviceUrl +
      this.config.page +
      page +
      this.config.pageSize +
      this.config.sortType +
      sortType +
      this.config.orderBy +
      orderBy;

    if (searchValue !== "") {
      url += this.config.search + searchValue;
    }

    return this.http.get(url);
  }

  getDeletedDevice(
    page: number,
    orderBy: string,
    sortType: string,
    searchValue?: string
  ): Observable<any> {
    var url =
      this.config.deviceUrl +
      "/deleted" +
      this.config.page +
      page +
      this.config.pageSize +
      this.config.sortType +
      sortType +
      this.config.orderBy +
      orderBy;

    if (searchValue !== "") {
      url += this.config.search + searchValue;
    }

    return this.http.get(url);
  }

  getSingleDevice(id: number) {
    return this.http.get(this.config.deviceUrl + "/" + id);
  }

  getAllBarcodes() {
    return this.http.get(this.config.allBarcodesUrl);
  }

  insertDevice(device: Device): Observable<Device> {
    return this.http.post<Device>(this.config.deviceUrl, device);
  }

  updateDevice(id: number, device: Device): Observable<Device> {
    return this.http.put<Device>(this.config.deviceUrl + "/" + id, device);
  }

  deleteDevice(id: number): Observable<any> {
    return this.http.delete(this.config.deviceUrl + "/" + id);
  }
}
