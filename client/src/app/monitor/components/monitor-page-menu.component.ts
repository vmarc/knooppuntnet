import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'kpn-monitor-page-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page-menu>
      <kpn-page-menu-option link="/monitor" [active]="pageName === 'groups'">
        Groups
      </kpn-page-menu-option>
      <kpn-page-menu-option
        link="/monitor/changes"
        [active]="pageName === 'changes'"
      >
        Changes
      </kpn-page-menu-option>
      <kpn-page-menu-option
        link="/monitor/about"
        [active]="pageName === 'about'"
      >
        About
      </kpn-page-menu-option>
    </kpn-page-menu>
  `,
})
export class MonitorPageMenuComponent {
  @Input() pageName: string;
}
