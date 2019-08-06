import {Component, Input} from "@angular/core";
import {MatDialog} from "@angular/material";
import {RouteInvestigateIndicatorDialogComponent} from "./route-investigate-indicator-dialog.component";
import {NetworkRouteRow} from "../../../../../kpn/shared/network/network-route-row";

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

  @Input() route: NetworkRouteRow;

  constructor(private dialog: MatDialog) {
  }

  onOpenDialog() {
    this.dialog.open(RouteInvestigateIndicatorDialogComponent, {data: this.color});
  }

  get color() {
    return this.route.investigate ? "red" : "green";
  }

}
