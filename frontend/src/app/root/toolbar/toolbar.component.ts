import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SpinnerComponent } from '@app/shared/spinner/spinner.component';
import { State } from '@app/state/state';
import { NzHeaderComponent } from 'ng-zorro-antd/layout';
import { SettingsMenuComponent } from './settings/settings-menu.component';
import { ToolbarPanelToggleComponent } from './toolbar-panel-toggle.component';
import { ToolbarRouteTypeMenuComponent } from './toolbar-route-type-menu.component';
import { ToolbarTitleComponent } from './toolbar-title.component';

@Component({
  selector: 'kpn-toolbar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nz-header class="toolbar">
      <kpn-settings-menu />
      <kpn-toolbar-route-type-menu />
      <kpn-toolbar-title />
      <kpn-spinner />
      @if (small()) {
        <kpn-toolbar-panel-toggle />
      }
    </nz-header>
  `,
  styles: `
    .toolbar {
      display: flex;
      background-color: #f8f8f8;
      border-bottom: solid 1px lightgray;
      align-items: center;
      gap: 1em;
    }
  `,
  imports: [
    NzHeaderComponent,
    SettingsMenuComponent,
    SpinnerComponent,
    ToolbarPanelToggleComponent,
    ToolbarRouteTypeMenuComponent,
    ToolbarTitleComponent,
  ],
})
export class ToolbarComponent {
  private readonly state = inject(State);
  readonly small = this.state.page.small;
}
