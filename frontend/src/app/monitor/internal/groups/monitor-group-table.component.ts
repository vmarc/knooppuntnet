import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MonitorGroupsPageGroup } from '@api/common/monitor/monitor-groups-page-group';
import { MonitorGroupDetail } from '@api/common/monitor/monitor-group-detail';
import { NzDividerComponent } from 'ng-zorro-antd/divider';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NzTableModule } from 'ng-zorro-antd/table';

@Component({
  selector: 'kpn-monitor-group-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nz-table nzBordered [nzFrontPagination]="false" #groupTable [nzData]="groups()" nzSize="small">
      <thead>
        <tr>
          <th i18n="@@monitor.group.table.name">Name</th>
          <th i18n="@@monitor.group.table.description">Description</th>
          <th i18n="@@monitor.group.table.routes">Routes</th>
          @if (admin()) {
            <th i18n="@@monitor.group.table.actions">Actions</th>
          }
        </tr>
      </thead>
      <tbody>
        @for (group of groupTable.data; track group) {
          <tr>
            <td>
              <a [routerLink]="groupLink(group)" [state]="group">
                {{ group.name }}
              </a>
            </td>
            <td>
              {{ group.description }}
            </td>
            <td>
              {{ group.routeCount }}
            </td>
            @if (admin()) {
              <td>
                <a
                  [routerLink]="updateLink(group)"
                  title="Update"
                  i18n-title="@@action.update"
                  class="kpn-action-button kpn-link"
                >
                  <nz-icon [nzType]="'edit'" />
                </a>
                <nz-divider [nzType]="'vertical'" />
                <a
                  [routerLink]="deleteLink(group)"
                  title="delete"
                  i18n-title="@@action.delete"
                  class="kpn-action-button kpn-warning"
                >
                  <nz-icon [nzType]="'delete'" />
                </a>
              </td>
            }
          </tr>
        }
      </tbody>
    </nz-table>
  `,
  imports: [RouterLink, NzTableModule, NzIconDirective, NzDividerComponent],
})
export class MonitorGroupTableComponent {
  admin = input.required<boolean>();
  groups = input.required<MonitorGroupsPageGroup[]>();

  groupLink(group: MonitorGroupDetail): string {
    return `/monitor/groups/${group.name}`;
  }

  updateLink(group: MonitorGroupDetail): string {
    return `/monitor/admin/groups/${group.name}`;
  }

  deleteLink(group: MonitorGroupDetail): string {
    return `/monitor/admin/groups/${group.name}/delete`;
  }
}
