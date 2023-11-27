import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { IndicatorDialogComponent } from '@app/components/shared/indicator';
import { MarkdownModule } from 'ngx-markdown';

@Component({
  selector: 'kpn-proposed-indicator-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-indicator-dialog letter="P" i18n-letter="@@proposed-indicator.letter" [color]="color">
      <span dialog-title *ngIf="isBlue()" i18n="@@proposed-indicator.blue.title">
        OK - Proposed route
      </span>
      <markdown dialog-body *ngIf="isBlue()" i18n="@@proposed-indicator.blue.text">
        This node is _"proposed"_. The node has lifecycle prefix "proposed:" in the tag that makes
        it a network node, or has has tag _"state=proposed"_. The node is assumed to still be in a
        planning phase and likely not signposted in the field.
      </markdown>

      <span dialog-title *ngIf="isGray()" i18n="@@proposed-indicator.gray.title">
        OK - Active node
      </span>
      <markdown dialog-body *ngIf="isGray()" i18n="@@proposed-indicator.gray.text">
        This network node is not _"proposed"_. It is an active node.
      </markdown>
    </kpn-indicator-dialog>
  `,
  standalone: true,
  imports: [IndicatorDialogComponent, NgIf, MarkdownModule],
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
