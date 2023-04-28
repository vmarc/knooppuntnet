import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatDialogModule } from '@angular/material/dialog';
import { DialogComponent } from '@app/components/shared/dialog';

@Component({
  selector: 'kpn-geolocation-permission-denied-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-dialog>
      <div mat-dialog-title i18n="@@geolocation-dialog.permission-denied.title">
        Permission denied
      </div>
      <div
        mat-dialog-content
        i18n="@@geolocation-dialog.permission-denied.message"
      >
        We cannot determine your location. Knooppuntnet does not have the
        permission to access your location.
      </div>
    </kpn-dialog>
  `,
  standalone: true,
  imports: [DialogComponent, MatDialogModule],
})
export class GeolocationPermissionDeniedDialogComponent {}
