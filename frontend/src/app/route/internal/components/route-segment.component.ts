import { computed } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouteSegment } from '@api/common/route/route-segment';
import { SegmentColors } from '@app/mapold/domain/segment-colors';
import { LegendLineComponent } from '@app/shared/components/legend-line';
import { DistancePipe } from '@app/shared/components/format/distance.pipe';

@Component({
  selector: 'ui-route-segment',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="segment">
      <span class="segment-id">{{ segment().id }}</span>
      <span class="segment-legend">
        <ui-legend-line [color]="segmentColor()" />
      </span>
      <span>{{ segment().meters | distance }}</span>
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
export class RouteSegmentComponent {
  readonly segment = input.required<RouteSegment>();
  protected readonly segmentColor = computed(() =>
    SegmentColors.colorForSegmentId(this.segment().id)
  );
}
