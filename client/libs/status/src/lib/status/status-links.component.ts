import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { StatusLinks } from './status-links';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'kpn-status-links',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <span class="kpn-comma-list">
      <a [routerLink]="links.hour">Hour</a>
      <a [routerLink]="links.day">Day</a>
      <a [routerLink]="links.week">Week</a>
      <a [routerLink]="links.month">Month</a>
      <a [routerLink]="links.year">Year</a>
    </span>
  `,
  standalone: true,
  imports: [RouterLink],
})
export class StatusLinksComponent {
  @Input() links: StatusLinks;
}
