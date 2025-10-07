import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { StructureRow } from '@api/common/route/structure-row';
import { ActionButtonNodeComponent } from '@app/analysis/components/action/action-button-node.component';
import { ActionButtonRelationComponent } from '@app/analysis/components/action/action-button-relation.component';
import { ActionButtonWayComponent } from '@app/analysis/components/action/action-button-way.component';

@Component({
  selector: 'ui-route-member-id',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let row = structureRow();
    <div class="kpn-align-center">
      @switch (row.memberType) {
        @case ('node') {
          <ui-action-button-node [nodeId]="row.id" />
        }
        @case ('way') {
          <ui-action-button-way [wayId]="row.id" />
        }
        @case ('relation') {
          <ui-action-button-relation [relationId]="row.id" />
        }
      }
    </div>
  `,
  imports: [ActionButtonNodeComponent, ActionButtonRelationComponent, ActionButtonWayComponent],
})
export class RouteMemberIdComponent {
  readonly structureRow = input.required<StructureRow>();
}
