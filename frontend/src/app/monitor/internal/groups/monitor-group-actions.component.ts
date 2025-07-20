import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MonitorGroupsPageGroup } from '@api/common/monitor/monitor-groups-page-group';
import { NzDividerComponent } from 'ng-zorro-antd/divider';
import { NzIconDirective } from 'ng-zorro-antd/icon';

@Component({
  selector: 'ui-monitor-group-actions',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a
      [routerLink]="updateLink()"
      title="Update"
      i18n-title="@@action.update"
      class="kpn-action-button kpn-link"
    >
      <nz-icon nzType="edit" />
    </a>
    <nz-divider nzType="vertical" />
    <a
      [routerLink]="deleteLink()"
      title="delete"
      i18n-title="@@action.delete"
      class="kpn-action-button kpn-warning"
    >
      <nz-icon nzType="delete" />
    </a>
  `,
  imports: [RouterLink, NzIconDirective, NzDividerComponent],
})
export class MonitorGroupActionsComponent {
  readonly group = input.required<MonitorGroupsPageGroup>();

  updateLink(): string {
    return `/monitor/admin/groups/${this.group().name}`;
  }

  deleteLink(): string {
    return `/monitor/admin/groups/${this.group().name}/delete`;
  }
}
