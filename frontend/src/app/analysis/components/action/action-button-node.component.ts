import { inject } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NzDividerComponent } from 'ng-zorro-antd/divider';
import { NzDropdownMenuComponent } from 'ng-zorro-antd/dropdown';
import { NzDropDownDirective } from 'ng-zorro-antd/dropdown';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NzMenuItemComponent } from 'ng-zorro-antd/menu';
import { NzMenuDirective } from 'ng-zorro-antd/menu';
import { ActionService } from './action.service';

@Component({
  selector: 'kpn-action-button-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nz-icon nzType="open-in-new" class="action-button-icon" nz-dropdown [nzDropdownMenu]="menu" />
    <nz-dropdown-menu #menu="nzDropdownMenu">
      <ul nz-menu>
        <li nz-menu-item (click)="josmLoad($event)">JOSM load node</li>
        <li nz-menu-item (click)="josmZoom($event)">JOSM zoom/pan to node</li>
        <nz-divider />
        <li nz-menu-item (click)="id($event)">Open in iD</li>
        <li nz-menu-item (click)="osm($event)">Open in openstreetmap.org</li>
        <li nz-menu-item (click)="deepHistory($event)">Open in OSM Deep History</li>
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
export class ActionButtonNodeComponent {
  nodeId = input.required<number>();

  private readonly actionService = inject(ActionService);

  josmLoad(event: MouseEvent): void {
    event.stopPropagation();
    this.actionService.josmLoadNode(this.nodeId());
  }

  josmZoom(event: MouseEvent): void {
    event.stopPropagation();
    this.actionService.josmZoomNode(this.nodeId());
  }

  id(event: MouseEvent): void {
    event.stopPropagation();
    this.actionService.idNode(this.nodeId());
  }

  osm(event: MouseEvent): void {
    event.stopPropagation();
    this.actionService.osmNode(this.nodeId());
  }

  deepHistory(event: MouseEvent): void {
    event.stopPropagation();
    this.actionService.deepHistoryNode(this.nodeId());
  }
}
