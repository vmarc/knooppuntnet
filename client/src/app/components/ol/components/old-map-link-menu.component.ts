import { ViewChild } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { MatMenuTrigger } from '@angular/material/menu';
import { toLonLat } from 'ol/proj';
import { OpenLayersMap } from '@app/components/ol/domain/open-layers-map';

@Component({
  selector: 'kpn-old-map-link-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <mat-menu #mapMenu="matMenu">
      <ng-template matMenuContent>
        <div (mouseleave)="mouseLeavesMenu()">
          <button mat-menu-item (click)="goto('iD')">iD</button>
          <button mat-menu-item (click)="goto('openstreetmap')">
            OpenStreetMap
          </button>
          <button mat-menu-item (click)="goto('mapillary')">Mapillary</button>
          <button mat-menu-item (click)="goto('google')">Google</button>
          <button mat-menu-item (click)="goto('google-satellite')">
            Google Satellite
          </button>
        </div>
      </ng-template>
    </mat-menu>

    <div class="map-control map-links-control" (click)="openPopupMenu()">
      <button
        class="map-control-button"
        [matMenuTriggerFor]="mapMenu"
        title="open website at this location"
        i18n-title="@@map-link-menu-title"
      >
        <mat-icon svgIcon="external-link" />
      </button>
    </div>
  `,
  styles: [
    `
      .map-links-control {
        top: 50px;
        right: 10px;
      }
    `,
  ],
})
export class OldMapLinkMenuComponent {
  @Input() map: OpenLayersMap;

  @ViewChild(MatMenuTrigger) trigger: MatMenuTrigger;

  openPopupMenu(): void {
    this.trigger.openMenu();
  }

  mouseLeavesMenu(): void {
    this.trigger.closeMenu();
  }

  goto(target: string): void {
    const zoom = Math.round(this.map.map.getView().getZoom());
    const center = toLonLat(this.map.map.getView().getCenter());
    let url = '';
    if (target === 'openstreetmap') {
      url = `https://www.openstreetmap.org/#map=${zoom}/${center[1]}/${center[0]}`;
    } else if (target === 'mapillary') {
      url = `https://www.mapillary.com/app/?lat=${center[1]}&lng=${center[0]}&z=${zoom}`;
    } else if (target === 'google') {
      url = `https://www.google.com/maps/@?api=1&map_action=map&center=${center[1]},${center[0]}&zoom=${zoom}`;
    } else if (target === 'google-satellite') {
      url = `https://www.google.com/maps/@?api=1&map_action=map&center=${center[1]},${center[0]}&zoom=${zoom}&basemap=satellite`;
    } else if (target === 'iD') {
      url = `https://www.openstreetmap.org/edit?editor=id#map=${zoom}/${center[1]}/${center[0]}`;
    }
    window.open(encodeURI(url), '_blank');
  }
}
