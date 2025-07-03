import { computed } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RoutePath } from '@api/common/route/route-path';
import { SegmentColors } from '@app/map/domain/segment-colors';
import { LegendLineComponent } from '@app/shared/components/legend-line';

@Component({
  selector: 'ui-route-path',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="segment">
      <span class="segment-id">{{ path().id }}</span>
      <span class="segment-legend">
        <ui-legend-line [color]="segmentColor()" />
      </span>
      <span>{{ name() }}</span>
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
  imports: [LegendLineComponent],
})
export class RoutePathComponent {
  readonly path = input.required<RoutePath>();
  protected readonly name = computed(() => this.path().name);
  protected readonly segmentColor = computed(() => SegmentColors.colorForSegmentId(this.path().id));
  protected readonly debug = computed(() => JSON.stringify(this.path(), null, 2));
}
