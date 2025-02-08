import { inject } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { State } from '@app/state/state';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { fromLonLat } from 'ol/proj';
import { MapService } from '../../../../map/map.service';
import { GeolocationPermissionDeniedDialogComponent } from './geolocation-permission-denied-dialog.component';
import { GeolocationTimeoutDialogComponent } from './geolocation-timeout-dialog.component';
import { GeolocationUnavailableDialogComponent } from './geolocation-unavailable-dialog.component';

@Component({
  selector: 'kpn-geolocation-button',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button
      nz-button
      (click)="onClick()"
      title="position the map on your current location"
      i18n-title="@@geolocation-control.title"
    >
      <nz-icon nzType="aim" />
      <span>current location</span>
    </button>
  `,
  imports: [NzButtonComponent, NzIconDirective],
})
export class GeolocationButtonComponent {
  private readonly state = inject(State);
  private readonly mapService = inject(MapService);
  private readonly dialog = inject(MatDialog);

  onClick(): void {
    if (!navigator.geolocation) {
      this.dialog.open(GeolocationUnavailableDialogComponent, {
        autoFocus: false,
        maxWidth: 600,
      });
    } else {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const center = fromLonLat([position.coords.longitude, position.coords.latitude]);
          this.mapService.geolocation(center);
        },
        (positionError: GeolocationPositionError) => {
          if (positionError.code === 1) {
            this.dialog.open(GeolocationPermissionDeniedDialogComponent, {
              autoFocus: false,
              maxWidth: 600,
            });
          } else if (positionError.code === 2) {
            this.dialog.open(GeolocationUnavailableDialogComponent, {
              autoFocus: false,
              maxWidth: 600,
            });
          } else if (positionError.code === 3) {
            this.dialog.open(GeolocationTimeoutDialogComponent, {
              autoFocus: false,
              maxWidth: 600,
            });
          } else {
            this.dialog.open(GeolocationUnavailableDialogComponent, {
              autoFocus: false,
              maxWidth: 600,
            });
          }
        },
        {
          enableHighAccuracy: true,
        }
      );
    }
  }
}
