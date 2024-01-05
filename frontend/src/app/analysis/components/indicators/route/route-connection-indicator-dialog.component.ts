import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { IndicatorDialogComponent } from '@app/components/shared/indicator';

@Component({
  selector: 'kpn-route-connection-indicator-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-indicator-dialog
      letter="C"
      i18n-letter="@@route-connection.indicator.letter"
      [color]="color"
    >
      @switch (color) {
        @case ('blue') {
          <span dialog-title i18n="@@route-connection-indicator.blue.title"> OK - Connection </span>
          <div dialog-body i18n="@@route-connection-indicator.blue.text">
            This route is a connection to another network.
          </div>
        }
        @case ('gray') {
          <span dialog-title i18n="@@route-connection-indicator.gray.title">
            OK - No connection
          </span>
          <div dialog-body i18n="@@route-connection-indicator.gray.text">
            This route is not a connection to another network.
          </div>
        }
      }
    </kpn-indicator-dialog>
  `,
  standalone: true,
  imports: [IndicatorDialogComponent],
})
export class RouteConnectionIndicatorDialogComponent {
  protected readonly color: string = inject(MAT_DIALOG_DATA);
}
