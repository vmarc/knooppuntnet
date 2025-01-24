import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { State } from '@app/state';
import { NzHeaderComponent } from 'ng-zorro-antd/layout';
import { SettingsMenuComponent } from './settings/settings-menu.component';
import { ToolbarTitleComponent } from './toolbar-title.component';

@Component({
  selector: 'kpn-toolbar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nz-header class="toolbar">
      <div class="toolbar">
        <kpn-settings-menu />
        <!-- <kpn-toolbar-route-type-menu /> -->
        <!--      <a title="User"></a>-->
        <kpn-toolbar-title />
        <!--      <kpn-spinner />-->
        <!--      @if (small()) {-->
        <!--        <kpn-toolbar-panel-toggle />-->
        <!--      }-->
      </div>
    </nz-header>
  `,
  styles: `
    .toolbar {
      background-color: #f8f8f8;
      border-bottom: solid 1px lightgray;
    }
  `,
  imports: [SettingsMenuComponent, ToolbarTitleComponent, NzHeaderComponent],
})
export class ToolbarComponent {
  private readonly state = inject(State);
  readonly small = this.state.page.small;
}
