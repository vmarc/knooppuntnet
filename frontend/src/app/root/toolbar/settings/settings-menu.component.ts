import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { TuiDropdownOpen } from '@taiga-ui/core';
import { TuiButton } from '@taiga-ui/core';
import { TuiDropdownDirective } from '@taiga-ui/core';
import { SettingsMenuOptionsComponent } from './settings-menu-options.component';

@Component({
  selector: 'kpn-settings-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button
      iconStart="@tui.settings"
      title="Settings"
      tuiIconButton
      type="button"
      tuiDropdownOpen
      [tuiDropdown]="settingsMenu"
    ></button>
    <ng-template #settingsMenu let-close>
      <kpn-settings-menu-options />
    </ng-template>
  `,
  imports: [SettingsMenuOptionsComponent, TuiButton, TuiDropdownDirective, TuiDropdownOpen],
})
export class SettingsMenuComponent {}
