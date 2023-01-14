import { OnInit } from '@angular/core';
import { Input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MonitorGroupDetail } from '@api/common/monitor/monitor-group-detail';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { AppState } from '../../core/core.state';
import { actionMonitorNavigateGroup } from '../store/monitor.actions';
import { selectMonitorAdmin } from '../store/monitor.selectors';

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
          <a [routerLink]="groupLink(group)" (click)="navigateGroup(group)">{{
            group.name
          }}</a>
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
            <mat-icon svgIcon="pencil"/>
          </button>
          <button
            mat-icon-button
            [routerLink]="deleteLink(group)"
            title="delete"
            i18n-title="@@action.delete"
            class="kpn-action-button kpn-warning"
          >
            <mat-icon svgIcon="garbage"/>
          </button>
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="displayedColumns$ | async"></tr>
      <tr
        mat-row
        *matRowDef="let group; columns: displayedColumns$ | async"
      ></tr>
    </table>
  `,
})
export class MonitorGroupTableComponent implements OnInit {
  @Input() groups: MonitorGroupDetail[];

  readonly dataSource = new MatTableDataSource<MonitorGroupDetail>();

  readonly displayedColumns$ = this.store.select(selectMonitorAdmin).pipe(
    map((admin) => {
      if (admin) {
        return ['name', 'description', 'routeCount', 'actions'];
      }
      return ['name', 'description', 'routeCount'];
    })
  );

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.dataSource.data = this.groups;
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

  navigateGroup(group: MonitorGroupDetail): void {
    this.store.dispatch(
      actionMonitorNavigateGroup({
        groupName: group.name,
        groupDescription: group.description,
      })
    );
    // TODO MON navigeren hier of in effect in plaats van routerLink ???
  }
}
