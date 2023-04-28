import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'kpn-connection-indicator-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-indicator-dialog
      letter="C"
      i18n-letter="@@node-connection-indicator.letter"
      [color]="color"
    >
      <span
        dialog-title
        *ngIf="isBlue()"
        i18n="@@node-connection-indicator.blue.title"
      >
        OK - Connection
      </span>
      <markdown
        dialog-body
        *ngIf="isBlue()"
        i18n="@@node-connection-indicator.blue.text"
      >
        This node is a connection to another network. All routes to this node
        have the role *"connection"* in the network relation.
      </markdown>

      <span
        dialog-title
        *ngIf="isGray()"
        i18n="@@node-connection-indicator.gray.title"
      >
        OK - No connection
      </span>
      <markdown
        dialog-body
        *ngIf="isGray()"
        i18n="@@node-connection-indicator.gray.text"
      >
        This node is not a connection to another network. The node would have
        been considered as a connection to another network if all routes to this
        node within the network had role *"connection"* in the network relation.
      </markdown>
    </kpn-indicator-dialog>
  `,
})
export class NodeConnectionIndicatorDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public color: string) {}

  isBlue() {
    return this.color === 'blue';
  }

  isGray() {
    return this.color === 'gray';
  }
}
