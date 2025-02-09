import { inject } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ActionMenuItemComponent } from '@app/analysis/components/action/action-menu-item.component';
import { ActionMenuComponent } from '@app/analysis/components/action/action-menu.component';
import { NzMenuDividerDirective } from 'ng-zorro-antd/menu';
import { NzMenuDirective } from 'ng-zorro-antd/menu';
import { ActionService } from './action.service';

@Component({
  selector: 'kpn-action-button-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-action-menu>
      <ul nz-menu>
        <kpn-action-menu-item (action)="josmLoad()">JOSM load node</kpn-action-menu-item>
        <kpn-action-menu-item (action)="josmZoom()">JOSM zoom/pan to node</kpn-action-menu-item>
        <li nz-menu-divider></li>
        <kpn-action-menu-item (action)="id()">Open in iD</kpn-action-menu-item>
        <kpn-action-menu-item (action)="osm()">Open in openstreetmap.org</kpn-action-menu-item>
        <kpn-action-menu-item (action)="deepHistory()"
          >Open in OSM Deep History</kpn-action-menu-item
        >
      </ul>
    </kpn-action-menu>
  `,
  imports: [NzMenuDirective, NzMenuDividerDirective, ActionMenuComponent, ActionMenuItemComponent],
})
export class ActionButtonNodeComponent {
  nodeId = input.required<number>();

  private readonly actionService = inject(ActionService);

  josmLoad(): void {
    this.actionService.josmLoadNode(this.nodeId());
  }

  josmZoom(): void {
    this.actionService.josmZoomNode(this.nodeId());
  }

  id(): void {
    this.actionService.idNode(this.nodeId());
  }

  osm(): void {
    this.actionService.osmNode(this.nodeId());
  }

  deepHistory(): void {
    this.actionService.deepHistoryNode(this.nodeId());
  }
}
