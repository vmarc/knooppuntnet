import {Component, Inject} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";

@Component({
  selector: "kpn-route-connection-indicator-dialog",
  template: `
    <!--@@ letter V -->
    <kpn-indicator-dialog 
      letter="C"
      i18n-letter="@@route-connection.indicator.letter"
      [color]="color" 
      (closeDialog)="onCloseDialog()">

      <!--@@ OK - Verbinding -->
      <span dialog-title *ngIf="isBlue()" i18n="@@route-connection-indicator.blue.title">
        OK - Connection       
      </span>
      <!--@@ Deze route is een verbinding naar een ander netwerk. -->
      <div dialog-body *ngIf="isBlue()" i18n="@@route-connection-indicator.blue.text">
        This route is a connection to another network.
      </div>

      <!--@@ OK - Geen verbinding -->
      <span dialog-title *ngIf="isGray()" i18n="@@route-connection-indicator.gray.title">
       OK - No connection
      </span>
      <!--@@ Deze route is geen verbinding naar een ander netwerk. -->
      <div dialog-body *ngIf="isGray()" i18n="@@route-connection-indicator.gray.text">
        This route is not a connection to another network.
      </div>

    </kpn-indicator-dialog>
  `
})
export class RouteConnectionIndicatorDialogComponent {

  constructor(private dialogRef: MatDialogRef<RouteConnectionIndicatorDialogComponent>,
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
