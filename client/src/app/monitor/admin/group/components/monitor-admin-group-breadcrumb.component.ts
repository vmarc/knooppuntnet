import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-monitor-admin-group-breadcrumb',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor">Monitor</a></li>
      <li>Group</li>
    </ul>
  `,
})
export class MonitorAdminGroupBreadcrumbComponent {}
