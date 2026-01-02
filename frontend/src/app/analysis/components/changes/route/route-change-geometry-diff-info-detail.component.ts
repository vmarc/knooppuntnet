import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { GeometryDiffInfoDetail } from '@api/common/route/geometry-diff-info-detail';
import { DistancePipe } from '@app/shared/components/format/distance.pipe';
import { IntegerFormatPipe } from '@app/shared/components/format/integer-format.pipe';

@Component({
  selector: 'ui-route-change-geometry-diff-info-detail',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-line">
      <svg height="30" width="50">
        <line x1="0" y1="15" x2="50" y2="15" [style]="lineStyle()" />
      </svg>
      <span>{{ title() }}</span>
      <div class="kpn-brackets">
        <div class="kpn-comma-list">
          <span>{{ detail().wayCount }} <span>ways</span></span>
          <span>{{ detail().nodeCount | integer }} <span>nodes</span></span>
          <span>{{ detail().meters | distance }} </span>
        </div>
      </div>
    </div>
  `,
  imports: [DistancePipe, IntegerFormatPipe],
})
export class RouteChangeGeometryDiffInfoDetailComponent {
  readonly title = input.required<string>();
  readonly detail = input.required<GeometryDiffInfoDetail>();
  readonly color = input.required<string>();
  readonly lineStyle = computed(() => `stroke: ${this.color()}; stroke-width: 3;`);
}
