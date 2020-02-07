import {Component, Input} from "@angular/core";
import {MatDialog} from "@angular/material/dialog";
import {NetworkIndicatorDialogComponent} from "./network-indicator-dialog.component";
import {NetworkNodeInfo2} from "../../../../kpn/api/common/network/network-node-info2";

@Component({
  selector: "kpn-network-indicator",
  template: `
    <kpn-indicator
      letter="N"
      i18n-letter="@@network-indicator.letter"
      [color]="color"
      (openDialog)="onOpenDialog()">
    </kpn-indicator>
  `
})
export class NetworkIndicatorComponent {

  @Input() node: NetworkNodeInfo2;

  constructor(private dialog: MatDialog) {
  }

  get color() {
    let color = "";
    if (this.node.definedInRelation) {
      if (this.node.connection && !this.node.roleConnection) {
        color = "orange";
      } else {
        color = "green";
      }
    } else {
      if (this.node.connection) {
        color = "gray";
      } else {
        color = "red";
      }
    }
    return color;
  }

  onOpenDialog() {
    this.dialog.open(NetworkIndicatorDialogComponent, {data: this.color});
  }

}
