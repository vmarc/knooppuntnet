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
    @let r = row();

    @if (r.memberType === 'node') {
      <span>-</span>
    } @else if (r.memberType === 'way') {
      <span>{{ r.distance | distance }}</span>
    } @else if (r.memberType === 'relation') {
      <span class="distance">
        @if (r.distance !== r.relation.totalDistance && r.relation.totalDistance > 0) {
          <span class="cumulative-distance" matTooltip="Total length of ways in all subrelations">
            {{ r.relation.totalDistance | distance }}
          </span>
        }
        @if (r.distance !== r.relation.totalDistance && r.distance > 0) {
          <span>/</span>
        }
        @if (r.distance > 0) {
          <span matTooltip="Total length of ways in this relation">
            {{ r.distance | distance }}
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
  row = input.required<RouteStructureRow>();
}
