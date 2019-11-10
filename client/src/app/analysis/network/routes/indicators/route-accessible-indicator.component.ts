import {Component, Input} from "@angular/core";
import {MatDialog} from "@angular/material";
import {NetworkType} from "../../../../kpn/api/custom/network-type";
import {NetworkRouteRow} from "../../../../kpn/api/common/network/network-route-row";
import {RouteAccessibleData} from "./route-accessible-data";
import {RouteAccessibleIndicatorDialogComponent} from "./route-accessible-indicator-dialog.component";

@Component({
  selector: "kpn-route-accessible-indicator",
  template: `
    <!--@@ letter T -->
    <kpn-indicator
      letter="A"
      i18n-letter="@@route-accessible-indicator.letter"
      [color]="color"
      (openDialog)="onOpenDialog()">
    </kpn-indicator>
  `
})
export class RouteAccessibleIndicatorComponent {

  @Input() route: NetworkRouteRow;
  @Input() networkType: NetworkType;

  constructor(private dialog: MatDialog) {
  }

  onOpenDialog() {
    const data = new RouteAccessibleData(this.networkType, this.route.accessible, this.color);
    this.dialog.open(RouteAccessibleIndicatorDialogComponent, {data: data});
  }

  get color() {
    let color = "gray";
    if (NetworkType.horseRiding.name === this.networkType.name || NetworkType.inlineSkating.name === this.networkType.name) {
      color = "gray";
    } else if (NetworkType.cycling.name === this.networkType.name ||
      NetworkType.hiking.name === this.networkType.name ||
      NetworkType.motorboat.name === this.networkType.name ||
      NetworkType.canoe.name === this.networkType.name) {
      color = this.route.accessible ? "green" : "red";
    }
    return color;
  }

}
