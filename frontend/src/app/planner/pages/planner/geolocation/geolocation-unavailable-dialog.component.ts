import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatDialogModule } from '@angular/material/dialog';
import { DialogComponent } from '@app/components/shared/dialog';

@Component({
  selector: 'kpn-no-geolocation-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-dialog>
      <div mat-dialog-title i18n="@@geolocation-dialog.unavailable.title">Unavailable</div>
      <div mat-dialog-content i18n="@@geolocation-dialog.unavailable.message">
        We cannot determine your location. Verify your settings to allow this application to access
        you location.
      </div>
    </kpn-dialog>
  `,
  standalone: true,
  imports: [DialogComponent, MatDialogModule],
})
export class GeolocationUnavailableDialogComponent {}
