import {Component, Input} from "@angular/core";
import {MatDialog} from "@angular/material";
import {IntegrityIndicatorDialogComponent} from "./integrity-indicator-dialog.component";
import {NetworkNodeInfo2} from "../../../../kpn/api/common/network/network-node-info2";
import {NetworkType} from "../../../../kpn/api/custom/network-type";
import {IntegrityIndicatorData} from "./integrity-indicator-data";

@Component({
  selector: "kpn-integrity-indicator",
  template: `
    <kpn-indicator
      letter="E"
      i18n-letter="@@integrity-indicator.letter"
      [color]="color"
      (openDialog)="onOpenDialog()">
    </kpn-indicator>
  `
})
export class IntegrityIndicatorComponent {

  @Input() networkType: NetworkType;
  @Input() node: NetworkNodeInfo2;

  constructor(private dialog: MatDialog) {
  }

  onOpenDialog() {
    const indicatorData = new IntegrityIndicatorData(
      this.color,
      this.networkType,
      this.node.integrityCheck ? this.node.integrityCheck.actual : 0,
      this.node.integrityCheck ? this.node.integrityCheck.expected : 0
    );
    this.dialog.open(IntegrityIndicatorDialogComponent, {data: indicatorData});
  }

  get color() {
    let color = "gray";
    if (this.node.integrityCheck) {
      if (this.node.integrityCheck.failed) {
        color = "red";
      } else {
        color = "green";
      }
    } else {
      color = "gray";
    }
    return color;
  }

}
