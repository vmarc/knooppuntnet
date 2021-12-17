import { OnInit } from '@angular/core';
import { Input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MonitorRouteDetail } from '@api/common/monitor/monitor-route-detail';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { PageWidthService } from '../../../components/shared/page-width.service';
import { AppState } from '../../../core/core.state';
import { selectMonitorAdmin } from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-group-route-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <table mat-table [dataSource]="dataSource">
      <ng-container matColumnDef="id">
        <th mat-header-cell *matHeaderCellDef class="id">Id</th>
        <td mat-cell *matCellDef="let route">
          <a [routerLink]="routeLink(route)">{{ route.monitorRouteId }}</a>
        </td>
      </ng-container>

      <ng-container matColumnDef="map">
        <th mat-header-cell *matHeaderCellDef>Map</th>
        <td mat-cell *matCellDef="let route">
          <a [routerLink]="routeMapLink(route)">map</a>
        </td>
      </ng-container>

      <ng-container matColumnDef="routeId">
        <th mat-header-cell *matHeaderCellDef>Relation</th>
        <td mat-cell *matCellDef="let route">
          <kpn-osm-link-relation
            *ngIf="route.routeId > 1"
            [relationId]="route.routeId"
            [title]="route.routeId.toString()"
          >
          </kpn-osm-link-relation>
        </td>
      </ng-container>

      <ng-container matColumnDef="name">
        <th mat-header-cell *matHeaderCellDef>Name</th>
        <td mat-cell *matCellDef="let route">
          {{ route.name }}
        </td>
      </ng-container>

      <ng-container matColumnDef="description">
        <th mat-header-cell *matHeaderCellDef>Description</th>
        <td mat-cell *matCellDef="let route">
          {{ route.description }}
        </td>
      </ng-container>

      <ng-container matColumnDef="distance">
        <th mat-header-cell *matHeaderCellDef>Distance</th>
        <td mat-cell *matCellDef="let route">
          {{ route.distance }}
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
          <a [routerLink]="routeUpdateLink(route)">Update</a>
          <a [routerLink]="routeDeleteLink(route)" class="delete">Delete</a>
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="displayedColumns$ | async"></tr>
      <tr
        mat-row
        *matRowDef="let group; columns: displayedColumns$ | async"
      ></tr>
    </table>
  `,
  styles: [
    `
      .id {
        width: 8em;
      }

      .delete {
        padding-left: 1em;
        color: red;
      }
    `,
  ],
})
export class MonitorGroupRouteTableComponent implements OnInit {
  @Input() routes: MonitorRouteDetail[];
  @Input() groupName: string;

  readonly admin$ = this.store.select(selectMonitorAdmin);

  readonly dataSource = new MatTableDataSource<MonitorRouteDetail>();

  readonly displayedColumns$ = this.admin$.pipe(
    map((admin) => {
      if (admin) {
        return [
          'id',
          'map',
          'routeId',
          'name',
          // 'description',
          // 'distance',
          // 'status',
          // 'actions',
        ];
      }
      return [
        'id',
        'map',
        'routeId',
        'name',
        // 'description',
        // 'distance',
        // 'status',
      ];
    })
  );

  constructor(
    private pageWidthService: PageWidthService,
    private store: Store<AppState>
  ) {}

  ngOnInit(): void {
    this.dataSource.data = this.routes;
  }

  routeLink(route: MonitorRouteDetail): string {
    return `/monitor/groups/${this.groupName}/routes/${route.monitorRouteId}`;
  }

  routeMapLink(route: MonitorRouteDetail): string {
    return `/monitor/groups/${this.groupName}/routes/${route.monitorRouteId}/map`;
  }

  routeUpdateLink(route: MonitorRouteDetail): string {
    return `/monitor/admin/groups/${this.groupName}/routes/${route.monitorRouteId}`;
  }

  routeDeleteLink(route: MonitorRouteDetail): string {
    return `/monitor/admin/groups/${this.groupName}/routes/${route.monitorRouteId}/delete`;
  }
}
