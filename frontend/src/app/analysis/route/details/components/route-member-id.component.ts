import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouteStructureRow } from '@api/common/route';
import { ActionButtonNodeComponent } from '../../../components/action/action-button-node.component';
import { ActionButtonRelationComponent } from '../../../components/action/action-button-relation.component';
import { ActionButtonWayComponent } from '../../../components/action/action-button-way.component';

@Component({
  selector: 'kpn-route-member-id',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let row = structureRow();
    <div class="kpn-align-center">
      @switch (row.memberType) {
        @case ('node') {
          <kpn-action-button-node [nodeId]="row.id" />
        }
        @case ('way') {
          <kpn-action-button-way [wayId]="row.id" />
        }
        @case ('relation') {
          <kpn-action-button-relation [relationId]="row.id" />
        }
      }
    </div>
  `,
  imports: [
    MatIconModule,
    ActionButtonRelationComponent,
    ActionButtonNodeComponent,
    ActionButtonWayComponent,
  ],
})
export class RouteMemberIdComponent {
  structureRow = input.required<RouteStructureRow>();
}
