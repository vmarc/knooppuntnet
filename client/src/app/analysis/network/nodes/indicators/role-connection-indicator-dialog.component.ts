import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';

@Component({
  selector: 'kpn-role-connection-indicator-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <kpn-indicator-dialog letter="C" i18n-letter="@@role-connection-indicator.letter" [color]="color">

      <span dialog-title *ngIf="isBlue()" i18n="@@role-connection-indicator.blue.title">
        OK - Connection
      </span>
      <markdown dialog-body *ngIf="isBlue()" i18n="@@role-connection-indicator.blue.text">
        This node is a connection to another network.
        This node has role _"connection"_ in the network relation.
      </markdown>

      <span dialog-title *ngIf="isGray()" i18n="@@role-connection-indicator.gray.title">
        OK - No connection role
      </span>
      <markdown dialog-body *ngIf="isGray()" i18n="@@role-connection-indicator.gray.text">
        This node does not have role _"connection"_ in het network relation.
      </markdown>

    </kpn-indicator-dialog>
  `
})
export class RoleConnectionIndicatorDialogComponent {

  constructor(@Inject(MAT_DIALOG_DATA) public color: string) {
  }

  isBlue() {
    return this.color === 'blue';
  }

  isGray() {
    return this.color === 'gray';
  }
}
