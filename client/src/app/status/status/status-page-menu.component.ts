import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { StatusLinks } from './status-links';

/* tslint:disable:template-i18n English only */
@Component({
  selector: 'kpn-status-page-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page-menu>
      <kpn-page-menu-option [link]="links.hour" [active]="periodType === 'hour'"
        >Hour</kpn-page-menu-option
      >
      <kpn-page-menu-option [link]="links.day" [active]="periodType === 'day'"
        >Day</kpn-page-menu-option
      >
      <kpn-page-menu-option [link]="links.week" [active]="periodType === 'week'"
        >Week</kpn-page-menu-option
      >
      <kpn-page-menu-option
        [link]="links.month"
        [active]="periodType === 'month'"
        >Month</kpn-page-menu-option
      >
      <kpn-page-menu-option [link]="links.year" [active]="periodType === 'year'"
        >Year</kpn-page-menu-option
      >
    </kpn-page-menu>
  `,
})
export class StatusPageMenuComponent {
  @Input() periodType: string;
  @Input() links: StatusLinks;
}
