import { AsyncPipe } from '@angular/common';
import { OnInit } from '@angular/core';
import { Input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableDataSource } from '@angular/material/table';
import { MatTableModule } from '@angular/material/table';
import { RouterLink } from '@angular/router';
import { MonitorGroupDetail } from '@api/common/monitor';

@Component({
  selector: 'kpn-monitor-group-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <table mat-table [dataSource]="dataSource">
      <ng-container matColumnDef="name">
        <th mat-header-cell *matHeaderCellDef i18n="@@monitor.group.table.name">
          Name
        </th>
        <td mat-cell *matCellDef="let group">
          <a [routerLink]="groupLink(group)" [state]="group">
            {{ group.name }}
          </a>
        </td>
      </ng-container>

      <ng-container matColumnDef="description">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@monitor.group.table.description"
        >
          Description
        </th>
        <td mat-cell *matCellDef="let group">
          {{ group.description }}
        </td>
      </ng-container>

      <ng-container matColumnDef="routeCount">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@monitor.group.table.routes"
        >
          Routes
        </th>
        <td mat-cell *matCellDef="let group">
          {{ group.routeCount }}
        </td>
      </ng-container>

      <ng-container matColumnDef="actions">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@monitor.group.table.actions"
        >
          Actions
        </th>
        <td mat-cell *matCellDef="let group" class="kpn-action-cell">
          <button
            mat-icon-button
            [routerLink]="updateLink(group)"
            title="Update"
            i18n-title="@@action.update"
            class="kpn-action-button kpn-link"
          >
            <mat-icon svgIcon="pencil" />
          </button>
          <button
            mat-icon-button
            [routerLink]="deleteLink(group)"
            title="delete"
            i18n-title="@@action.delete"
            class="kpn-action-button kpn-warning"
          >
            <mat-icon svgIcon="garbage" />
          </button>
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="displayedColumns(admin)"></tr>
      <tr mat-row *matRowDef="let group; columns: displayedColumns(admin)"></tr>
    </table>
  `,
  standalone: true,
  imports: [
    MatTableModule,
    RouterLink,
    MatButtonModule,
    MatIconModule,
    AsyncPipe,
  ],
})
export class MonitorGroupTableComponent implements OnInit {
  @Input() admin: boolean;
  @Input() groups: MonitorGroupDetail[];

  readonly dataSource = new MatTableDataSource<MonitorGroupDetail>();

  ngOnInit(): void {
    this.dataSource.data = this.groups;
  }

  displayedColumns(admin: boolean) {
    if (admin) {
      return ['name', 'description', 'routeCount', 'actions'];
    }
    return ['name', 'description', 'routeCount'];
  }

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
