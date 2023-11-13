import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'kpn-monitor-group-breadcrumb',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor" i18n="@@breadcrumb.monitor">Monitor</a></li>
      <li i18n="@@breadcrumb.monitor.group">Group</li>
    </ul>
  `,
  standalone: true,
  imports: [RouterLink],
})
export class MonitorGroupBreadcrumbComponent {}
