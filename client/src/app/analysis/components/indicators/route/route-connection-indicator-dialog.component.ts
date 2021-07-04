import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'kpn-route-connection-indicator-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-indicator-dialog
      letter="C"
      i18n-letter="@@route-connection.indicator.letter"
      [color]="color"
    >
      <span
        dialog-title
        *ngIf="isBlue()"
        i18n="@@route-connection-indicator.blue.title"
      >
        OK - Connection
      </span>

      <div
        dialog-body
        *ngIf="isBlue()"
        i18n="@@route-connection-indicator.blue.text"
      >
        This route is a connection to another network.
      </div>

      <span
        dialog-title
        *ngIf="isGray()"
        i18n="@@route-connection-indicator.gray.title"
      >
        OK - No connection
      </span>

      <div
        dialog-body
        *ngIf="isGray()"
        i18n="@@route-connection-indicator.gray.text"
      >
        This route is not a connection to another network.
      </div>
    </kpn-indicator-dialog>
  `,
})
export class RouteConnectionIndicatorDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public color: string) {}

  isBlue() {
    return this.color === 'blue';
  }

  isGray() {
    return this.color === 'gray';
  }
}
