import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouteStructureRow } from '@api/common/route/route-structure-row';
import { TryoutWrapperComponent } from '../../../../../tryout/canvas/tryout-wrapper.component';

@Component({
  selector: 'kpn-route-member-image',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let r = row();
    @if (r.memberType === 'node') {
      <kpn-tryout-wrapper [memberType]="r.memberType" [link]="null" />
    } @else if (r.memberType === 'way') {
      <kpn-tryout-wrapper [memberType]="r.memberType" [link]="r.link" />
    } @else if (r.memberType === 'relation') {
      <!--      @if (r.relation) {-->
      <kpn-tryout-wrapper [memberType]="r.memberType" [link]="null" />
      <!--        @if (r.relation.gaps !== undefined) {-->
      <!--          <kpn-monitor-route-gap [description]="" [osmSegmentCount]="r.relation.osmSegmentCount" />-->
      <!--        }-->
    }
    <!--    }-->
  `,
  styles: `
    :host {
      display: flex;
      flex-direction: row;
    }
  `,
  imports: [MatIconModule, TryoutWrapperComponent],
})
export class RouteMemberImageComponent {
  row = input.required<RouteStructureRow>();
}
