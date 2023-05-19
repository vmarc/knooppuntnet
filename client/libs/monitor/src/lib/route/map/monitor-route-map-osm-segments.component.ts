import { AsyncPipe, NgFor } from '@angular/common';
import { computed } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatListModule, MatSelectionListChange } from '@angular/material/list';
import { MonitorRouteSegment } from '@api/common/monitor';
import { DistancePipe } from '@app/components/shared/format';
import { LegendLineComponent } from './legend-line';
import { MonitorRouteMapStateService } from './monitor-route-map-state.service';

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
        *ngFor="let segment of osmSegments()"
        [value]="segment"
        [selected]="service.selectedOsmSegment()?.id === segment.id"
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
  standalone: true,
  imports: [MatListModule, NgFor, LegendLineComponent, AsyncPipe, DistancePipe],
})
export class MonitorRouteMapOsmSegmentsComponent {
  readonly osmSegments = computed(() => {
    return this.service.page()?.osmSegments ?? [];
  });

  constructor(protected service: MonitorRouteMapStateService) {}

  selectionChanged(event: MatSelectionListChange): void {
    if (event.options.length > 0) {
      const segment = event.options[0].value;
      this.service.selectedOsmSegmentChanged(segment);
    }
  }

  segmentColor(segment: MonitorRouteSegment): string {
    // TODO move color logic into separate class?
    // return this.service.colorForSegmentId(segment.id);
    return 'red';
  }
}
