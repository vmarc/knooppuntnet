import {Component, Input} from "@angular/core";
import {MatDialog} from "@angular/material";
import {RouteIndicatorDialogComponent} from "./route-indicator-dialog.component";
import {NetworkNodeInfo2} from "../../../../kpn/shared/network/network-node-info2";

@Component({
  selector: "kpn-route-indicator",
  template: `
    <kpn-indicator
      letter="R"
      i18n-letter="@@route-indicator.letter"
      [color]="color()"
      (openDialog)="onOpenDialog()">
    </kpn-indicator>
  `
})
export class RouteIndicatorComponent {

  @Input() node: NetworkNodeInfo2;

  constructor(private dialog: MatDialog) {
  }

  onOpenDialog() {
    this.dialog.open(RouteIndicatorDialogComponent, {data: this.color()});
  }

  color() {
    return this.node.definedInRoute ? "green" : "gray";
  }

}
