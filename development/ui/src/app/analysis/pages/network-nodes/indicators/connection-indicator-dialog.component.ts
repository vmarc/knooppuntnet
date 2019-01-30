import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";

@Component({
  selector: 'kpn-connection-indicator-dialog',
  template: `
    <kpn-indicator-dialog letter="C" color="{{color}}" (closeDialog)="onCloseDialog()">

      <span dialog-title *ngIf="isBlue()">
        OK - Connection
      </span>
      <div dialog-body *ngIf="isBlue()">
        This node is a connection to another network. All routes to this
        node have the role "connection" in the network relation.
      </div>

      <span dialog-title *ngIf="isGray()">
        OK - No connection
      </span>
      <div dialog-body *ngIf="isGray()">
        This node is not a connection to another network. The node would have been considered
        as a connection to another network if all routes to this node within the network had
        role "connection" in the network relation.
      </div>

    </kpn-indicator-dialog>
  `
})
export class ConnectionIndicatorDialogComponent {

  constructor(private dialogRef: MatDialogRef<ConnectionIndicatorDialogComponent>,
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
