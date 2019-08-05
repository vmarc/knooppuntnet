import {Component, Inject} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";

@Component({
  selector: "kpn-route-investigate-indicator-dialog",
  template: `
    <!--@@ letter F -->
    <kpn-indicator-dialog
      letter="F"
      i18n-letter="@@route-investigate-indicator.letter"
      [color]="color"
      (closeDialog)="onCloseDialog()">

      <!--@ OK - Geen feiten -->
      <span dialog-title *ngIf="isGreen()" i18n="@@route-investigate-indicator.green.title">
        OK - No facts       
      </span>
      <!--@ Geen problemen gevonden tijdens route analyse. -->
      <div dialog-body *ngIf="isGreen()" i18n="@@route-investigate-indicator.green.text">
        No issues found during route analysis.
      </div>

      <!--@ NOK - Onderzoek feiten -->
      <span dialog-title *ngIf="isRed()" i18n="@@route-investigate-indicator.red.title">
        NOK - Investigate facts
      </span>
      <!--@@ Er is iets mis met deze route. -->
      <div dialog-body *ngIf="isRed()" i18n="@@route-investigate-indicator.red.text">
        Something is wrong with this route.
      </div>

    </kpn-indicator-dialog>
  `
})
export class RouteInvestigateIndicatorDialogComponent {

  constructor(private dialogRef: MatDialogRef<RouteInvestigateIndicatorDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public color: string) {
  }

  onCloseDialog(): void {
    this.dialogRef.close();
  }

  isGreen() {
    return this.color === "green";
  }

  isRed() {
    return this.color === "red";
  }
}
