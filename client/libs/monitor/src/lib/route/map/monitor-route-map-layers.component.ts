import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { Store } from '@ngrx/store';
import { LegendLineComponent } from './legend-line';
import { actionMonitorRouteMapOsmRelationVisible } from './store/monitor-route-map.actions';
import { actionMonitorRouteMapDeviationsVisible } from './store/monitor-route-map.actions';
import { actionMonitorRouteMapMatchesVisible } from './store/monitor-route-map.actions';
import { actionMonitorRouteMapReferenceVisible } from './store/monitor-route-map.actions';
import { selectMonitorRouteMapReferenceType } from './store/monitor-route-map.selectors';
import { selectMonitorRouteMapOsmRelationEnabled } from './store/monitor-route-map.selectors';
import { selectMonitorRouteMapDeviationsEnabled } from './store/monitor-route-map.selectors';
import { selectMonitorRouteMapMatchesEnabled } from './store/monitor-route-map.selectors';
import { selectMonitorRouteMapReferenceEnabled } from './store/monitor-route-map.selectors';
import { selectMonitorRouteMapOsmRelationVisible } from './store/monitor-route-map.selectors';
import { selectMonitorRouteMapDeviationsVisible } from './store/monitor-route-map.selectors';
import { selectMonitorRouteMapMatchesVisible } from './store/monitor-route-map.selectors';
import { selectMonitorRouteMapReferenceVisible } from './store/monitor-route-map.selectors';
import { selectMonitorRouteMapMode } from './store/monitor-route-map.selectors';

@Component({
  selector: 'kpn-monitor-route-map-layers',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="mode()" class="map-layers">
      <ng-container *ngIf="referenceType()">
        <mat-checkbox
          [checked]="referenceVisible()"
          [disabled]="referenceEnabled() === false"
          (change)="referenceVisibleChanged($event)"
        >
          <div class="kpn-line">
            <kpn-legend-line color="blue"></kpn-legend-line>
            <span
              *ngIf="referenceType() === 'gpx'"
              i18n="@@monitor.route.map-layers.reference.gpx"
              >GPX Reference</span
            >
            <span
              *ngIf="referenceType() === 'osm'"
              i18n="@@monitor.route.map-layers.reference.osm"
              >OSM reference</span
            >
          </div>
        </mat-checkbox>

        <mat-checkbox
          [checked]="matchesVisible()"
          [disabled]="matchesEnabled() === false"
          (change)="matchesVisibleChanged($event)"
        >
          <div class="kpn-line">
            <kpn-legend-line color="green" />
            <span
              *ngIf="referenceType() === 'gpx'"
              i18n="@@monitor.route.map-layers.gpx-same-as-osm"
            >
              GPX same as OSM
            </span>
            <span
              *ngIf="referenceType() === 'osm'"
              i18n="@@monitor.route.map-layers.reference-same-as-osm"
            >
              Reference matches
            </span>
          </div>
        </mat-checkbox>

        <mat-checkbox
          [checked]="deviationsVisible()"
          [disabled]="gpxDeviationsEnabled() === false"
          (change)="deviationsVisibleChanged($event)"
        >
          <div class="kpn-line">
            <kpn-legend-line color="red" />
            <span
              *ngIf="referenceType() === 'gpx'"
              i18n="@@monitor.route.map-layers.deviations.gpx"
            >
              GPX where OSM is deviating
            </span>
            <span
              *ngIf="referenceType() === 'osm'"
              i18n="@@monitor.route.map-layers.deviations.osm"
            >
              Reference deviations
            </span>
          </div>
        </mat-checkbox>

        <mat-checkbox
          [checked]="osmRelationVisible()"
          [disabled]="osmRelationEnabled() === false"
          (change)="osmRelationVisibleChanged($event)"
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
  imports: [NgIf, MatCheckboxModule, LegendLineComponent, AsyncPipe],
})
export class MonitorRouteMapLayersComponent {
  readonly mode = this.store.selectSignal(selectMonitorRouteMapMode);

  readonly referenceType = this.store.selectSignal(
    selectMonitorRouteMapReferenceType
  );

  readonly referenceVisible = this.store.selectSignal(
    selectMonitorRouteMapReferenceVisible
  );

  readonly matchesVisible = this.store.selectSignal(
    selectMonitorRouteMapMatchesVisible
  );

  readonly deviationsVisible = this.store.selectSignal(
    selectMonitorRouteMapDeviationsVisible
  );

  readonly osmRelationVisible = this.store.selectSignal(
    selectMonitorRouteMapOsmRelationVisible
  );

  readonly referenceEnabled = this.store.selectSignal(
    selectMonitorRouteMapReferenceEnabled
  );

  readonly matchesEnabled = this.store.selectSignal(
    selectMonitorRouteMapMatchesEnabled
  );

  readonly gpxDeviationsEnabled = this.store.selectSignal(
    selectMonitorRouteMapDeviationsEnabled
  );

  readonly osmRelationEnabled = this.store.selectSignal(
    selectMonitorRouteMapOsmRelationEnabled
  );

  constructor(private store: Store) {}

  referenceVisibleChanged(event: MatCheckboxChange): void {
    this.store.dispatch(
      actionMonitorRouteMapReferenceVisible({ visible: event.checked })
    );
  }

  matchesVisibleChanged(event: MatCheckboxChange): void {
    this.store.dispatch(
      actionMonitorRouteMapMatchesVisible({ visible: event.checked })
    );
  }

  deviationsVisibleChanged(event: MatCheckboxChange): void {
    this.store.dispatch(
      actionMonitorRouteMapDeviationsVisible({ visible: event.checked })
    );
  }

  osmRelationVisibleChanged(event: MatCheckboxChange): void {
    this.store.dispatch(
      actionMonitorRouteMapOsmRelationVisible({ visible: event.checked })
    );
  }
}
