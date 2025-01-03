import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouteStructureRow } from '@api/common/route';
import { MonitorRouteGapComponent } from '../../../../monitor/route/monitor-route-gap.component';

@Component({
  selector: 'kpn-route-member-image',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let r = row();
    @if (r.memberType !== 'relation') {
      <img [src]="'/assets/images/links/' + r.linkName + '.png'" [alt]="r.linkName" />
    } @else {
      gaps
      @if (r.relation) {
        @if (r.relation.gaps !== undefined) {
          <kpn-monitor-route-gap [description]="" [osmSegmentCount]="r.relation.osmSegmentCount" />
        }
      }
    }
  `,
  styles: `
    :host {
      display: flex;
      flex-direction: row;
    }

    :host img {
      width: 40px;
      height: 100%;
    }
  `,
  imports: [MatIconModule, MonitorRouteGapComponent],
})
export class RouteMemberImageComponent {
  row = input.required<RouteStructureRow>();
}
