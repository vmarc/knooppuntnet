import {Component, Input} from "@angular/core";
import {MatDialog} from "@angular/material";
import {RouteAccessibleIndicatorDialogComponent} from "./route-accessible-indicator-dialog.component";
import {NetworkType} from "../../../../kpn/shared/network-type";
import {NetworkTypes} from "../../../../kpn/common/network-types";
import {RouteAccessibleData} from "./route-accessible-data";
import {NetworkRouteRow} from "../../../../kpn/shared/network/network-route-row";

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
    if (NetworkTypes.horse.id === this.networkType.id || NetworkTypes.inlineSkating.id === this.networkType.id) {
      color = "gray";
    } else if (NetworkTypes.cycling.id === this.networkType.id ||
      NetworkTypes.hiking.id === this.networkType.id ||
      NetworkTypes.motorboat.id === this.networkType.id ||
      NetworkTypes.canoe.id === this.networkType.id) {
      color = this.route.accessible ? "green" : "red";
    }
    return color;
  }

}
