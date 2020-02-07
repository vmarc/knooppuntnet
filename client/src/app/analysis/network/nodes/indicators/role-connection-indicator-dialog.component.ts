import {Component, Inject} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: "kpn-role-connection-indicator-dialog",
  template: `
    <kpn-indicator-dialog
      letter="C"
      i18n-letter="@@role-connection-indicator.letter"
      [color]="color"
      (closeDialog)="onCloseDialog()">

      <span dialog-title *ngIf="isBlue()" i18n="@@role-connection-indicator.blue.title">
        OK - Connection
      </span>
      <div dialog-body *ngIf="isBlue()" i18n="@@role-connection-indicator.blue.text">
        This node is a connection to another network.
        This node has role "connection" in the network relation.
      </div>

      <span dialog-title *ngIf="isGray()" i18n="@@role-connection-indicator.gray.title">
        OK - No connection role
      </span>
      <div dialog-body *ngIf="isGray()" i18n="@@role-connection-indicator.gray.text">
        This node does not have role "connection" in het network relation.
      </div>

    </kpn-indicator-dialog>
  `
})
export class RoleConnectionIndicatorDialogComponent {

  constructor(private dialogRef: MatDialogRef<RoleConnectionIndicatorDialogComponent>,
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
