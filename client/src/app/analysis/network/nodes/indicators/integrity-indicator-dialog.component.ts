import {Component, Inject} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {IntegrityIndicatorData} from "./integrity-indicator-data";

@Component({
  selector: "kpn-integrity-indicator-dialog",
  template: `
    <kpn-indicator-dialog
      letter="E"
      i18n-letter="@@integrity-indicator.letter"
      [color]="color"
      (closeDialog)="onCloseDialog()">

      <span dialog-title *ngIf="isGray()" i18n="@@integrity-indicator.gray.title">
        OK - expected route count missing
      </span>
      <markdown dialog-body *ngIf="isGray()" i18n="@@integrity-indicator.gray.text">
        This network node does not have an _"{{tag}}"_ tag.
        This is OK because the use of this tag is optional.
      </markdown>

      <span dialog-title *ngIf="isGreen()" i18n="@@integrity-indicator.green.title">
        OK - expected route count       
      </span>
      <markdown dialog-body *ngIf="isGreen()" i18n="@@integrity-indicator.green.text">
        The number of routes found in this network node ({{actual}}) does match the expected number of
        routes ({{expected}}) as defined in the _"{{tag}}"_ tag on this node. This is what we expect.
      </markdown>

      <span dialog-title *ngIf="isRed()" i18n="@@integrity-indicator.red.title">
        NOK - unexpected route count     
      </span>
      <markdown dialog-body *ngIf="isRed()" i18n="@@integrity-indicator.red.text">
        The number of routes found in this network node ({{actual}}) does not match the expected number of
        routes ({{expected}}) as defined in the _"{{tag}}"_ tag on this node.
      </markdown>

    </kpn-indicator-dialog>
  `
})
export class IntegrityIndicatorDialogComponent {

  constructor(private dialogRef: MatDialogRef<IntegrityIndicatorDialogComponent>,
              @Inject(MAT_DIALOG_DATA) private indicatorData: IntegrityIndicatorData) {
  }

  onCloseDialog(): void {
    this.dialogRef.close();
  }

  isGray() {
    return this.color === "gray";
  }

  isGreen() {
    return this.color === "green";
  }

  isRed() {
    return this.color === "red";
  }

  get color() {
    return this.indicatorData.color;
  }

  get tag() {
    return `expected_${this.indicatorData.networkType.id}_route_relations`;
  }

  get actual() {
    return this.indicatorData.actual;
  }

  get expected() {
    return this.indicatorData.expected;
  }

}
