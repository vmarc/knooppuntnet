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
  selector: 'kpn-action-button-network-fact-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-action-button [nzDropdownMenu]="menu" />
    <nz-dropdown-menu #menu="nzDropdownMenu">
      <ul nz-menu>
        <kpn-action-menu-item (action)="josmLoadRelations()">
          JOSM load route relations
        </kpn-action-menu-item>
        <kpn-action-menu-item (action)="josmLoadRelationsAndMembers()">
          JOSM load relations and members
        </kpn-action-menu-item>
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
export class ActionButtonNetworkFactRoutesComponent {
  networkFact = input.required<NetworkFact>();

  private readonly actionService = inject(ActionService);

  josmLoadRelations(): void {
    this.actionService.josmLoadRelations(this.relationIds());
  }

  josmLoadRelationsAndMembers(): void {
    this.actionService.josmLoadRelationsAndMembers(this.relationIds());
  }

  private relationIds(): Array<number> {
    return this.networkFact().elementIds;
  }
}
