import { inject } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouteType } from '@api/common/route-type';
import { NzDividerComponent } from 'ng-zorro-antd/divider';
import { NzDropdownMenuComponent } from 'ng-zorro-antd/dropdown';
import { NzDropDownDirective } from 'ng-zorro-antd/dropdown';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NzMenuItemComponent } from 'ng-zorro-antd/menu';
import { NzMenuDirective } from 'ng-zorro-antd/menu';
import { ActionService } from './action.service';

@Component({
  selector: 'kpn-action-button-route',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nz-icon nzType="open-in-new" class="action-button-icon" nz-dropdown [nzDropdownMenu]="menu" />
    <nz-dropdown-menu #menu="nzDropdownMenu">
      <ul nz-menu>
        <li nz-menu-item (click)="josmLoad($event)">JOSM load route relation</li>
        <li nz-menu-item (click)="josmLoadFull($event)">JOSM load relation and members</li>
        <li nz-menu-item (click)="josmZoom($event)">JOSM zoom/pan to route</li>
        <nz-divider />
        <li nz-menu-item (click)="id($event)">Open in iD</li>
        <li nz-menu-item (click)="osm($event)">Open in openstreetmap.org</li>
        <li nz-menu-item (click)="deepHistory($event)">Open in OSM Deep History</li>
        @if (routeType()) {
          <nz-divider />
          <li nz-menu-item (click)="waymarkedTrails($event)">Waymarked Trails</li>
        }
      </ul>
    </nz-dropdown-menu>
  `,
  imports: [
    NzDropDownDirective,
    NzDropdownMenuComponent,
    NzIconDirective,
    NzMenuDirective,
    NzMenuItemComponent,
    NzDividerComponent,
  ],
})
export class ActionButtonRouteComponent {
  relationId = input.required<number>();
  routeType = input<RouteType | undefined>(undefined);

  private readonly actionService = inject(ActionService);

  josmLoad(event: MouseEvent): void {
    event.stopPropagation();
    this.actionService.josmLoadRelation(this.relationId());
  }

  josmLoadFull(event: MouseEvent): void {
    event.stopPropagation();
    this.actionService.josmLoadRelationAndMembers(this.relationId());
  }

  josmZoom(event: MouseEvent): void {
    event.stopPropagation();
    this.actionService.josmZoomRelation(this.relationId());
  }

  id(event: MouseEvent): void {
    event.stopPropagation();
    this.actionService.idRelation(this.relationId());
  }

  osm(event: MouseEvent): void {
    event.stopPropagation();
    this.actionService.osmRelation(this.relationId());
  }

  deepHistory(event: MouseEvent): void {
    event.stopPropagation();
    this.actionService.deepHistoryRelation(this.relationId());
  }

  waymarkedTrails(event: MouseEvent): void {
    event.stopPropagation();
    this.actionService.waymarkedTrails(this.routeType(), this.relationId());
  }
}
