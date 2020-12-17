import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {MonitorRouteGroup} from '@api/common/monitor/monitor-route-group';
import {Store} from '@ngrx/store';
import {map} from 'rxjs/operators';
import {PageWidthService} from '../../components/shared/page-width.service';
import {AppState} from '../../core/core.state';
import {selectMonitorAdmin} from '../../core/monitor/monitor.selectors';

@Component({
  selector: 'kpn-monitor-group-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <table mat-table [dataSource]="dataSource">

      <ng-container matColumnDef="name">
        <th mat-header-cell *matHeaderCellDef>Name</th>
        <td mat-cell *matCellDef="let group">
          <a [routerLink]="'/monitor/groups/' + group.name">{{group.name}}</a>
        </td>
      </ng-container>

      <ng-container matColumnDef="description">
        <th mat-header-cell *matHeaderCellDef>Description</th>
        <td mat-cell *matCellDef="let group">
          {{group.description}}
        </td>
      </ng-container>

      <ng-container matColumnDef="actions">
        <th mat-header-cell *matHeaderCellDef>Actions</th>
        <td mat-cell *matCellDef="let group">
          <a [routerLink]="'/monitor/admin/groups/group-1'">Update</a>
          <a [routerLink]="'/monitor/admin/groups/group-1/delete'" class="delete">Delete</a>
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
export class MonitorGroupTableComponent {

  dataSource: MatTableDataSource<MonitorRouteGroup> = new MatTableDataSource();

  readonly admin$ = this.store.select(selectMonitorAdmin);
  readonly displayedColumns$ = this.admin$.pipe(
    // TODO take page width into account? = pageWidthService.current$.pipe(map(() => this.displayedColumns()));
    map(admin => {
      if (admin) {
        return ['name', 'description', 'actions'];
      }
      return ['name', 'description'];
    })
  );

  constructor(private pageWidthService: PageWidthService,
              private store: Store<AppState>) {

    this.dataSource.data = [
      {id: '1', name: 'group-1', description: 'Group one'},
      {id: '2', name: 'group-2', description: 'Group two'},
      {id: '3', name: 'group-3', description: 'Group three'},
      {id: '4', name: 'group-4', description: 'Group four'},
      {id: '5', name: 'group-5', description: 'Group five'},
    ];
  }

}
