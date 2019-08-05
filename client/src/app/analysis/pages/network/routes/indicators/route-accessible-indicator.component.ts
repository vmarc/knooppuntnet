import {Component, Input} from "@angular/core";
import {MatDialog} from "@angular/material";
import {RouteAccessibleIndicatorDialogComponent} from "./route-accessible-indicator-dialog.component";
import {NetworkRouteInfo} from "../../../../../kpn/shared/network/network-route-info";
import {NetworkType} from "../../../../../kpn/shared/network-type";
import {NetworkTypes} from "../../../../../kpn/common/network-types";
import {RouteAccessibleData} from "./route-accessible-data";

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

  @Input() route: NetworkRouteInfo;
  @Input() networkType: NetworkType;

  constructor(private dialog: MatDialog) {
  }

  onOpenDialog() {
    const data = new RouteAccessibleData(this.networkType, this.isAccessible(), this.color);
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
      color = this.isAccessible() ? "green" : "red";
    }
    return color;
  }

  private isAccessible(): boolean {
    return this.route.facts.indexOf(fact => fact.name === "RouteUnaccessible") < 0;
  }

}
