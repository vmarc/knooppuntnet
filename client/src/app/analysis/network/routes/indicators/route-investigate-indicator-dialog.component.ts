import {ChangeDetectionStrategy} from "@angular/core";
import {Component, Inject} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: "kpn-route-investigate-indicator-dialog",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-indicator-dialog
      letter="F"
      i18n-letter="@@route-investigate-indicator.letter"
      [color]="color"
      (closeDialog)="onCloseDialog()">

      <span dialog-title *ngIf="isGreen()" i18n="@@route-investigate-indicator.green.title">
        OK - No facts
      </span>
      <div dialog-body *ngIf="isGreen()" i18n="@@route-investigate-indicator.green.text">
        No issues found during route analysis.
      </div>

      <span dialog-title *ngIf="isRed()" i18n="@@route-investigate-indicator.red.title">
        NOK - Investigate facts
      </span>
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
