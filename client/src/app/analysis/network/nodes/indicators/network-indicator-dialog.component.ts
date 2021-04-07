import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'kpn-network-indicator-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-indicator-dialog
      letter="N"
      i18n-letter="@@network-indicator.letter"
      [color]="color"
    >
      <span
        dialog-title
        *ngIf="isOrange()"
        i18n="@@network-indicator.orange.title"
      >
        Unexpected - Defined in network relation
      </span>
      <markdown
        dialog-body
        *ngIf="isOrange()"
        i18n="@@network-indicator.orange.text"
      >
        This node is included as a member in the network relation. We did not
        expect this, because all routes to this node have role _"connection"_.
        This would mean that the node is part of another network. We expect that
        the node is not included in the network relation, unless it receives the
        role _"connection"_.
      </markdown>

      <span
        dialog-title
        *ngIf="isGreen()"
        i18n="@@network-indicator.green.title"
      >
        OK - Defined in network relation
      </span>
      <div dialog-body *ngIf="isGreen()" i18n="@@network-indicator.green.text">
        This node is included as a member in the network relation. This is what
        we expect.
      </div>

      <span dialog-title *ngIf="isGray()" i18n="@@network-indicator.gray.title">
        OK - Not defined in network relation
      </span>
      <markdown
        dialog-body
        *ngIf="isGray()"
        i18n="@@network-indicator.gray.text"
      >
        This node is not included as a member in the network relation. This is
        OK. This node must belong to a different network, because all routes to
        this node within this network have the role _"connection"_ in the
        network relation.
      </markdown>

      <span dialog-title *ngIf="isRed()" i18n="@@network-indicator.red.title">
        Not OK - Not defined in network relation
      </span>
      <markdown dialog-body *ngIf="isRed()" i18n="@@network-indicator.red.text">
        This node is not included as a member in the network relation. This is
        not OK. The convention is to include each node in the network relation.
        An exception is when the node belongs to another network (all routes to
        this node have role _"connection"_ in the network relation), then the
        node does not have to be included as member in the network relation. The
        node can be added the network relation, but should get the role
        _"connection"_ in that case.
      </markdown>
    </kpn-indicator-dialog>
  `,
})
export class NetworkIndicatorDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public color: string) {}

  isOrange() {
    return this.color === 'orange';
  }

  isGreen() {
    return this.color === 'green';
  }

  isGray() {
    return this.color === 'gray';
  }

  isRed() {
    return this.color === 'red';
  }
}
