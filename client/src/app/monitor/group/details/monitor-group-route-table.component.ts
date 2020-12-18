import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {Store} from '@ngrx/store';
import {of} from 'rxjs';
import {map} from 'rxjs/operators';
import {PageWidthService} from '../../../components/shared/page-width.service';
import {AppState} from '../../../core/core.state';
import {selectMonitorAdmin} from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-group-route-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <table mat-table [dataSource]="dataSource">

      <ng-container matColumnDef="id">
        <th mat-header-cell *matHeaderCellDef>Id</th>
        <td mat-cell *matCellDef="let route">
          <a [routerLink]="routeLink$ | async">{{route.id}}</a>
        </td>
      </ng-container>

      <ng-container matColumnDef="name">
        <th mat-header-cell *matHeaderCellDef>Name</th>
        <td mat-cell *matCellDef="let route">
          {{route.name}}
        </td>
      </ng-container>

      <ng-container matColumnDef="description">
        <th mat-header-cell *matHeaderCellDef>Description</th>
        <td mat-cell *matCellDef="let route">
          {{route.description}}
        </td>
      </ng-container>

      <ng-container matColumnDef="distance">
        <th mat-header-cell *matHeaderCellDef>Distance</th>
        <td mat-cell *matCellDef="let route">
          {{route.distance}}
        </td>
      </ng-container>

      <ng-container matColumnDef="status">
        <th mat-header-cell *matHeaderCellDef>Status</th>
        <td mat-cell *matCellDef="let route">
          <mat-icon *ngIf="route.happy" svgIcon="happy"></mat-icon>
        </td>
      </ng-container>

      <ng-container matColumnDef="actions">
        <th mat-header-cell *matHeaderCellDef>Actions</th>
        <td mat-cell *matCellDef="let route">
          <a [routerLink]="routeUpdateLink$ | async">Update</a>
          <a [routerLink]="routeDeleteLink$ | async" class="delete">Delete</a>
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
export class MonitorGroupRouteTableComponent {

  groupName = 'group-1';
  routeId = 1111;
  readonly routeLink$ = of('/monitor/groups/' + this.groupName + '/routes/' + this.routeId);
  readonly routeUpdateLink$ = of('/monitor/admin/groups/' + this.groupName + '/routes/' + this.routeId);
  readonly routeDeleteLink$ = of('/monitor/admin/groups/' + this.groupName + '/routes/' + this.routeId + '/delete');

  // dataSource: MatTableDataSource<MonitorRouteGroup> = new MatTableDataSource();
  dataSource: MatTableDataSource<any> = new MatTableDataSource();

  readonly admin$ = this.store.select(selectMonitorAdmin);
  readonly displayedColumns$ = this.admin$.pipe(
    // TODO take page width into account? = pageWidthService.current$.pipe(map(() => this.displayedColumns()));
    map(admin => {
      if (admin) {
        return ['id', 'name', 'description', 'distance', 'status', 'actions'];
      }
      return ['id', 'name', 'description', 'distance', 'status'];
    })
  );

  constructor(private pageWidthService: PageWidthService,
              private store: Store<AppState>) {

    this.dataSource.data = [
      {id: '2929186', name: 'GR14', description: 'Route one', distance: 100, happy: true},
      {id: '8613893', name: 'GR15', description: 'Route two', distance: 100, happy: true},
      {id: '197843', name: 'GR16', description: 'Route three', distance: 100, happy: true},
      {id: '3121667', name: 'GR05-vl', description: 'Route four', distance: 100, happy: false},
      {id: '3121668', name: 'GR05-wa', description: 'Route five', distance: 100, happy: false},
      {id: '1111', name: 'test-1', description: 'Test route one', distance: 100, happy: false},
    ];
  }

}
