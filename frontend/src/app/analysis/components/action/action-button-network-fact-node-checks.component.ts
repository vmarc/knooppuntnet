import { inject } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NetworkFact } from '@api/common/network-fact';
import { ActionButtonComponent } from '@app/analysis/components/action/action-button.component';
import { ActionMenuItemComponent } from '@app/analysis/components/action/action-menu-item.component';
import { NzDropdownMenuComponent } from 'ng-zorro-antd/dropdown';
import { NzMenuDirective } from 'ng-zorro-antd/menu';
import { ActionService } from './action.service';

@Component({
  selector: 'ui-action-button-network-fact-node-checks',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-action-button [nzDropdownMenu]="menu" />
    <nz-dropdown-menu #menu="nzDropdownMenu">
      <ul nz-menu>
        <ui-action-menu-item (action)="josmLoad()">JOSM load nodes</ui-action-menu-item>
      </ul>
    </nz-dropdown-menu>
  `,
  imports: [
    ActionButtonComponent,
    ActionMenuItemComponent,
    NzDropdownMenuComponent,
    NzMenuDirective,
  ],
})
export class ActionButtonNetworkFactNodeChecksComponent {
  readonly networkFact = input.required<NetworkFact>();

  private readonly actionService = inject(ActionService);

  josmLoad(): void {
    const nodeIds = this.networkFact().checks.map((check) => check.nodeId);
    this.actionService.josmLoadNodes(nodeIds);
  }
}
