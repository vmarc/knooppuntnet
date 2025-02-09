import { inject } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ActionMenuItemComponent } from '@app/analysis/components/action/action-menu-item.component';
import { ActionMenuComponent } from '@app/analysis/components/action/action-menu.component';
import { NzMenuDirective } from 'ng-zorro-antd/menu';
import { ActionService } from './action.service';

@Component({
  selector: 'kpn-action-button-nodes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-action-menu>
      <ul nz-menu>
        <kpn-action-menu-item (action)="josmLoad()">JOSM load nodes</kpn-action-menu-item>
      </ul>
    </kpn-action-menu>
  `,
  imports: [NzMenuDirective, ActionMenuComponent, ActionMenuItemComponent],
})
export class ActionButtonNodesComponent {
  nodeIds = input.required<number[]>();

  private readonly actionService = inject(ActionService);

  josmLoad(): void {
    this.actionService.josmLoadNodes(this.nodeIds());
  }
}
