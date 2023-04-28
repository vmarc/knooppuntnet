import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { PageMenuOptionComponent } from '@app/components/shared/menu';
import { PageMenuComponent } from '@app/components/shared/menu';

@Component({
  selector: 'kpn-monitor-page-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page-menu>
      <kpn-page-menu-option
        link="/monitor"
        [active]="pageName === 'groups'"
        i18n="@@monitor.menu.groups"
      >
        Groups
      </kpn-page-menu-option>
      <kpn-page-menu-option
        link="/monitor/about"
        [active]="pageName === 'about'"
        i18n="@@monitor.menu.about"
      >
        About
      </kpn-page-menu-option>
    </kpn-page-menu>
  `,
  standalone: true,
  imports: [PageMenuComponent, PageMenuOptionComponent],
})
export class MonitorPageMenuComponent {
  @Input() pageName: string;
}
