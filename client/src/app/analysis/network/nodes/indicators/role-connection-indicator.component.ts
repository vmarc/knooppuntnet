import {Component, Input} from "@angular/core";
import {MatDialog} from "@angular/material";
import {RoleConnectionIndicatorDialogComponent} from "./role-connection-indicator-dialog.component";
import {NetworkNodeInfo2} from "../../../../kpn/api/common/network/network-node-info2";

@Component({
  selector: "kpn-role-connection-indicator",
  template: `
    <kpn-indicator 
      letter="C"
      i18n-letter="@@role-connection-indicator.letter"
      [color]="color" 
      (openDialog)="onOpenDialog()">
    </kpn-indicator>
  `
})
export class RoleConnectionIndicatorComponent {

  @Input() node: NetworkNodeInfo2;

  constructor(private dialog: MatDialog) {
  }

  onOpenDialog() {
    this.dialog.open(RoleConnectionIndicatorDialogComponent, {data: this.color});
  }

  get color() {
    return this.node.roleConnection ? "blue" : "gray";
  }

}
