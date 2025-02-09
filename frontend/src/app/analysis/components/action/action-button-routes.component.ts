import { inject } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ActionButtonComponent } from '@app/analysis/components/action/action-button.component';
import { ActionMenuItemComponent } from '@app/analysis/components/action/action-menu-item.component';
import { NzDropdownMenuComponent } from 'ng-zorro-antd/dropdown';
import { NzMenuDirective } from 'ng-zorro-antd/menu';
import { ActionService } from './action.service';

@Component({
  selector: 'kpn-action-button-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-action-button [nzDropdownMenu]="menu" />
    <nz-dropdown-menu #menu="nzDropdownMenu">
      <ul nz-menu>
        <kpn-action-menu-item (action)="josmLoad()">JOSM load route relation</kpn-action-menu-item>
        <kpn-action-menu-item (action)="josmLoadFull()">
          JOSM load relation and members
        </kpn-action-menu-item>
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
export class ActionButtonRoutesComponent {
  relationIds = input.required<number[]>();

  private readonly actionService = inject(ActionService);

  josmLoad(): void {
    this.actionService.josmLoadRelations(this.relationIds());
  }

  josmLoadFull(): void {
    this.actionService.josmLoadRelationsAndMembers(this.relationIds());
  }
}
