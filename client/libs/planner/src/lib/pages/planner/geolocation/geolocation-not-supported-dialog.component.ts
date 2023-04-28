import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatDialogModule } from '@angular/material/dialog';
import { DialogComponent } from '@app/components/shared/dialog';

@Component({
  selector: 'kpn-geolocation-not-supported-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-dialog>
      <div mat-dialog-title i18n="@@geolocation-dialog.not-supported.title">
        Not supported
      </div>
      <div mat-dialog-content i18n="@@geolocation-dialog.not-supported.message">
        We cannot determine your location. Your device does not seem to support
        this.
      </div>
    </kpn-dialog>
  `,
  standalone: true,
  imports: [DialogComponent, MatDialogModule],
})
export class GeolocationNotSupportedDialogComponent {}
