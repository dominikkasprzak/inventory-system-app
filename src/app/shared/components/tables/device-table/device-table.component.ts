import { Component, ElementRef, Input, OnInit, ViewChild } from "@angular/core";
import { SubjectService } from "../../../../service/subjectService";
import { SortInfo } from "../../../models/sortInfo.model";
import { Table } from "../table.class";
import { TableData } from "../table.data";

@Component({
  selector: "app-device-table",
  templateUrl: "./device-table.component.html",
  styleUrls: ["../table.component.scss"],
})
export class DeviceTableComponent extends Table implements OnInit {
  @Input() devices = [];
  @ViewChild("serialNumber") serialNumberArrow: ElementRef;

  tableData = [];

  constructor(subjectService: SubjectService) {
    super(subjectService, "serialNumber", "asc", [
      "Option one",
      "Option two",
      "Option three",
    ]);

    this.tableData = TableData.getDeviceTableData();
  }

  ngOnInit(): void {
    var sort = new SortInfo();
    sort.value = this.currentSortValue;
    sort.sortType = this.currentSortType;
    this.subjectService.sortValueEmitter.next(sort);
  }

  ngAfterViewInit() {
    this.currentArrow = document.getElementById("serialNumber");
  }
}
