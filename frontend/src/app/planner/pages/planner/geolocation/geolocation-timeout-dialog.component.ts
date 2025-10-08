import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { DialogComponent } from '@app/shared/components/dialog/dialog.component';

@Component({
  selector: 'ui-geolocation-timeout-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-dialog>
      <div dialog-title i18n="@@geolocation-dialog.timeout.title">Timeout</div>
      <div i18n="@@geolocation-dialog.timeout.message">We cannot determine your location.</div>
    </ui-dialog>
  `,
  imports: [DialogComponent],
})
export class GeolocationTimeoutDialogComponent {}
