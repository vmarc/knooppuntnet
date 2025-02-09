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
  selector: 'kpn-action-button-relation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-action-button [nzDropdownMenu]="menu" />
    <nz-dropdown-menu #menu="nzDropdownMenu">
      <ul nz-menu>
        <kpn-action-menu-item (action)="josmLoad()">JOSM load relation</kpn-action-menu-item>
        <kpn-action-menu-item (action)="josmLoadFull()">
          JOSM load relation and members
        </kpn-action-menu-item>
        <kpn-action-menu-item (action)="josmZoom()">JOSM zoom/pan to relation</kpn-action-menu-item>
        <li nz-menu-divider></li>
        <kpn-action-menu-item (action)="id()">Open in iD</kpn-action-menu-item>
        <kpn-action-menu-item (action)="osm()">Open in openstreetmap.org</kpn-action-menu-item>
        <kpn-action-menu-item (action)="deepHistory()">
          Open in OSM Deep History
        </kpn-action-menu-item>
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
export class ActionButtonRelationComponent {
  relationId = input.required<number>();

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
}
