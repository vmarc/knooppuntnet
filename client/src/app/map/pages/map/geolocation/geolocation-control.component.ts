import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Coordinate } from 'ol/coordinate';
import { fromLonLat } from 'ol/proj';
import { GeolocationPermissionDeniedDialogComponent } from './geolocation-permission-denied-dialog.component';
import { GeolocationTimeoutDialogComponent } from './geolocation-timeout-dialog.component';
import { GeolocationUnavailableDialogComponent } from './geolocation-unavailable-dialog.component';

@Component({
  selector: 'kpn-geolocation-control',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="control ol-unselectable ol-control">
      <button
        (click)="onClick()"
        title="position the map on your current location"
        i18n-title="@@geolocation-control.title"
      >
        <mat-icon svgIcon="location"></mat-icon>
      </button>
    </div>
  `,
  styles: [
    `
      .control {
        left: 8px;
        top: 148px;
        z-index: 100;
        padding: 2px;
        width: 28px;
        height: 28px;
      }

      .control mat-icon {
        width: 14px;
        height: 14px;
      }
    `,
  ],
})
export class GeolocationControlComponent {
  @Output() action = new EventEmitter<Coordinate>();

  constructor(private dialog: MatDialog) {}

  onClick(): void {
    if (!navigator.geolocation) {
      this.dialog.open(GeolocationUnavailableDialogComponent, {
        autoFocus: false,
        maxWidth: 600,
      });
    } else {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const center = fromLonLat([
            position.coords.longitude,
            position.coords.latitude,
          ]);
          this.action.emit(center);
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
