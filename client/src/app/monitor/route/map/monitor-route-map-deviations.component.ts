import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatSelectionListChange } from '@angular/material/list';
import { MonitorRouteDeviation } from '@app/kpn/api/common/monitor/monitor-route-deviation';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { AppState } from '../../../core/core.state';
import { actionMonitorRouteMapSelectDeviation } from '../../store/monitor.actions';
import { selectMonitorRouteMapReferenceEnabled } from '../../store/monitor.selectors';
import { selectMonitorRouteMapDeviations } from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-route-map-deviations',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div
      *ngIf="referenceAvailable$ | async; then gpxTrace; else noGpxTrace"
    ></div>
    <ng-template #noGpxTrace>
      <p i18n="@@monitor.route.map-deviations.no-gpx">
        No GPX, no known deviations
      </p>
    </ng-template>
    <ng-template #gpxTrace>
      <div
        *ngIf="hasDeviations$ | async; then deviations; else noDeviations"
      ></div>
      <ng-template #noDeviations>
        <p class="kpn-spacer-above kpn-line">
          <kpn-icon-happy></kpn-icon-happy>
          <span i18n="@@monitor.route.map-deviations.no-deviations">
            No deviations
          </span>
        </p>
      </ng-template>

      <ng-template #deviations>
        <p class="segments-title">
          <span i18n="@@monitor.route.map-deviations.title"
            >GPX segments where OSM is deviating</span
          >
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

        <mat-selection-list
          [multiple]="false"
          (selectionChange)="selectionChanged($event)"
        >
          <mat-list-option
            *ngFor="let segment of deviations$ | async"
            [value]="segment"
          >
            <div class="segment">
              <span class="segment-id">{{ segment.id }}</span>
              <span class="segment-deviation">{{
                segment.distance | distance
              }}</span>
              <span>{{ segment.meters | distance }}</span>
            </div>
          </mat-list-option>
        </mat-selection-list>
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

  constructor(private store: Store<AppState>) {}

  selectionChanged(event: MatSelectionListChange): void {
    let deviation: MonitorRouteDeviation = null;
    if (event.options.length > 0) {
      deviation = event.options[0].value;
    }
    this.store.dispatch(actionMonitorRouteMapSelectDeviation(deviation));
  }
}
