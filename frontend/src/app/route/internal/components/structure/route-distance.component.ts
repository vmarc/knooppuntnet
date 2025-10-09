import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { StructureRow } from '@api/common/route/structure-row';
import { DistancePipe } from '@app/shared/components/format/distance.pipe';
import { NzTooltipDirective } from 'ng-zorro-antd/tooltip';

@Component({
  selector: 'ui-route-distance',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let row = structureRow();

    @if (row.memberType === 'way') {
      <span class="distance">{{ row.distance | distance }}</span>
    } @else if (row.memberType === 'relation') {
      <span class="distance">
        @if (row.distance !== row.relation.totalDistance && row.relation.totalDistance > 0) {
          <span
            class="cumulative-distance"
            nz-tooltip
            nzTooltipTitle="Total length of ways in all subrelations"
          >
            {{ row.relation.totalDistance | distance }}
          </span>
        }
        @if (row.distance !== row.relation.totalDistance && row.distance > 0) {
          <span>/</span>
        }
        @if (row.distance > 0) {
          <span nz-tooltip nzTooltipTitle="Total length of ways in this relation">
            {{ row.distance | distance }}
          </span>
        }
      </span>
    }
  `,
  styles: `
    .distance {
      white-space: nowrap;
      text-align: right;
    }

    .cumulative-distance {
      font-weight: 800;
    }
  `,
  imports: [DistancePipe, DistancePipe, NzTooltipDirective],
})
export class RouteDistanceComponent {
  readonly structureRow = input.required<StructureRow>();
}
