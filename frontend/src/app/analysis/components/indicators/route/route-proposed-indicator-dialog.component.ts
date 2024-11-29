import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
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
      @switch (color) {
        @case ('blue') {
          <span dialog-title i18n="@@route-proposed-indicator.blue.title">
            OK - Proposed route
          </span>
        }
        @case ('gray') {
          <span dialog-title i18n="@@route-proposed-indicator.gray.title"> OK - Active route </span>
        }
      }
      @switch (color) {
        @case ('blue') {
          <markdown dialog-body i18n="@@route-proposed-indicator.blue.text">
            This route has _"state=proposed"_. The route is assumed to still be in a planning phase
            and likely not signposted in the field.
          </markdown>
        }
        @case ('gray') {
          <markdown dialog-body i18n="@@route-proposed-indicator.gray.text">
            This route does not have _"state=proposed"_. This is an active route.
          </markdown>
        }
      }
    </kpn-indicator-dialog>
  `,
  imports: [IndicatorDialogComponent, MarkdownModule],
})
export class RouteProposedIndicatorDialogComponent {
  protected readonly color: string = inject(MAT_DIALOG_DATA);
}
