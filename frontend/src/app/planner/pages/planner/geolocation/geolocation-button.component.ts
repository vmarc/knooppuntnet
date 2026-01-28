import { DOCUMENT } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NzModalService } from 'ng-zorro-antd/modal';
import { fromLonLat } from 'ol/proj';
import { OldMapService } from '@app/mapold/old-map.service';
import { GeolocationPermissionDeniedDialogComponent } from './geolocation-permission-denied-dialog.component';
import { GeolocationTimeoutDialogComponent } from './geolocation-timeout-dialog.component';
import { GeolocationUnavailableDialogComponent } from './geolocation-unavailable-dialog.component';

@Component({
  selector: 'ui-geolocation-button',
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
  private readonly mapService = inject(OldMapService);
  private readonly modalService = inject(NzModalService);
  private readonly document = inject(DOCUMENT);
  private readonly navigator = this.document?.defaultView?.navigator;

  onClick(): void {
    if (!this.navigator.geolocation) {
      this.showUnavailableDialog();
    } else {
      this.navigator.geolocation.getCurrentPosition(
        (position) => {
          const center = fromLonLat([position.coords.longitude, position.coords.latitude]);
          this.mapService.geolocation(center);
        },
        // @ts-ignore
        (positionError: GeolocationPositionError) => {
          this.showError(positionError);
        },
        {
          enableHighAccuracy: true,
        }
      );
    }
  }

  // @ts-ignore
  private showError(positionError: GeolocationPositionError) {
    if (positionError.code === 1) {
      this.showPermissionDeniedDialog();
    } else if (positionError.code === 2) {
      this.showUnavailableDialog();
    } else if (positionError.code === 3) {
      this.showTimeoutDialog();
    } else {
      this.showUnavailableDialog();
    }
  }

  private showUnavailableDialog(): void {
    this.modalService.create({
      nzContent: GeolocationUnavailableDialogComponent,
      nzFooter: null,
    });
  }

  private showTimeoutDialog() {
    this.modalService.create({
      nzContent: GeolocationTimeoutDialogComponent,
      nzFooter: null,
    });
  }

  private showPermissionDeniedDialog() {
    this.modalService.create({
      nzContent: GeolocationPermissionDeniedDialogComponent,
      nzFooter: null,
    });
  }
}
