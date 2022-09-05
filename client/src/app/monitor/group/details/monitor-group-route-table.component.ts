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
      <ng-container matColumnDef="nr">
        <th
          mat-header-cell
          *matHeaderCellDef
          class="nr"
          i18n="@@monitor.group.route-table.nr"
        >
          Nr
        </th>
        <td mat-cell *matCellDef="let route">
          {{ route.rowIndex + 1 }}
        </td>
      </ng-container>

      <ng-container matColumnDef="name">
        <th
          mat-header-cell
          *matHeaderCellDef
          class="id"
          [colSpan]="2"
          i18n="@@monitor.group.route-table.name"
        >
          Name
        </th>
        <td mat-cell *matCellDef="let route">
          <a [routerLink]="routeLink(route)">{{ route.name }}</a>
        </td>
      </ng-container>

      <ng-container matColumnDef="happy">
        <th mat-header-cell *matHeaderCellDef></th>
        <td mat-cell *matCellDef="let route">
          <mat-icon *ngIf="route.happy" svgIcon="happy"></mat-icon>
        </td>
      </ng-container>

      <ng-container matColumnDef="map">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@monitor.group.route-table.map"
        >
          Map
        </th>
        <td mat-cell *matCellDef="let route">
          <a
            [routerLink]="routeMapLink(route)"
            i18n="@@monitor.group.route-table.map-link"
            >map</a
          >
        </td>
      </ng-container>

      <ng-container matColumnDef="relationId">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@monitor.group.route-table.relation"
        >
          Relation
        </th>
        <td mat-cell *matCellDef="let route">
          <kpn-osm-link-relation
            *ngIf="!!route.relationId"
            [relationId]="route.relationId"
            [title]="route.relationId.toString()"
          >
          </kpn-osm-link-relation>
        </td>
      </ng-container>

      <ng-container matColumnDef="description">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@monitor.group.route-table.description"
        >
          Description
        </th>
        <td mat-cell *matCellDef="let route">
          {{ route.description }}
        </td>
      </ng-container>

      <ng-container matColumnDef="distance">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@monitor.group.route-table.distance"
        >
          Distance
        </th>
        <td mat-cell *matCellDef="let route">
          {{ route.distance }}
        </td>
      </ng-container>

      <ng-container matColumnDef="status">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@monitor.group.route-table.status"
        >
          Status
        </th>
        <td mat-cell *matCellDef="let route">
          <mat-icon *ngIf="route.happy" svgIcon="happy"></mat-icon>
        </td>
      </ng-container>

      <ng-container matColumnDef="actions">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@monitor.group.route-table.actions"
        >
          Actions
        </th>
        <td mat-cell *matCellDef="let route">
          <a [routerLink]="routeUpdateLink(route)" i18n="@@action.update"
            >Update</a
          >
          <a
            [routerLink]="routeDeleteLink(route)"
            class="delete"
            i18n="@@action.delete"
            >Delete</a
          >
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="displayedHeaders$ | async"></tr>
      <tr
        mat-row
        *matRowDef="let group; columns: displayedColumns$ | async"
      ></tr>
    </table>
  `,
  styles: [
    `
      .id {
        width: 12em;
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
          'nr',
          'name',
          'happy',
          'map',
          'relationId',
          'description',
          'distance',
          'status',
          'actions',
        ];
      }
      return [
        'nr',
        'name',
        'happy',
        'map',
        'relationId',
        'description',
        'distance',
        'status',
      ];
    })
  );

  readonly displayedHeaders$ = this.displayedColumns$.pipe(
    map((columnNames) => columnNames.filter((name) => name !== 'happy'))
  );

  constructor(
    private pageWidthService: PageWidthService,
    private store: Store<AppState>
  ) {}

  ngOnInit(): void {
    this.dataSource.data = this.routes;
  }

  routeLink(route: MonitorRouteDetail): string {
    return `/monitor/groups/${this.groupName}/routes/${route.name}`;
  }

  routeMapLink(route: MonitorRouteDetail): string {
    return `/monitor/groups/${this.groupName}/routes/${route.name}/map`;
  }

  routeUpdateLink(route: MonitorRouteDetail): string {
    return `/monitor/admin/groups/${this.groupName}/routes/${route.name}`;
  }

  routeDeleteLink(route: MonitorRouteDetail): string {
    return `/monitor/admin/groups/${this.groupName}/routes/${route.name}/delete`;
  }
}
