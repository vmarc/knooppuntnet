import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { Coordinate } from 'ol/coordinate';
import { fromLonLat } from 'ol/proj';
import { GeolocationPermissionDeniedDialogComponent } from './geolocation-permission-denied-dialog.component';
import { GeolocationTimeoutDialogComponent } from './geolocation-timeout-dialog.component';
import { GeolocationUnavailableDialogComponent } from './geolocation-unavailable-dialog.component';

@Component({
  selector: 'kpn-geolocation-control',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="control">
      <button
        class="button"
        (click)="onClick()"
        title="position the map on your current location"
        i18n-title="@@geolocation-control.title"
      >
        <mat-icon svgIcon="location" />
      </button>
    </div>
  `,
  styles: [
    `
      .control {
        position: absolute;
        left: 8px;
        top: 146px;
        z-index: 100;
        height: 31px;
        width: 31px;
        padding: 2px;
        border: 0 none white;
        border-radius: 3px;
        background-color: rgba(255, 255, 255, 0.5);
      }

      .button {
        margin: 1px;
        background-color: rgba(0, 60, 136, 0.5);
        color: white;
        border: 0 none white;
        border-radius: 2px;
        height: 28px;
        width: 28px;
        padding-left: 3px;
        padding-top: 5px;
      }

      .control ::ng-deep mat-icon svg {
        width: 18px;
        height: 18px;
      }

      .button:hover {
        background-color: rgba(0, 60, 136, 0.7);
      }
    `,
  ],
  standalone: true,
  imports: [MatIconModule],
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
