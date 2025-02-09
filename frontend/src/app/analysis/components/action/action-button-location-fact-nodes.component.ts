import { inject } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { LocationFact } from '@api/common/location/location-fact';
import { ActionButtonComponent } from '@app/analysis/components/action/action-button.component';
import { ActionMenuItemComponent } from '@app/analysis/components/action/action-menu-item.component';
import { NzDropdownMenuComponent } from 'ng-zorro-antd/dropdown';
import { NzMenuDirective } from 'ng-zorro-antd/menu';
import { ActionService } from './action.service';

@Component({
  selector: 'kpn-action-button-location-fact-nodes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-action-button [nzDropdownMenu]="menu" />
    <nz-dropdown-menu #menu="nzDropdownMenu">
      <ul nz-menu>
        <kpn-action-menu-item (action)="josmLoad()">JOSM load nodes</kpn-action-menu-item>
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
export class ActionButtonLocationFactNodesComponent {
  locationFact = input.required<LocationFact>();

  private readonly actionService = inject(ActionService);

  josmLoad(): void {
    const nodeIds = this.locationFact().refs.map((ref) => ref.id);
    this.actionService.josmLoadNodes(nodeIds);
  }
}
