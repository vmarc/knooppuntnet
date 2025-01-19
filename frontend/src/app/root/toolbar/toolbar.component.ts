import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { SpinnerComponent } from '@app/spinner';
import { State } from '@app/state';
import { TuiButton } from '@taiga-ui/core';
import { TuiAppBarDirective } from '@taiga-ui/layout';
import { TuiAppBarComponent } from '@taiga-ui/layout';
import { SettingsMenuComponent } from './settings/settings-menu.component';
import { ToolbarPanelToggleComponent } from './toolbar-panel-toggle.component';
import { ToolbarTitleComponent } from './toolbar-title.component';

@Component({
  selector: 'kpn-toolbar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <tui-app-bar class="toolbar">
      <kpn-settings-menu tuiSlot="left" />
      <!-- <kpn-toolbar-route-type-menu /> -->
      <a tuiSlot="left" iconStart="@tui.user" title="User" tuiIconButton></a>
      <kpn-toolbar-title tuiSlot="left" />
      <kpn-spinner />
      <a tuiSlot="right" iconStart="@tui.user" title="User" tuiIconButton></a>

      @if (small()) {
        <kpn-toolbar-panel-toggle tuiSlot="right" />
      }
    </tui-app-bar>
  `,
  styles: `
    .toolbar {
      background-color: #f8f8f8;
      border-bottom: solid 1px lightgray;
    }
  `,
  imports: [
    MatButtonModule,
    MatIconModule,
    MatToolbarModule,
    SpinnerComponent,
    ToolbarPanelToggleComponent,
    ToolbarTitleComponent,
    TuiAppBarComponent,
    TuiAppBarDirective,
    TuiButton,
    SettingsMenuComponent,
  ],
})
export class ToolbarComponent {
  private readonly state = inject(State);
  readonly small = this.state.page.small;
}
