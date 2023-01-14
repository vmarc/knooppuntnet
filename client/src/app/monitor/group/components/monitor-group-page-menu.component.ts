import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'kpn-monitor-group-page-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page-menu>
      <kpn-page-menu-option
        [link]="'/monitor/groups/' + groupName"
        [active]="pageName === 'routes'"
        i18n="@@monitor.group.menu.routes"
      >
        Routes
      </kpn-page-menu-option>
    </kpn-page-menu>
    <kpn-error/>
  `,
})
export class MonitorGroupPageMenuComponent {
  @Input() pageName: string;
  @Input() groupName: string;
}
