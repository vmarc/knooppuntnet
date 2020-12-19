import {OnInit} from '@angular/core';
import {Input} from '@angular/core';
import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {RouteGroupDetail} from '@api/common/monitor/route-group-detail';
import {Store} from '@ngrx/store';
import {map} from 'rxjs/operators';
import {AppState} from '../../core/core.state';
import {selectMonitorAdmin} from '../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-group-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <table mat-table [dataSource]="dataSource">

      <ng-container matColumnDef="name">
        <th mat-header-cell *matHeaderCellDef>Name</th>
        <td mat-cell *matCellDef="let group">
          <a [routerLink]="groupLink(group)">{{group.groupName}}</a>
        </td>
      </ng-container>

      <ng-container matColumnDef="description">
        <th mat-header-cell *matHeaderCellDef>Description</th>
        <td mat-cell *matCellDef="let group">
          {{group.groupDescription}}
        </td>
      </ng-container>

      <ng-container matColumnDef="actions">
        <th mat-header-cell *matHeaderCellDef>Actions</th>
        <td mat-cell *matCellDef="let group">
          <a [routerLink]="updateLink(group)">Update</a>
          <a [routerLink]="deleteLink(group)" class="delete">Delete</a>
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="displayedColumns$ | async"></tr>
      <tr mat-row *matRowDef="let group; columns: displayedColumns$ | async;"></tr>
    </table>
  `,
  styles: [`
    .delete {
      padding-left: 1em;
      color: red;
    }
  `]
})
export class MonitorGroupTableComponent implements OnInit {

  @Input() groups: RouteGroupDetail[];

  readonly dataSource: MatTableDataSource<RouteGroupDetail> = new MatTableDataSource<RouteGroupDetail>();

  readonly displayedColumns$ = this.store.select(selectMonitorAdmin).pipe(
    map(admin => {
      if (admin) {
        return ['name', 'description', 'actions'];
      }
      return ['name', 'description'];
    })
  );

  constructor(private store: Store<AppState>) {
  }

  ngOnInit(): void {
    this.dataSource.data = this.groups;
  }

  groupLink(group: RouteGroupDetail): string {
    return `/monitor/groups/${group.groupName}`;
  }

  updateLink(group: RouteGroupDetail): string {
    return `/monitor/admin/groups/${group.groupName}`;
  }

  deleteLink(group: RouteGroupDetail): string {
    return `/monitor/admin/groups/${group.groupName}/delete`;
  }

}
