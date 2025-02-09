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
  selector: 'kpn-action-button-way',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-action-menu>
      <ul nz-menu>
        <kpn-action-menu-item (action)="josmLoad()">JOSM load way</kpn-action-menu-item>
        <kpn-action-menu-item (action)="josmZoom()">JOSM zoom/pan to way</kpn-action-menu-item>
        <li nz-menu-divider></li>
        <kpn-action-menu-item (action)="id()">Open in iD</kpn-action-menu-item>
        <kpn-action-menu-item (action)="osm()">Open in openstreetmap.org</kpn-action-menu-item>
        <kpn-action-menu-item (action)="deepHistory()">
          Open in OSM Deep History
        </kpn-action-menu-item>
      </ul>
    </kpn-action-menu>
  `,
  imports: [NzMenuDirective, NzMenuDividerDirective, ActionMenuComponent, ActionMenuItemComponent],
})
export class ActionButtonWayComponent {
  wayId = input.required<number>();

  private readonly actionService = inject(ActionService);

  josmLoad(): void {
    this.actionService.josmLoadWay(this.wayId());
  }

  josmZoom(): void {
    this.actionService.josmZoomWay(this.wayId());
  }

  id(): void {
    this.actionService.idWay(this.wayId());
  }

  osm(): void {
    this.actionService.osmWay(this.wayId());
  }

  deepHistory(): void {
    this.actionService.deepHistoryWay(this.wayId());
  }
}
