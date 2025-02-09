import { inject } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { LocationFact } from '@api/common/location/location-fact';
import { ActionMenuItemComponent } from '@app/analysis/components/action/action-menu-item.component';
import { ActionMenuComponent } from '@app/analysis/components/action/action-menu.component';
import { NzMenuDirective } from 'ng-zorro-antd/menu';
import { ActionService } from './action.service';

@Component({
  selector: 'kpn-action-button-location-fact-nodes',
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
export class ActionButtonLocationFactNodesComponent {
  locationFact = input.required<LocationFact>();

  private readonly actionService = inject(ActionService);

  josmLoad(): void {
    const nodeIds = this.locationFact().refs.map((ref) => ref.id);
    this.actionService.josmLoadNodes(nodeIds);
  }
}
