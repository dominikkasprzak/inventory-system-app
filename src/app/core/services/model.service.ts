import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { ConfigService } from "./config.service";
import { Observable } from "rxjs";
import { Model } from "../../shared/models/model.model";

@Injectable({
  providedIn: "root",
})
export class ModelService {
  constructor(private http: HttpClient, private config: ConfigService) {}

  getAllModelsWithDeviceCount(
    page: number,
    orderBy: string,
    sortType: string,
    searchValue?: string
  ): Observable<any> {
    var url =
      this.config.modelUrl +
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

  getAllModels(): Observable<any> {
    var url = this.config.modelUrl + "/all";
    return this.http.get(url);
  }

  getSingleModel(id: number) {
    return this.http.get(this.config.modelUrl + "/" + id);
  }

  insertModel(model: Model): Observable<Model> {
    console.log(model);
    return this.http.post<Model>(this.config.modelUrl, model);
  }

  updateModel(id: number, model: Model): Observable<Model> {
    return this.http.put<Model>(this.config.modelUrl + "/" + id, model);
  }

  deleteModel(id: number): Observable<any> {
    return this.http.delete(this.config.modelUrl + "/" + id);
  }
}
