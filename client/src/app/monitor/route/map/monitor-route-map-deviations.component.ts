import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatSelectionListChange } from '@angular/material/list';
import { MonitorRouteDeviation } from '@app/kpn/api/common/monitor/monitor-route-deviation';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
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
        referenceAvailable$ | async;
        then referenceAvailable;
        else noReference
      "
    ></div>
    <ng-template #noReference>
      <p i18n="@@monitor.route.map-deviations.no-reference">
        No reference, so no analysis results.
      </p>
    </ng-template>
    <ng-template #referenceAvailable>
      <div *ngIf="osmRelationAvailable$ | async; then osmRelationAvailable; else osmRelationMissing"></div>
      <ng-template #osmRelationMissing>
        <p i18n="@@monitor.route.map-deviations.no-relation">
          OSM relation missing in route definition, so no analysis results.
        </p>
      </ng-template>
      <ng-template #osmRelationAvailable>
        <p *ngIf="osmRelationEmpty$ | async"
           i18n="@@monitor.route.map-deviations.relation-empty">
          OSM relation empty, so no analysis results.
        </p>
        <div *ngIf="hasDeviations$ | async; then deviations; else noDeviations"></div>
        <ng-template #noDeviations>
          <p class="kpn-spacer-above kpn-line">
            <kpn-icon-happy />
            <span i18n="@@monitor.route.map-deviations.no-deviations">
              No deviations
            </span>
          </p>
        </ng-template>

        <ng-template #deviations>
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
              *ngFor="let deviation of deviations$ | async"
              [selected]="(selectedDeviationId$ | async) === deviation.id"
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
})
export class MonitorRouteMapDeviationsComponent {
  readonly deviations$ = this.store.select(selectMonitorRouteMapDeviations);

  readonly hasDeviations$ = this.deviations$.pipe(
    map((deviations) => deviations.length > 0)
  );

  readonly referenceAvailable$ = this.store.select(
    selectMonitorRouteMapReferenceEnabled
  );

  readonly osmRelationAvailable$ = this.store.select(
    selectMonitorRouteMapOsmRelationAvailable
  );

  readonly osmRelationEmpty$ = this.store.select(
    selectMonitorRouteMapOsmRelationEmpty
  );

  readonly selectedDeviationId$ = this.store.select(
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
