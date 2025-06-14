import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { OrphanNodeInfo } from '@api/common/orphan-node-info';
import { DayComponent } from '@app/shared/components/day/day.component';
import { DayPipe } from '@app/shared/components/format/day.pipe';
import { LinkNodeComponent } from '@app/shared/components/link/link-node.component';
import { ActionButtonNodeComponent } from '../../../../components/action/action-button-node.component';

@Component({
  selector: 'ui-subset-orphan-node-list-item',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let node = row();
    <div class="kpn-line">
      <span>{{ rowNumber() }}</span>
      <ui-action-button-node [nodeId]="node.id" />
      <ui-link-node [nodeId]="node.id" [nodeName]="node.name" />
      @if (node.longName) {
        <span>{{ node.longName }}</span>
      }
    </div>
    @if (node.lastSurvey) {
      <div>
        <span i18n="@@subset-orphan-nodes.table.last-survey" class="kpn-label">Survey</span>
        <span> {{ node.lastSurvey | day }} </span>
      </div>
    }
    <div>
      <span i18n="@@subset-orphan-nodes.table.last-edit" class="kpn-label">Last edit</span>
      <span>
        <ui-day [timestamp]="node.lastUpdated" />
      </span>
    </div>
  `,
  imports: [ActionButtonNodeComponent, DayComponent, DayPipe, LinkNodeComponent],
})
export class SubsetOrphanNodeListItemComponent {
  readonly rowNumber = input.required<number>();
  readonly row = input.required<OrphanNodeInfo>();
}
