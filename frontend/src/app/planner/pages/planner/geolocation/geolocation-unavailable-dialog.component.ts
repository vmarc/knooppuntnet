import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { DialogComponent } from '@app/shared/components/dialog/dialog.component';

@Component({
  selector: 'ui-no-geolocation-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-dialog>
      <div dialog-title i18n="@@geolocation-dialog.unavailable.title">Unavailable</div>
      <div i18n="@@geolocation-dialog.unavailable.message">
        We cannot determine your location. Verify your settings to allow this application to access
        you location.
      </div>
    </ui-dialog>
  `,
  imports: [DialogComponent],
})
export class GeolocationUnavailableDialogComponent {}
