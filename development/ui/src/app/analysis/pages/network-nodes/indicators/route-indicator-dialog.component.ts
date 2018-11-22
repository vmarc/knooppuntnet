import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";

@Component({
  selector: 'kpn-route-indicator-dialog',
  template: `
    <kpn-indicator-dialog letter="R" color="{{color}}" (closeDialog)="onCloseDialog()">

      <span dialog-title *ngIf="isGreen()">
        OK - Defined in route relation        
      </span>
      <div dialog-body *ngIf="isGreen()">
        This node is included as a member in one or more route relations.
      </div>

      <span dialog-title *ngIf="isGray()">
        OK - Defined in route relation
      </span>
      <div dialog-body *ngIf="isGray()">
        This node is included as a member in one or more route relations.
      </div>

    </kpn-indicator-dialog>
  `
})
export class RouteIndicatorDialogComponent {

  constructor(private dialogRef: MatDialogRef<RouteIndicatorDialogComponent>,
              @Inject(MAT_DIALOG_DATA) private color: string) {
  }

  onCloseDialog(): void {
    this.dialogRef.close();
  }

  isGreen() {
    return this.color === "green";
  }

  isGray() {
    return this.color === "gray";
  }
}
