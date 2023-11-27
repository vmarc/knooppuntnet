import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatDialogModule } from '@angular/material/dialog';
import { DialogComponent } from '@app/components/shared/dialog';

@Component({
  selector: 'kpn-geolocation-timeout-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-dialog>
      <div mat-dialog-title i18n="@@geolocation-dialog.timeout.title">Timeout</div>
      <div mat-dialog-content i18n="@@geolocation-dialog.timeout.message">
        We cannot determine your location.
      </div>
    </kpn-dialog>
  `,
  standalone: true,
  imports: [DialogComponent, MatDialogModule],
})
export class GeolocationTimeoutDialogComponent {}
