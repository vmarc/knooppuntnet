import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { LegendLineComponent } from './legend-line';
import { MonitorRouteMapService } from './monitor-route-map.service';

@Component({
  selector: 'kpn-monitor-route-map-layers',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="map-layers">
      <ng-container *ngIf="service.referenceType()">
        <mat-checkbox
          [checked]="service.referenceVisible()"
          [disabled]="service.referenceAvailable() === false"
          (change)="service.referenceVisibleChanged($event.checked)"
        >
          <div class="kpn-line">
            <kpn-legend-line color="blue"></kpn-legend-line>
            <span
              *ngIf="service.referenceType() === 'gpx'"
              i18n="@@monitor.route.map-layers.reference.gpx"
              >GPX Reference</span
            >
            <span
              *ngIf="service.referenceType() === 'osm'"
              i18n="@@monitor.route.map-layers.reference.osm"
              >OSM reference</span
            >
          </div>
        </mat-checkbox>

        <mat-checkbox
          [checked]="service.matchesVisible()"
          [disabled]="service.matchesEnabled() === false"
          (change)="service.matchesVisibleChanged($event.checked)"
        >
          <div class="kpn-line">
            <kpn-legend-line color="green" />
            <span
              *ngIf="service.referenceType() === 'gpx'"
              i18n="@@monitor.route.map-layers.gpx-same-as-osm"
            >
              GPX same as OSM
            </span>
            <span
              *ngIf="service.referenceType() === 'osm'"
              i18n="@@monitor.route.map-layers.reference-same-as-osm"
            >
              Reference matches
            </span>
          </div>
        </mat-checkbox>

        <mat-checkbox
          [checked]="service.deviationsVisible()"
          [disabled]="service.gpxDeviationsEnabled() === false"
          (change)="service.deviationsVisibleChanged($event.checked)"
        >
          <div class="kpn-line">
            <kpn-legend-line color="red" />
            <span
              *ngIf="service.referenceType() === 'gpx'"
              i18n="@@monitor.route.map-layers.deviations.gpx"
            >
              GPX where OSM is deviating
            </span>
            <span
              *ngIf="service.referenceType() === 'osm'"
              i18n="@@monitor.route.map-layers.deviations.osm"
            >
              Reference deviations
            </span>
          </div>
        </mat-checkbox>

        <mat-checkbox
          [checked]="service.osmRelationVisible()"
          [disabled]="service.osmRelationEnabled() === false"
          (change)="service.osmRelationVisibleChanged($event.checked)"
        >
          <div class="kpn-line">
            <kpn-legend-line color="gold" />
            <span i18n="@@monitor.route.map-layers.osm-relation">
              OSM relation
            </span>
          </div>
        </mat-checkbox>
      </ng-container>
    </div>
  `,
  styles: [
    `
      .map-layers {
        margin-top: 1em;
      }
    `,
  ],
  standalone: true,
  imports: [NgIf, MatCheckboxModule, LegendLineComponent],
})
export class MonitorRouteMapLayersComponent {
  constructor(protected service: MonitorRouteMapService) {
    console.log('MonitorRouteMapLayersComponent.constructor()');
  }
}
