import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { computed } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule, MatSelectionListChange } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MonitorRouteDeviation } from '@api/common/monitor';
import { DistancePipe } from '@app/components/shared/format';
import { IconHappyComponent } from '@app/components/shared/icon';
import { Store } from '@ngrx/store';
import { LegendLineComponent } from './legend-line';
import { actionMonitorRouteMapJosmZoomToSelectedDeviation } from './store/monitor-route-map.actions';
import { actionMonitorRouteMapSelectDeviation } from './store/monitor-route-map.actions';
import { selectMonitorRouteMapSelectedDeviationId } from './store/monitor-route-map.selectors';
import { selectMonitorRouteMapOsmRelationAvailable } from './store/monitor-route-map.selectors';
import { selectMonitorRouteMapOsmRelationEmpty } from './store/monitor-route-map.selectors';
import { selectMonitorRouteMapReferenceEnabled } from './store/monitor-route-map.selectors';
import { selectMonitorRouteMapDeviations } from './store/monitor-route-map.selectors';

@Component({
  selector: 'kpn-monitor-route-map-deviations',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div
      *ngIf="
        referenceAvailable();
        then showReferenceAvailable;
        else showNoReference
      "
    ></div>
    <ng-template #showNoReference>
      <p i18n="@@monitor.route.map-deviations.no-reference">
        No reference, so no analysis results.
      </p>
    </ng-template>
    <ng-template #showReferenceAvailable>
      <div
        *ngIf="
          osmRelationAvailable();
          then showOsmRelationAvailable;
          else showOsmRelationMissing
        "
      ></div>
      <ng-template #showOsmRelationMissing>
        <p i18n="@@monitor.route.map-deviations.no-relation">
          OSM relation missing in route definition, so no analysis results.
        </p>
      </ng-template>
      <ng-template #showOsmRelationAvailable>
        <p
          *ngIf="osmRelationEmpty()"
          i18n="@@monitor.route.map-deviations.relation-empty"
        >
          OSM relation empty, so no analysis results.
        </p>
        <div
          *ngIf="hasDeviations(); then showDeviations; else showNoDeviations"
        ></div>
        <ng-template #showNoDeviations>
          <p class="kpn-spacer-above kpn-line">
            <kpn-icon-happy />
            <span i18n="@@monitor.route.map-deviations.no-deviations">
              No deviations
            </span>
          </p>
        </ng-template>

        <ng-template #showDeviations>
          <p class="segments-title">
            <span i18n="@@monitor.route.map-deviations.title">Deviations</span>
          </p>

          <div class="segment segment-header">
            <span class="segment-id">
              <kpn-legend-line color="red"></kpn-legend-line>
            </span>
            <span
              class="segment-deviation"
              i18n="@@monitor.route.map-deviations.deviation"
              >Deviation</span
            >
            <span i18n="@@monitor.route.map-deviations.length">Length</span>
          </div>

          <mat-menu #appMenu="matMenu">
            <ng-template matMenuContent let-deviation="deviation">
              <button
                mat-menu-item
                (click)="josmZoomToSelectedDeviation()"
                i18n="@@monitor.route.map-deviations.zoom-josm"
              >
                Go here in JOSM
              </button>
              <button
                mat-menu-item
                (click)="zoomToDeviationInOpenstreetMap(deviation)"
                i18n="@@monitor.route.map-deviations.zoom-openstreetmap"
              >
                Go here in openstreetmap.org
              </button>
            </ng-template>
          </mat-menu>

          <mat-selection-list
            [multiple]="false"
            (selectionChange)="selectionChanged($event)"
            [hideSingleSelectionIndicator]="true"
          >
            <mat-list-option
              *ngFor="let deviation of deviations()"
              [selected]="selectedDeviationId() === deviation.id"
              [value]="deviation"
            >
              <div class="segment">
                <span class="segment-id">{{ deviation.id }}</span>
                <span
                  *ngIf="deviation.distance === 2500"
                  class="segment-deviation"
                  >{{ longDistance }}</span
                >
                <span
                  *ngIf="deviation.distance !== 2500"
                  class="segment-deviation"
                  >{{ deviation.distance | distance }}</span
                >
                <span>{{ deviation.meters | distance }}</span>
                <button
                  mat-icon-button
                  [matMenuTriggerFor]="appMenu"
                  [matMenuTriggerData]="{ deviation: deviation }"
                  class="popup-menu"
                >
                  <mat-icon svgIcon="menu-dots"></mat-icon>
                </button>
              </div>
            </mat-list-option>
          </mat-selection-list>
        </ng-template>
      </ng-template>
    </ng-template>
  `,
  styles: [
    `
      .segments-title {
        padding-top: 1em;
      }

      .segment {
        display: flex;
        align-items: center;
      }

      .segment-header {
        padding-left: 1em;
      }

      .segment-id {
        width: 3em;
      }

      .segment-deviation {
        width: 5em;
      }

      .popup-menu {
        margin-left: auto;
      }
    `,
  ],
  standalone: true,
  imports: [
    NgIf,
    IconHappyComponent,
    LegendLineComponent,
    MatMenuModule,
    MatListModule,
    NgFor,
    MatButtonModule,
    MatIconModule,
    AsyncPipe,
    DistancePipe,
  ],
})
export class MonitorRouteMapDeviationsComponent {
  readonly deviations = this.store.selectSignal(
    selectMonitorRouteMapDeviations
  );

  readonly hasDeviations = computed(() => this.deviations().length > 0);

  readonly referenceAvailable = this.store.selectSignal(
    selectMonitorRouteMapReferenceEnabled
  );

  readonly osmRelationAvailable = this.store.selectSignal(
    selectMonitorRouteMapOsmRelationAvailable
  );

  readonly osmRelationEmpty = this.store.selectSignal(
    selectMonitorRouteMapOsmRelationEmpty
  );

  readonly selectedDeviationId = this.store.selectSignal(
    selectMonitorRouteMapSelectedDeviationId
  );

  readonly longDistance = '> 2.5 km';

  constructor(private store: Store) {}

  selectionChanged(event: MatSelectionListChange): void {
    let deviation: MonitorRouteDeviation = null;
    if (event.options.length > 0) {
      deviation = event.options[0].value;
    }
    this.store.dispatch(actionMonitorRouteMapSelectDeviation(deviation));
  }

  josmZoomToSelectedDeviation(): void {
    this.store.dispatch(actionMonitorRouteMapJosmZoomToSelectedDeviation());
  }

  zoomToDeviationInOpenstreetMap(deviation: MonitorRouteDeviation): void {
    const bounds = deviation.bounds;
    const url = `https://www.openstreetmap.org/?bbox=${bounds.minLon},${bounds.minLat},${bounds.maxLon},${bounds.maxLat}`;
    window.open(url, 'openstreetmap');
  }
}
