import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { RouteStructureRow } from '@api/common/route';
import { ActionButtonNodeComponent } from '../../../components/action/action-button-node.component';
import { ActionButtonRelationComponent } from '../../../components/action/action-button-relation.component';
import { ActionButtonWayComponent } from '../../../components/action/action-button-way.component';

@Component({
  selector: 'kpn-route-member-id',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let r = row();
    <div class="kpn-align-center">
      @switch (r.memberType) {
        @case ('node') {
          <kpn-action-button-node [nodeId]="r.id" />
          {{ r.id }}
        }
        @case ('way') {
          <kpn-action-button-way [wayId]="r.id" />
          {{ r.id }}
        }
        @case ('relation') {
          <kpn-action-button-relation [relationId]="r.id" />
          <a [routerLink]="'/analysis/route/' + r.id">{{ r.id }}</a>
        }
      }
    </div>
  `,
  imports: [
    MatIconModule,
    ActionButtonRelationComponent,
    ActionButtonNodeComponent,
    ActionButtonWayComponent,
    RouterLink,
  ],
})
export class RouteMemberIdComponent {
  row = input.required<RouteStructureRow>();
}
