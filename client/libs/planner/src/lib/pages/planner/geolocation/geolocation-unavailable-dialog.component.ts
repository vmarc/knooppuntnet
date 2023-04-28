import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-no-geolocation-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-dialog>
      <div mat-dialog-title i18n="@@geolocation-dialog.unavailable.title">
        Unavailable
      </div>
      <div mat-dialog-content i18n="@@geolocation-dialog.unavailable.message">
        We cannot determine your location. Verify your settings to allow this
        application to access you location.
      </div>
    </kpn-dialog>
  `,
})
export class GeolocationUnavailableDialogComponent {}
