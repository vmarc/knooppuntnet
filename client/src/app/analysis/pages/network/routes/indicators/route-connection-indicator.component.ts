import {Component, Input} from "@angular/core";
import {MatDialog} from "@angular/material";
import {NetworkRouteInfo} from "../../../../../kpn/shared/network/network-route-info";
import {RouteConnectionIndicatorDialogComponent} from "./route-connection-indicator-dialog.component";

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

  @Input() route: NetworkRouteInfo;

  constructor(private dialog: MatDialog) {
  }

  onOpenDialog() {
    this.dialog.open(RouteConnectionIndicatorDialogComponent, {data: this.color});
  }

  get color() {
    return this.isConnection() ? "blue" : "gray";
  }

  private isConnection(): boolean {
    return this.route.role && this.route.role === "connection"
  }

}
