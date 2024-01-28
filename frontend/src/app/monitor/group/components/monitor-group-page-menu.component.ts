import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ErrorComponent } from '@app/components/shared/error';
import { PageMenuOptionComponent } from '@app/components/shared/menu';
import { PageMenuComponent } from '@app/components/shared/menu';

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
  standalone: true,
  imports: [PageMenuComponent, PageMenuOptionComponent, ErrorComponent],
})
export class MonitorGroupPageMenuComponent {
  pageName = input.required<string>();
  groupName = input.required<string>();
}
