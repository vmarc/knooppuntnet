import { inject } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ActionMenuComponent } from '@app/analysis/components/action/action-menu.component';
import { NzMenuItemComponent } from 'ng-zorro-antd/menu';
import { NzMenuDirective } from 'ng-zorro-antd/menu';
import { ActionService } from './action.service';

@Component({
  selector: 'kpn-action-button-ways',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-action-menu>
      <ul nz-menu>
        <li nz-menu-item (click)="josmLoad($event)">JOSM load ways</li>
      </ul>
    </kpn-action-menu>
  `,
  imports: [NzMenuDirective, NzMenuItemComponent, ActionMenuComponent],
})
export class ActionButtonWaysComponent {
  wayIds = input.required<number[]>();

  private readonly actionService = inject(ActionService);

  josmLoad(event: MouseEvent): void {
    event.stopPropagation();
    this.actionService.josmLoadWays(this.wayIds());
  }
}
