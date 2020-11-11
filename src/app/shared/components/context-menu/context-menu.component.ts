import {
  Component,
  ElementRef,
  HostListener,
  OnInit,
  Renderer2,
  ViewChild,
} from "@angular/core";
import { SubjectService } from "../../../core/services/subject.service";
import { ContextMenu } from "../../models/context-menu.model";

@Component({
  selector: "app-context-menu",
  templateUrl: "./context-menu.component.html",
  styleUrls: ["./context-menu.component.scss"],
})
export class ContextMenuComponent implements OnInit {
  @ViewChild("contextMenu") contextMenu: ElementRef;

  public options;

  private recordId;
  private open = false;
  private contextMenuOptions;

  constructor(
    private subjectService: SubjectService,
    private renderer: Renderer2,
    private hostElement: ElementRef
  ) {}

  ngOnInit(): void {
    this.subjectService.contextMenuEmitter.subscribe((event) => {
      if (event instanceof ContextMenu) {
        var contextMenu = this.hostElement.nativeElement;
        this.renderer.setStyle(contextMenu, "top", event.mouseY - 30 + "px");
        this.renderer.setStyle(contextMenu, "left", event.mouseX - 320 + "px");
        this.renderer.setStyle(contextMenu, "display", "block");
        this.contextMenuOptions = event.options;
        this.recordId = event.recordId;
        this.setOptions();
        setTimeout(() => {
          this.open = true;
        }, 50);
      }
    });
  }

  setOptions() {
    this.options = [];
    for (let option of this.contextMenuOptions) {
      let action = option.action;
      let name = option.name;
      this.options.push({ name: name, param: this.getQueryParam(action) });
    }
  }

  getQueryParam(action) {
    let param;

    switch (action) {
      case "edit":
        param = { edit: this.recordId };
        break;

      case "delete":
        param = { delete: this.recordId };
        break;

      case "addBarcode":
        param = { addBarcode: this.recordId };
        break;
      default:
        param = {};
    }

    return param;
  }

  @HostListener("document:click", ["$event"])
  onLeftClick(event) {
    var contextMenu = this.hostElement.nativeElement;
    if (this.open) {
      this.renderer.setStyle(contextMenu, "display", "none");
      this.subjectService.contextMenuEmitter.next("closed");
      this.open = false;
    }
  }
}
