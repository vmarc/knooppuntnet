import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { IndicatorDialogComponent } from '@app/components/shared/indicator';
import { MarkdownModule } from 'ngx-markdown';

@Component({
  selector: 'kpn-route-proposed-indicator-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-indicator-dialog
      letter="P"
      i18n-letter="@@route-proposed-indicator.letter"
      [color]="color"
    >
      <span dialog-title *ngIf="isBlue()" i18n="@@route-proposed-indicator.blue.title">
        OK - Proposed route
      </span>
      <markdown dialog-body *ngIf="isBlue()" i18n="@@route-proposed-indicator.blue.text">
        This route has _"state=proposed"_. The route is assumed to still be in a planning phase and
        likely not signposted in the field.
      </markdown>

      <span dialog-title *ngIf="isGray()" i18n="@@route-proposed-indicator.gray.title">
        OK - Active route
      </span>
      <markdown dialog-body *ngIf="isGray()" i18n="@@route-proposed-indicator.gray.text">
        This route does not have _"state=proposed"_. This is an active route.
      </markdown>
    </kpn-indicator-dialog>
  `,
  standalone: true,
  imports: [IndicatorDialogComponent, NgIf, MarkdownModule],
})
export class RouteProposedIndicatorDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public color: string) {}

  isBlue() {
    return this.color === 'blue';
  }

  isGray() {
    return this.color === 'gray';
  }
}
