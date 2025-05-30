import { inject } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ActionButtonComponent } from '@app/analysis/components/action/action-button.component';
import { ActionMenuItemComponent } from '@app/analysis/components/action/action-menu-item.component';
import { NzDropdownMenuComponent } from 'ng-zorro-antd/dropdown';
import { NzMenuDividerDirective } from 'ng-zorro-antd/menu';
import { NzMenuDirective } from 'ng-zorro-antd/menu';
import { ActionService } from './action.service';

@Component({
  selector: 'ui-action-button-way',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-action-button [nzDropdownMenu]="menu" />
    <nz-dropdown-menu #menu="nzDropdownMenu">
      <ul nz-menu>
        <ui-action-menu-item (action)="josmLoad()">JOSM load way</ui-action-menu-item>
        <ui-action-menu-item (action)="josmZoom()">JOSM zoom/pan to way</ui-action-menu-item>
        <li nz-menu-divider></li>
        <ui-action-menu-item (action)="id()">Open in iD</ui-action-menu-item>
        <ui-action-menu-item (action)="osm()">Open in openstreetmap.org</ui-action-menu-item>
        <ui-action-menu-item (action)="deepHistory()">
          Open in OSM Deep History
        </ui-action-menu-item>
      </ul>
    </nz-dropdown-menu>
  `,
  imports: [
    ActionButtonComponent,
    ActionMenuItemComponent,
    NzDropdownMenuComponent,
    NzMenuDirective,
    NzMenuDividerDirective,
  ],
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
