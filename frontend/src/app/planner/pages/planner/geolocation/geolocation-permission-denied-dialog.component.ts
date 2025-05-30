import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatDialogModule } from '@angular/material/dialog';
import { DialogComponent } from '@app/shared/components/dialog/dialog.component';

@Component({
  selector: 'ui-geolocation-permission-denied-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-dialog>
      <div mat-dialog-title i18n="@@geolocation-dialog.permission-denied.title">
        Permission denied
      </div>
      <div mat-dialog-content i18n="@@geolocation-dialog.permission-denied.message">
        We cannot determine your location. Knooppuntnet does not have the permission to access your
        location.
      </div>
    </ui-dialog>
  `,
  imports: [DialogComponent, MatDialogModule],
})
export class GeolocationPermissionDeniedDialogComponent {}
