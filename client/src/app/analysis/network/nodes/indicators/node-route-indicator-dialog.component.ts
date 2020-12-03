import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';

@Component({
  selector: 'kpn-node-route-indicator-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <kpn-indicator-dialog letter="R" i18n-letter="@@route-indicator.letter" [color]="color">

      <span dialog-title *ngIf="isGreen()" i18n="@@route-indicator.green.title">
        OK - Defined in route relation
      </span>
      <div dialog-body *ngIf="isGreen()" i18n="@@route-indicator.green.text">
        This node is included as a member in one or more route relations.
      </div>

      <span dialog-title *ngIf="isGray()" i18n="@@route-indicator.gray.title">
        OK - Not defined in route relation
      </span>
      <div dialog-body *ngIf="isGray()" i18n="@@route-indicator.gray.text">
        This node is not included as a member in any route relations. This is OK as including the
        node as member in the route relations is optional.
      </div>

    </kpn-indicator-dialog>
  `
})
export class NodeRouteIndicatorDialogComponent {

  constructor(@Inject(MAT_DIALOG_DATA) public color: string) {
  }

  isGreen() {
    return this.color === 'green';
  }

  isGray() {
    return this.color === 'gray';
  }
}
