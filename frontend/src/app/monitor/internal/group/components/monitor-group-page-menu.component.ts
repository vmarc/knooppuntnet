import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { PageMenuOptionComponent } from '@app/shared/components/menu/page-menu-option.component';
import { PageMenuComponent } from '@app/shared/components/menu/page-menu.component';

@Component({
  selector: 'kpn-monitor-group-page-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page-menu>
      <kpn-page-menu-option
        [link]="'/monitor/groups/' + groupName()"
        [active]="pageName() === 'routes'"
        i18n="@@monitor.group.menu.routes"
      >
        Routes
      </kpn-page-menu-option>
    </kpn-page-menu>
    <kpn-error />
  `,
  imports: [PageMenuComponent, PageMenuOptionComponent, ErrorComponent],
})
export class MonitorGroupPageMenuComponent {
  pageName = input.required<string>();
  groupName = input.required<string>();
}
