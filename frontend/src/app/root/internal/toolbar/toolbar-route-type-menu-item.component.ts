import { inject } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouteType } from '@api/common/route-type';
import { State } from '@app/state/state';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NzMenuItemComponent } from 'ng-zorro-antd/menu';

@Component({
  selector: 'ui-toolbar-route-type-menu-item',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <li nz-menu-item (click)="clicked()">
      <div class="item">
        <nz-icon [nzType]="routeType()" />
        <span>{{ label() }}</span>
      </div>
    </li>
  `,
  styles: [
    `
      .item {
        display: flex;
        align-items: center;
        gap: 1em;
      }
    `,
  ],
  imports: [NzIconDirective, NzMenuItemComponent],
})
export class ToolbarRouteTypeMenuItemComponent {
  private readonly state = inject(State);

  readonly routeType = input.required<RouteType>();
  readonly label = input.required<string>();

  clicked(): void {
    this.state.page.updateRouteType(this.routeType());
  }
}
