import { computed } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SuperSegment } from '@api/common/route/super-segment';
import { SegmentColors } from '@app/map/domain/segment-colors';
import { LegendLineComponent } from '@app/shared/components/legend-line';
import { DistancePipe } from '@app/shared/components/format/distance.pipe';

@Component({
  selector: 'ui-route-segment-list-item',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="segment">
      <span class="segment-id">{{ segment().id }}</span>
      <span class="segment-legend">
        <ui-legend-line [color]="segmentColor()" />
      </span>
      <span>{{ meters() | distance }}</span>
    </div>
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
  imports: [DistancePipe, LegendLineComponent],
})
export class RouteSegmentListItemComponent {
  readonly segment = input.required<SuperSegment>();
  readonly meters = computed(() =>
    this.segment()
      .segments.map((segment) => segment.info.meters)
      .reduce((sum, current) => sum + current, 0)
  );

  protected readonly segmentColor = computed(() =>
    SegmentColors.colorForSegmentId(this.segment().id)
  );
}
