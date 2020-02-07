import {Component, Input} from "@angular/core";
import {MatDialog} from "@angular/material/dialog";
import {RouteConnectionIndicatorDialogComponent} from "./route-connection-indicator-dialog.component";
import {NetworkRouteRow} from "../../../../kpn/api/common/network/network-route-row";

@Component({
  selector: "kpn-route-connection-indicator",
  template: `
    <!--@@ letter V -->
    <kpn-indicator
      letter="C"
      i18n-letter="@@route-connection-indicator.letter"
      [color]="color"
      (openDialog)="onOpenDialog()">
    </kpn-indicator>
  `
})
export class RouteConnectionIndicatorComponent {

  @Input() route: NetworkRouteRow;

  constructor(private dialog: MatDialog) {
  }

  get color() {
    return this.route.roleConnection ? "blue" : "gray";
  }

  onOpenDialog() {
    this.dialog.open(RouteConnectionIndicatorDialogComponent, {data: this.color});
  }

}
