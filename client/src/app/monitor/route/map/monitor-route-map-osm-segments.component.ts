import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatSelectionListChange } from '@angular/material/list';
import { MonitorRouteSegment } from '@api/common/monitor/monitor-route-segment';
import { Store } from '@ngrx/store';
import { actionMonitorRouteMapSelectOsmSegment } from './store/monitor-route-map.actions';
import { selectMonitorRouteMapSelectedOsmSegmentId } from './store/monitor-route-map.selectors';
import { selectMonitorRouteMapOsmSegments } from './store/monitor-route-map.selectors';
import { MonitorRouteMapService } from './monitor-route-map.service';

@Component({
  selector: 'kpn-monitor-route-map-osm-segments',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-selection-list
      [multiple]="false"
      (selectionChange)="selectionChanged($event)"
      [hideSingleSelectionIndicator]="true"
    >
      <mat-list-option
        *ngFor="let segment of segments$ | async"
        [value]="segment"
        [selected]="(selectedSegmentId$ | async) === segment.id"
      >
        <div class="segment">
          <span class="segment-id">{{ segment.id }}</span>
          <span class="segment-legend">
            <kpn-legend-line [color]="segmentColor(segment)" />
          </span>
          <span>{{ segment.meters | distance }}</span>
        </div>
      </mat-list-option>
    </mat-selection-list>
  `,
  styles: [
    `
      .segment {
        display: flex;
      }

      .segment-id {
        width: 2em;
      }

      .segment-legend {
        width: 3em;
      }
    `,
  ],
})
export class MonitorRouteMapOsmSegmentsComponent {
  readonly segments$ = this.store.select(selectMonitorRouteMapOsmSegments);
  readonly selectedSegmentId$ = this.store.select(
    selectMonitorRouteMapSelectedOsmSegmentId
  );

  constructor(
    private mapService: MonitorRouteMapService,
    private store: Store
  ) {}

  selectionChanged(event: MatSelectionListChange): void {
    let segment: MonitorRouteSegment = null;
    if (event.options.length > 0) {
      segment = event.options[0].value;
    }
    this.store.dispatch(actionMonitorRouteMapSelectOsmSegment(segment));
  }

  segmentColor(segment: MonitorRouteSegment): string {
    return this.mapService.colorForSegmentId(segment.id);
  }
}
