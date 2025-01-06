import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltip } from '@angular/material/tooltip';
import { RouteStructureRow } from '@api/common/route';
import { DistancePipe } from '@app/components/shared/format';

@Component({
  selector: 'kpn-route-distance',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let row = structureRow();

    @if (row.memberType === 'way') {
      <span class="distance">{{ row.distance | distance }}</span>
    } @else if (row.memberType === 'relation') {
      <span class="distance">
        @if (row.distance !== row.relation.totalDistance && row.relation.totalDistance > 0) {
          <span class="cumulative-distance" matTooltip="Total length of ways in all subrelations">
            {{ row.relation.totalDistance | distance }}
          </span>
        }
        @if (row.distance !== row.relation.totalDistance && row.distance > 0) {
          <span>/</span>
        }
        @if (row.distance > 0) {
          <span matTooltip="Total length of ways in this relation">
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
  imports: [MatIconModule, DistancePipe, MatTooltip],
})
export class RouteDistanceComponent {
  structureRow = input.required<RouteStructureRow>();
}
