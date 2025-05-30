import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { State } from '@app/state/state';
import { NzDropdownMenuComponent } from 'ng-zorro-antd/dropdown';
import { NzDropDownDirective } from 'ng-zorro-antd/dropdown';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NzMenuDirective } from 'ng-zorro-antd/menu';
import { ToolbarRouteTypeMenuItemComponent } from './toolbar-route-type-menu-item.component';

@Component({
  selector: 'ui-toolbar-route-type-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button
      nz-dropdown
      [nzDropdownMenu]="routeTypeMenu"
      class="ant-btn ant-btn-icon-only menu-button"
    >
      <nz-icon [nzType]="routeType()" />
      <nz-icon nzType="down" />
    </button>

    <nz-dropdown-menu #routeTypeMenu="nzDropdownMenu">
      <ul nz-menu>
        <ui-toolbar-route-type-menu-item
          routeType="hiking"
          i18n-label="@@route-type.hiking"
          label="Hiking"
        />
        <ui-toolbar-route-type-menu-item
          routeType="cycling"
          i18n-label="@@route-type.cycling"
          label="Cycling"
        />
        <ui-toolbar-route-type-menu-item
          routeType="horse-riding"
          i18n-label="@@route-type.horse-riding"
          label="Horse riding"
        />
        <ui-toolbar-route-type-menu-item
          routeType="motorboat"
          i18n-label="@@route-type.motorboat"
          label="Motorboat"
        />
        <ui-toolbar-route-type-menu-item
          routeType="canoe"
          i18n-label="@@route-type.canoe"
          label="Canoe"
        />
        <ui-toolbar-route-type-menu-item
          routeType="inline-skating"
          i18n-label="@@route-type.inline-skating"
          label="Inline skating"
        />
      </ul>
    </nz-dropdown-menu>
  `,
  styles: `
    .menu-button {
      display: flex;
      align-items: center;
      padding-left: 0.5em;
      padding-right: 2.3em;
      gap: 0.2em;
    }
  `,
  imports: [
    FormsModule,
    MatButtonModule,
    MatIconModule,
    NzDropDownDirective,
    NzDropdownMenuComponent,
    NzIconDirective,
    NzMenuDirective,
    ToolbarRouteTypeMenuItemComponent,
  ],
})
export class ToolbarRouteTypeMenuComponent {
  private readonly state = inject(State);
  readonly routeType = this.state.page.routeType;
}
