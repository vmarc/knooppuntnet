import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { PageMenuOptionComponent } from '@app/components/shared/menu';
import { PageMenuComponent } from '@app/components/shared/menu';
import { StatusLinks } from './status-links';

@Component({
  selector: 'kpn-status-page-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <kpn-page-menu>
      <kpn-page-menu-option [link]="links.hour" [active]="periodType === 'hour'"
        >Hour
      </kpn-page-menu-option>
      <kpn-page-menu-option [link]="links.day" [active]="periodType === 'day'"
        >Day
      </kpn-page-menu-option>
      <kpn-page-menu-option [link]="links.week" [active]="periodType === 'week'"
        >Week
      </kpn-page-menu-option>
      <kpn-page-menu-option
        [link]="links.month"
        [active]="periodType === 'month'"
        >Month
      </kpn-page-menu-option>
      <kpn-page-menu-option [link]="links.year" [active]="periodType === 'year'"
        >Year
      </kpn-page-menu-option>
    </kpn-page-menu>
  `,
  standalone: true,
  imports: [PageMenuComponent, PageMenuOptionComponent],
})
export class StatusPageMenuComponent {
  @Input() periodType: string;
  @Input() links: StatusLinks;
}
