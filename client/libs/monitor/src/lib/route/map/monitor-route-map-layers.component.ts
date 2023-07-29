import { NgIf } from '@angular/common';
import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { LegendLineComponent } from './legend-line';
import { MonitorMapMode } from './monitor-map-mode';
import { MonitorRouteMapStateService } from './monitor-route-map-state.service';

@Component({
  selector: 'kpn-monitor-route-map-layers',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="map-layers">
      <mat-checkbox
        [checked]="mapStateService.referenceVisible()"
        [disabled]="!modeComparison()"
        (change)="mapStateService.referenceVisibleChanged($event.checked)"
      >
        <div class="kpn-line">
          <kpn-legend-line color="blue"></kpn-legend-line>
          <span
            *ngIf="referenceType() === 'gpx' || referenceType() === 'multi-gpx'"
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
        [checked]="mapStateService.matchesVisible()"
        [disabled]="!modeComparison()"
        (change)="mapStateService.matchesVisibleChanged($event.checked)"
      >
        <div class="kpn-line">
          <kpn-legend-line color="green" />
          <span
            *ngIf="referenceType() === 'gpx' || referenceType() === 'multi-gpx'"
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
        [checked]="mapStateService.deviationsVisible()"
        [disabled]="!modeComparison()"
        (change)="mapStateService.deviationsVisibleChanged($event.checked)"
      >
        <div class="kpn-line">
          <kpn-legend-line color="red" />
          <span
            *ngIf="referenceType() === 'gpx' || referenceType() === 'multi-gpx'"
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
        [checked]="mapStateService.osmRelationVisible()"
        [disabled]="!modeComparison()"
        (change)="mapStateService.osmRelationVisibleChanged($event.checked)"
      >
        <div class="kpn-line">
          <kpn-legend-line color="gold" />
          <span i18n="@@monitor.route.map-layers.osm-relation">
            OSM relation
          </span>
        </div>
      </mat-checkbox>
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
  readonly referenceType = computed(
    () => this.mapStateService.page().referenceType
  );

  readonly modeComparison = computed(
    () => this.mapStateService.mode() === MonitorMapMode.comparison
  );

  constructor(protected mapStateService: MonitorRouteMapStateService) {}
}
