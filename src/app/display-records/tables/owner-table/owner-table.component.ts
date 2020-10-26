import { Component, ElementRef, Input, OnInit, ViewChild } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { OwnerService } from "../../../core/services/owner.service";
import { SubjectService } from "../../../core/services/subject.service";
import { Table } from "../table.class";
import { Data } from "../../../shared/data";

@Component({
  selector: "app-owner-table",
  templateUrl: "./owner-table.component.html",
  styleUrls: ["../table.scss"],
})
export class OwnerTableComponent extends Table implements OnInit {
  @ViewChild("name") serialNumberArrow: ElementRef;

  public tableData = [];
  public owners;

  constructor(
    subjectService: SubjectService,
    route: ActivatedRoute,
    private ownerService: OwnerService
  ) {
    super(subjectService, route, "name", "asc", [
      "Option one",
      "Option two",
      "Option three",
    ]);
  }

  ngOnInit() {
    this.tableData = Data.getOwnerTableData();
    this.initialize();
  }

  ngAfterViewInit() {
    this.currentArrow = document.getElementById("name");
    this.currentArrow.classList.add("active");
  }

  getRecords() {
    this.apiSub = this.ownerService
      .getAllOwnersWithDevicesCount(
        this.currentPage,
        this.sort.value,
        this.sort.type,
        this.searchValue
      )
      .subscribe((data) => {
        this.owners = data.content;
        this.subjectService.totalPageNumber.next(data.totalPages);
      });
  }
}
