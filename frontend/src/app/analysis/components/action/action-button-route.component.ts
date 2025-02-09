import { inject } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouteType } from '@api/common/route-type';
import { ActionMenuItemComponent } from '@app/analysis/components/action/action-menu-item.component';
import { ActionMenuComponent } from '@app/analysis/components/action/action-menu.component';
import { NzMenuDividerDirective } from 'ng-zorro-antd/menu';
import { NzMenuDirective } from 'ng-zorro-antd/menu';
import { ActionService } from './action.service';

@Component({
  selector: 'kpn-action-button-route',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-action-menu>
      <ul nz-menu>
        <kpn-action-menu-item (action)="josmLoad()">JOSM load route relation</kpn-action-menu-item>
        <kpn-action-menu-item (action)="josmLoadFull()">
          JOSM load relation and members
        </kpn-action-menu-item>
        <kpn-action-menu-item (action)="josmZoom()">JOSM zoom/pan to route</kpn-action-menu-item>
        <li nz-menu-divider></li>
        <kpn-action-menu-item (action)="id()">Open in iD</kpn-action-menu-item>
        <kpn-action-menu-item (action)="osm()">Open in openstreetmap.org</kpn-action-menu-item>
        <kpn-action-menu-item (action)="deepHistory()">
          Open in OSM Deep History
        </kpn-action-menu-item>
        @if (routeType()) {
          <li nz-menu-divider></li>
          <kpn-action-menu-item (action)="waymarkedTrails()">Waymarked Trails</kpn-action-menu-item>
        }
      </ul>
    </kpn-action-menu>
  `,
  imports: [NzMenuDirective, NzMenuDividerDirective, ActionMenuComponent, ActionMenuItemComponent],
})
export class ActionButtonRouteComponent {
  relationId = input.required<number>();
  routeType = input<RouteType | undefined>(undefined);

  private readonly actionService = inject(ActionService);

  josmLoad(): void {
    this.actionService.josmLoadRelation(this.relationId());
  }

  josmLoadFull(): void {
    this.actionService.josmLoadRelationAndMembers(this.relationId());
  }

  josmZoom(): void {
    this.actionService.josmZoomRelation(this.relationId());
  }

  id(): void {
    this.actionService.idRelation(this.relationId());
  }

  osm(): void {
    this.actionService.osmRelation(this.relationId());
  }

  deepHistory(): void {
    this.actionService.deepHistoryRelation(this.relationId());
  }

  waymarkedTrails(): void {
    this.actionService.waymarkedTrails(this.routeType(), this.relationId());
  }
}
