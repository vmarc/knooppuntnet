import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { TuiDropdownManual } from '@taiga-ui/core';
import { TuiDropdownOptionsDirective } from '@taiga-ui/core';
import { TuiDropdownPositionSided } from '@taiga-ui/core';
import { TuiDropdownDirective } from '@taiga-ui/core';
import { TuiOption } from '@taiga-ui/core';
import { TuiDataListComponent } from '@taiga-ui/core';
import { TuiDataListDropdownManager } from '@taiga-ui/kit';
import { SettingsMenuLayersComponent } from './settings-menu-layers.component';
import { SettingsMenuMapOptionsComponent } from './settings-menu-map-options.component';
import { SettingsMenuScopeComponent } from './settings-menu-scope.component';

@Component({
  selector: 'kpn-settings-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <tui-data-list tuiDataListDropdownManager size="m">
      <button
        iconStart="@tui.map"
        iconEnd="@tui.chevron-right"
        tuiDropdownAlign="right"
        tuiDropdownDirection="top"
        tuiDropdownManual
        tuiDropdownSided
        tuiOption
        type="button"
        [tuiDropdown]="mapOptionsMenu"
      >
        Map options
      </button>

      <button
        iconStart="@tui.telescope"
        iconEnd="@tui.chevron-right"
        tuiDropdownAlign="right"
        tuiDropdownDirection="top"
        tuiDropdownManual
        tuiDropdownSided
        tuiOption
        type="button"
        [tuiDropdown]="scopeMenu"
      >
        Scope
      </button>

      <button
        iconStart="@tui.layers"
        iconEnd="@tui.chevron-right"
        tuiDropdownAlign="right"
        tuiDropdownDirection="top"
        tuiDropdownManual
        tuiDropdownSided
        tuiOption
        type="button"
        [tuiDropdown]="layersMenu"
      >
        Layers
      </button>
    </tui-data-list>

    <ng-template #mapOptionsMenu let-close>
      <kpn-settings-menu-map-options />
    </ng-template>

    <ng-template #scopeMenu let-close>
      <kpn-settings-menu-scope />
    </ng-template>

    <ng-template #layersMenu let-close>
      <kpn-settings-menu-layers />
    </ng-template>
  `,
  imports: [
    SettingsMenuLayersComponent,
    SettingsMenuMapOptionsComponent,
    SettingsMenuScopeComponent,
    TuiDataListComponent,
    TuiDataListDropdownManager,
    TuiDropdownDirective,
    TuiDropdownManual,
    TuiDropdownOptionsDirective,
    TuiDropdownPositionSided,
    TuiOption,
  ],
})
export class SettingsMenuComponent {
  protected open1 = false;
  protected open2 = false;
  protected open3 = false;
}
