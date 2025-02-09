import { inject } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NetworkFact } from '@api/common/network-fact';
import { NzDropdownMenuComponent } from 'ng-zorro-antd/dropdown';
import { NzDropDownDirective } from 'ng-zorro-antd/dropdown';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NzMenuItemComponent } from 'ng-zorro-antd/menu';
import { NzMenuDirective } from 'ng-zorro-antd/menu';
import { ActionService } from './action.service';

@Component({
  selector: 'kpn-action-button-network-fact-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nz-icon nzType="open-in-new" class="action-button-icon" nz-dropdown [nzDropdownMenu]="menu" />
    <nz-dropdown-menu #menu="nzDropdownMenu">
      <ul nz-menu>
        <li nz-menu-item (click)="josmLoadRelations($event)">JOSM load route relations</li>
        <li nz-menu-item (click)="josmLoadRelationsAndMembers($event)">
          JOSM load relations and members
        </li>
      </ul>
    </nz-dropdown-menu>
  `,
  imports: [
    NzDropDownDirective,
    NzDropdownMenuComponent,
    NzIconDirective,
    NzMenuDirective,
    NzMenuItemComponent,
  ],
})
export class ActionButtonNetworkFactRoutesComponent {
  networkFact = input.required<NetworkFact>();

  private readonly actionService = inject(ActionService);

  josmLoadRelations(event: MouseEvent): void {
    event.stopPropagation();
    this.actionService.josmLoadRelations(this.relationIds());
  }

  josmLoadRelationsAndMembers(event: MouseEvent): void {
    event.stopPropagation();
    this.actionService.josmLoadRelationsAndMembers(this.relationIds());
  }

  private relationIds(): Array<number> {
    return this.networkFact().elementIds;
  }
}
