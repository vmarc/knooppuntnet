import { inject } from '@angular/core';
import { computed } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatListModule, MatSelectionListChange } from '@angular/material/list';
import { MonitorRouteSegment } from '@api/common/monitor';
import { DistancePipe } from '@app/components/shared/format';
import { LegendLineComponent } from './legend-line';
import { MonitorRouteMapStateService } from './monitor-route-map-state.service';
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
      @for (segment of osmSegments(); track segment.id) {
        <mat-list-option
          [value]="segment"
          [selected]="mapStateService.selectedOsmSegment()?.id === segment.id"
        >
          <div class="segment">
            <span class="segment-id">{{ segment.id }}</span>
            <span class="segment-legend">
              <kpn-legend-line [color]="segmentColor(segment)" />
            </span>
            <span>{{ segment.meters | distance }}</span>
          </div>
        </mat-list-option>
      }
    </mat-selection-list>
  `,
  styles: `
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
  standalone: true,
  imports: [MatListModule, LegendLineComponent, DistancePipe],
})
export class MonitorRouteMapOsmSegmentsComponent {
  protected readonly mapStateService = inject(MonitorRouteMapStateService);
  private readonly mapService = inject(MonitorRouteMapService);

  readonly osmSegments = computed(() => {
    return this.mapStateService.page()?.osmSegments ?? [];
  });

  selectionChanged(event: MatSelectionListChange): void {
    if (event.options.length > 0) {
      const segment = event.options[0].value;
      this.mapStateService.selectedOsmSegmentChanged(segment);
    }
  }

  segmentColor(segment: MonitorRouteSegment): string {
    return this.mapService.colorForSegmentId(segment.id);
  }
}
