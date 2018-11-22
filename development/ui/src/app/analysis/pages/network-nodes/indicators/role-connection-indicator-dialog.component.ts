import {Component, Inject} from '@angular/core';
import {MatDialogRef} from "@angular/material";
import {MAT_DIALOG_DATA} from "@angular/material/typings/dialog";

@Component({
  selector: 'kpn-role-connection-indicator-dialog',
  template: `
    <kpn-indicator-dialog letter="C" color="{{color}}" (closeDialog)="onCloseDialog()">

      <span dialog-title *ngIf="isBlue()">
        OK - Connection        
      </span>
      <div dialog-body *ngIf="isBlue()">
        This node is a connection to another network.
        This node has role "connection" in the network relation.
      </div>

      <span dialog-title *ngIf="isGray()">
        OK - No connection role
      </span>
      <div dialog-body *ngIf="isGray()">
        This node does not have role "connection" in het network relation.
      </div>

    </kpn-indicator-dialog>
  `,
  styles: []
})
export class RoleConnectionIndicatorDialogComponent {

  constructor(public dialogRef: MatDialogRef<RoleConnectionIndicatorDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public color: string) {
  }

  onCloseDialog(): void {
    this.dialogRef.close();
  }

  isBlue() {
    return this.color === "blue";
  }

  isGray() {
    return this.color === "gray";
  }
}
