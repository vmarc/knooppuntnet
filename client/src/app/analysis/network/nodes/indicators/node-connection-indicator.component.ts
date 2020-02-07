import {Component, Input} from "@angular/core";
import {MatDialog} from "@angular/material/dialog";
import {NodeConnectionIndicatorDialogComponent} from "./node-connection-indicator-dialog.component";
import {NetworkNodeInfo2} from "../../../../kpn/api/common/network/network-node-info2";

@Component({
  selector: "kpn-node-connection-indicator",
  template: `
    <kpn-indicator
      letter="C"
      i18n-letter="@@node-connection-indicator.letter"
      [color]="color"
      (openDialog)="onOpenDialog()">
    </kpn-indicator>
  `
})
export class NodeConnectionIndicatorComponent {

  @Input() node: NetworkNodeInfo2;

  constructor(private dialog: MatDialog) {
  }

  get color() {
    return this.node.connection ? "blue" : "gray";
  }

  onOpenDialog() {
    this.dialog.open(NodeConnectionIndicatorDialogComponent, {data: this.color});
  }

}
