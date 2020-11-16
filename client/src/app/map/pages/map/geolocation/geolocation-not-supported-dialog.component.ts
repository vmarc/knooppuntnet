import {ChangeDetectionStrategy, Component} from '@angular/core';

@Component({
  selector: 'kpn-geolocation-not-supported-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-dialog>
      <div mat-dialog-title i18n="@@geolocation-dialog.not-supported.title">
        Not supported
      </div>
      <div mat-dialog-content i18n="@@geolocation-dialog.not-supported.message">
        We cannot determine your location. Your device does not seem to support this.
      </div>
    </kpn-dialog>
  `
})
export class GeolocationNotSupportedDialogComponent {
}
