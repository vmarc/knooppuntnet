import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NzBreadCrumbItemComponent } from 'ng-zorro-antd/breadcrumb';
import { NzBreadCrumbComponent } from 'ng-zorro-antd/breadcrumb';

@Component({
  selector: 'ui-monitor-group-breadcrumb',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nz-breadcrumb>
      <nz-breadcrumb-item>
        <a routerLink="/" i18n="@@breadcrumb.home">Home</a>
      </nz-breadcrumb-item>
      <nz-breadcrumb-item>
        <a routerLink="/monitor" i18n="@@breadcrumb.monitor">Monitor</a>
      </nz-breadcrumb-item>
      <nz-breadcrumb-item>
        <span i18n="@@breadcrumb.monitor.group">Group</span>
      </nz-breadcrumb-item>
    </nz-breadcrumb>
  `,
  imports: [RouterLink, NzBreadCrumbComponent, NzBreadCrumbItemComponent],
})
export class MonitorGroupBreadcrumbComponent {}
