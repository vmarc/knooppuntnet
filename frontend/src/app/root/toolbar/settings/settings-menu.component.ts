import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzDropdownMenuComponent } from 'ng-zorro-antd/dropdown';
import { NzDropDownDirective } from 'ng-zorro-antd/dropdown';
import { NzDropdownButtonDirective } from 'ng-zorro-antd/dropdown';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NzSubMenuComponent } from 'ng-zorro-antd/menu';
import { NzMenuDirective } from 'ng-zorro-antd/menu';
import { SettingsMenuLayersComponent } from './settings-menu-layers.component';
import { SettingsMenuMapOptionsComponent } from './settings-menu-map-options.component';
import { SettingsMenuScopeComponent } from './settings-menu-scope.component';

@Component({
  selector: 'kpn-settings-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button nz-button nz-dropdown [nzClickHide]="false" [nzDropdownMenu]="settingsMenu">
      <nz-icon nzType="setting" />
    </button>

    <nz-dropdown-menu #settingsMenu="nzDropdownMenu">
      <ul nz-menu>
        <li nz-submenu nzTitle="Map options">
          <ul>
            <kpn-settings-menu-map-options />
          </ul>
        </li>
        <li nz-submenu nzTitle="Scope">
          <ul>
            <kpn-settings-menu-scope />
          </ul>
        </li>
        <li nz-submenu nzTitle="Layers">
          <ul>
            <kpn-settings-menu-layers />
          </ul>
        </li>
      </ul>
    </nz-dropdown-menu>
  `,
  imports: [
    NzIconDirective,
    NzButtonComponent,
    NzDropdownButtonDirective,
    NzDropDownDirective,
    NzDropdownMenuComponent,
    NzMenuDirective,
    NzSubMenuComponent,
    SettingsMenuMapOptionsComponent,
    SettingsMenuScopeComponent,
    SettingsMenuLayersComponent,
  ],
})
export class SettingsMenuComponent {}
