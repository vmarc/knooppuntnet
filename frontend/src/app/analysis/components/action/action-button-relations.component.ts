import { inject } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ActionMenuItemComponent } from '@app/analysis/components/action/action-menu-item.component';
import { ActionMenuComponent } from '@app/analysis/components/action/action-menu.component';
import { NzMenuDirective } from 'ng-zorro-antd/menu';
import { ActionService } from './action.service';

@Component({
  selector: 'kpn-action-button-relations',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-action-menu>
      <ul nz-menu>
        <kpn-action-menu-item (action)="josmLoadRelations()">
          JOSM load relations
        </kpn-action-menu-item>
        <kpn-action-menu-item (action)="josmLoadRelationsAndMembers()">
          JOSM load relations and members
        </kpn-action-menu-item>
      </ul>
    </kpn-action-menu>
  `,
  imports: [NzMenuDirective, ActionMenuComponent, ActionMenuItemComponent],
})
export class ActionButtonRelationsComponent {
  relationIds = input.required<number[]>();

  private readonly actionService = inject(ActionService);

  josmLoadRelations(): void {
    this.actionService.josmLoadRelations(this.relationIds());
  }

  josmLoadRelationsAndMembers(): void {
    this.actionService.josmLoadRelationsAndMembers(this.relationIds());
  }
}
