import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { ConfigService } from "./config.service";
import { Observable } from "rxjs";
import { Owner } from "../../shared/models/owner.model";

@Injectable({
  providedIn: "root",
})
export class OwnerService {
  constructor(private http: HttpClient, private config: ConfigService) {}

  getAllOwnersWithDevicesCount(
    page: number,
    orderBy: string,
    sortType: string,
    searchValue?: string
  ): Observable<any> {
    var url =
      this.config.ownerUrl +
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

    return this.http.get(url);
  }

  getAllOwners() {
    var url = this.config.ownerUrl + "/all";
    return this.http.get(url);
  }

  insertOwner(owner: Owner): Observable<Owner> {
    return this.http.post<Owner>(this.config.ownerUrl, owner);
  }
}