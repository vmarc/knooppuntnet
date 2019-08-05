import {Component, Input} from "@angular/core";
import {MatDialog} from "@angular/material";
import {NetworkRouteInfo} from "../../../../../kpn/shared/network/network-route-info";
import {RouteInvestigateIndicatorDialogComponent} from "./route-investigate-indicator-dialog.component";

@Component({
  selector: "kpn-route-investigate-indicator",
  template: `
    <!--@@ letter F -->
    <kpn-indicator
      letter="F"
      i18n-letter="@@route-investigate-indicator.letter"
      [color]="color"
      (openDialog)="onOpenDialog()">
    </kpn-indicator>
  `
})
export class RouteInvestigateIndicatorComponent {

  @Input() route: NetworkRouteInfo;

  constructor(private dialog: MatDialog) {
  }

  onOpenDialog() {
    this.dialog.open(RouteInvestigateIndicatorDialogComponent, {data: this.color});
  }

  get color() {
    return this.isRouteBroken() ? "red" : "green";
  }

  private isRouteBroken(): boolean {
    return this.route.facts.indexOf(fact => fact.name === "RouteBroken") >= 0;
  }

}
