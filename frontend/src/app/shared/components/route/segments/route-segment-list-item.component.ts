import { computed } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SegmentInfo } from '@api/common/route/segment-info';
import { SegmentColors } from '@app/map/domain/segment-colors';
import { LegendLineComponent } from '@app/shared/components/legend-line';
import { DistancePipe } from '@app/shared/components/format/distance.pipe';

@Component({
  selector: 'ui-route-segment-list-item',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="segment">
      <span class="segment-id">{{ id() }}</span>
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
  readonly segment = input.required<SegmentInfo>();

  protected readonly id = computed(() => this.segment().id);
  protected readonly meters = computed(() => this.segment().meters);
  protected readonly segmentColor = computed(() => SegmentColors.colorForSegmentId(this.id()));
}
