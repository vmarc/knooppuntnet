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
  selector: 'ui-action-button-relations',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-action-button [nzDropdownMenu]="menu" />
    <nz-dropdown-menu #menu="nzDropdownMenu">
      <ul nz-menu>
        <ui-action-menu-item (action)="josmLoadRelations()">
          JOSM load relations
        </ui-action-menu-item>
        <ui-action-menu-item (action)="josmLoadRelationsAndMembers()">
          JOSM load relations and members
        </ui-action-menu-item>
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
export class ActionButtonRelationsComponent {
  readonly relationIds = input.required<ReadonlyArray<number>>();

  private readonly actionService = inject(ActionService);

  josmLoadRelations(): void {
    this.actionService.josmLoadRelations(this.relationIds());
  }

  josmLoadRelationsAndMembers(): void {
    this.actionService.josmLoadRelationsAndMembers(this.relationIds());
  }
}
