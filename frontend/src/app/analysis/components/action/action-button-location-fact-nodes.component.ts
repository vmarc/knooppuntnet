import { inject } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { LocationFact } from '@api/common/location/location-fact';
import { NzDropdownMenuComponent } from 'ng-zorro-antd/dropdown';
import { NzDropDownDirective } from 'ng-zorro-antd/dropdown';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NzMenuItemComponent } from 'ng-zorro-antd/menu';
import { NzMenuDirective } from 'ng-zorro-antd/menu';
import { ActionService } from './action.service';

@Component({
  selector: 'kpn-action-button-location-fact-nodes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nz-icon nzType="open-in-new" class="action-button-icon" nz-dropdown [nzDropdownMenu]="menu" />
    <nz-dropdown-menu #menu="nzDropdownMenu">
      <ul nz-menu>
        <li nz-menu-item (click)="josmLoad($event)">JOSM load nodes</li>
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
export class ActionButtonLocationFactNodesComponent {
  locationFact = input.required<LocationFact>();

  private readonly actionService = inject(ActionService);

  josmLoad(event: MouseEvent): void {
    event.stopPropagation();
    const nodeIds = this.locationFact().refs.map((ref) => ref.id);
    this.actionService.josmLoadNodes(nodeIds);
  }
}
