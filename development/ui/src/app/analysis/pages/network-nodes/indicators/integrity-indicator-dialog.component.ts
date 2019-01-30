import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";

@Component({
  selector: 'kpn-integrity-indicator-dialog',
  template: `
    <kpn-indicator-dialog letter="C" color="{{color}}" (closeDialog)="onCloseDialog()">

      <span dialog-title *ngIf="isGray()">
        OK - expected route count missing
      </span>
      <markdown dialog-body *ngIf="isGray()">
        This network node does not have an $tag tag.
        This is OK because the use of this tag is optional.
      </markdown>

      <span dialog-title *ngIf="isGreen()">
        OK - expected route count       
      </span>
      <markdown dialog-body *ngIf="isGreen()">
        The number of routes found in this network node ($actual) does match the expected number of
        routes ($expected) as defined in the $tag tag on this node.
      </markdown>

      <span dialog-title *ngIf="isRed()">
        NOK - unexpected route count     
      </span>
      <div dialog-body *ngIf="isRed()">
        The number of routes found in this network node ($actual) does not match the expected number of
        routes ($expected) as defined in the $tag tag on this node.
      </div>

    </kpn-indicator-dialog>
  `
})
export class IntegrityIndicatorDialogComponent {

  tag = "TODO expected_rcn_route_relations";
  actual = 1;
  expected = 1;

  constructor(public dialogRef: MatDialogRef<IntegrityIndicatorDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public color: string) {
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

}
