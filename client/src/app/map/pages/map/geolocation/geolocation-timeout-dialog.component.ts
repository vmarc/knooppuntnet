import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'kpn-geolocation-timeout-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-dialog>
      <div mat-dialog-title i18n="@@geolocation-dialog.timeout.title">
        Timeout
      </div>
      <div mat-dialog-content i18n="@@geolocation-dialog.timeout.message">
        We cannot determine your location.
      </div>
    </kpn-dialog>
  `,
})
export class GeolocationTimeoutDialogComponent {}
