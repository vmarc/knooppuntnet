import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MonitorRouteDetail } from '@api/common/monitor/monitor-route-detail';
import { NzDividerComponent } from 'ng-zorro-antd/divider';
import { NzIconDirective } from 'ng-zorro-antd/icon';

@Component({
  selector: 'ui-monitor-group-route-actions',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a
      [routerLink]="routeUpdateLink()"
      [state]="route()"
      title="Update"
      i18n-title="@@action.update"
      class="kpn-action-button kpn-link"
    >
      <nz-icon nzType="edit" />
    </a>
    <nz-divider nzType="vertical" />
    <a
      [routerLink]="routeDeleteLink()"
      [state]="route()"
      title="delete"
      i18n-title="@@action.delete"
      class="kpn-action-button kpn-warning"
    >
      <nz-icon nzType="delete" />
    </a>
  `,
  imports: [NzIconDirective, RouterLink, NzDividerComponent],
})
export class MonitorGroupRouteActionsComponent {
  readonly groupName = input.required<string>();
  readonly route = input.required<MonitorRouteDetail>();

  routeUpdateLink(): string {
    return this.buildUrl('');
  }

  routeDeleteLink(): string {
    return this.buildUrl('delete');
  }

  private buildUrl(action: string, isAdmin = false): string {
    const suffix = action.length > 0 ? `/${action}` : '';
    return `/monitor/admin/groups/${this.groupName()}/routes/${this.route().name}${suffix}`;
  }
}
