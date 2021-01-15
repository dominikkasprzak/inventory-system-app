import { Component, OnInit, ElementRef, ViewChild } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { DeviceService } from "app/core/services/device.service";
import { PrintableBarcodes } from "../shared/printableBarcodes";
import { Subscription } from "rxjs";
import { SubjectService } from "../core/services/subject.service";

@Component({
  selector: "app-display-records",
  templateUrl: "./display-records.component.html",
  styleUrls: ["./display-records.component.scss"],
})
export class DisplayRecordsComponent implements OnInit {
  @ViewChild("searchBar") searchBar: ElementRef;
  @ViewChild("device") deviceTableButton: ElementRef;
  @ViewChild("popup") popup;
  @ViewChild("alert") alert;
  @ViewChild("device") deviceElement;

  public records = [];
  public totalPages: number;
  public currentPage = 1;
  public searchValue = "";
  public currentRoute;
  public blured = false;
  public deviceForm;

  private deviceId;
  private search = "";
  private searchTimeout;
  private routeSub: Subscription;
  private alertSub: Subscription;
  private activeItem = null;

  constructor(
    private subjectService: SubjectService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private deviceService: DeviceService
  ) {}

  ngOnInit(): void {
    this.subjectService.totalPageNumber.subscribe((totalPages) => {
      this.totalPages = totalPages;
    }).unsubscribe;
  }

  ngAfterViewInit() {
    this.routeSub = this.activatedRoute.queryParams.subscribe((params) => {
      if (params["edit"] !== undefined) {
        this.blured = true;
      } else {
        this.blured = false;
      }

      if (params["addBarcode"] !== undefined) {
        this.deviceId = params["addBarcode"];
        this.addBarcode();
      }

      if (params["delete"] !== undefined) {
        this.deviceId = params["delete"];
        this.alert.trigger();
      }
    });

    this.alertSub = this.subjectService.alert.subscribe((accept) => {
      if (accept) {
        this.deleteRecord(this.deviceId);
      }
    });

    this.activeItem = this.deviceElement.nativeElement;
    this.activeItem.classList.add("active");
  }

  ngOnDestroy() {
    this.routeSub.unsubscribe();
    this.alertSub.unsubscribe();
  }

  deleteRecord(id: number) {
    this.deviceService.deleteDevice(id).subscribe((device) => {
      console.log(device);
    });
  }

  addBarcode() {
    this.deviceService.getSingleDevice(this.deviceId).subscribe((device) => {
      let barcode = device["barCode"];
      let model = device["model"]["name"];
      let serialNumber = device["serialNumber"];
      let success = PrintableBarcodes.addBarcode(barcode, serialNumber, model);

      if (success) {
        this.popup.triggerSuccess();
      } else {
        this.popup.triggerFailure();
      }
    });
  }

  onTyping() {
    this.search = this.searchBar.nativeElement.value;
    this.resetSearchTimeout();
  }

  resetSearchTimeout() {
    window.clearTimeout(this.searchTimeout);
    this.searchTimeout = window.setTimeout(() => {
      this.searchValue = this.search;
      this.router.navigate([], {
        queryParams: { search: this.searchValue },
        queryParamsHandling: "merge",
      });
    }, 500);
  }

  updateRoute() {
    if (!this.router.url.includes("edit")) {
      this.router.navigate(["/display-records/" + this.currentRoute], {
        queryParams: { page: this.currentPage, search: this.searchValue },
      });
    }
  }

  setLink(link, element) {
    this.setActiveItem(element);

    this.router.navigate(["/display-records/" + link], {
      queryParams: { page: 1 },
    });
  }

  setActiveItem(element) {
    if (this.activeItem !== null) {
      this.activeItem.classList.remove("active");
    }
    this.activeItem = element;
    this.activeItem.classList.add("active");
  }
}
