import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { ConfigService } from "./config.service";
import { Observable } from "rxjs";

@Injectable({
  providedIn: "root",
})
export class HistoryService {
  constructor(private http: HttpClient, private config: ConfigService) {}

  getGlobaHistory(
    page: number,
    orderBy: string,
    sortType: string,
    searchValue?: string
  ): Observable<any> {
    var url =
      this.config.historyUrl +
      this.config.page +
      page +
      this.config.pageSize +
      this.config.sortType +
      sortType +
      this.config.orderBy +
      orderBy;

    if (searchValue != "") {
      url += this.config.search + searchValue;
    }

    console.log(url);

    return this.http.get(url);
  }

  getDevicesHistory(
    page: number,
    orderBy: string,
    sortType: string,
    searchValue?: string
  ): Observable<any> {
    var url =
      this.config.deviceHistoryUrl +
      this.config.page +
      page +
      this.config.pageSize +
      this.config.sortType +
      sortType +
      this.config.orderBy +
      orderBy;

    if (searchValue !== "" && searchValue !== undefined) {
      url += this.config.search + searchValue;
    }

    return this.http.get(url);
  }

  getSingleDeviceHistory(page: number, id: number): Observable<any> {
    var url =
      this.config.deviceHistoryUrl +
      "/" +
      id +
      this.config.page +
      page +
      this.config.pageSize;

    return this.http.get(url);
  }
}
