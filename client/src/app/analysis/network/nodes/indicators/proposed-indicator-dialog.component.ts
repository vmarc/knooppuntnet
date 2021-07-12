import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'kpn-proposed-indicator-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-indicator-dialog
      letter="P"
      i18n-letter="@@proposed-indicator.letter"
      [color]="color"
    >
      <span
        dialog-title
        *ngIf="isBlue()"
        i18n="@@proposed-indicator.blue.title"
      >
        OK - Proposed route
      </span>
      <markdown
        dialog-body
        *ngIf="isBlue()"
        i18n="@@proposed-indicator.blue.text"
      >
        This node has _"state=proposed"_. The node is assumed to still be in a
        planning phase and likely not signposted in the field.
      </markdown>

      <span
        dialog-title
        *ngIf="isGray()"
        i18n="@@proposed-indicator.gray.title"
      >
        OK - Active node
      </span>
      <markdown
        dialog-body
        *ngIf="isGray()"
        i18n="@@proposed-indicator.gray.text"
      >
        This network node is not _"proposed"_. It is an active node.
      </markdown>
    </kpn-indicator-dialog>
  `,
})
export class ProposedIndicatorDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public color: string) {}

  isBlue() {
    return this.color === 'blue';
  }

  isGray() {
    return this.color === 'gray';
  }
}
