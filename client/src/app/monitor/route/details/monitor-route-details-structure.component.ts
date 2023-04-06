import { OnInit } from '@angular/core';
import { Input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { Params } from '@angular/router';
import { MonitorRouteRelationStructureRow } from '@api/common/monitor/monitor-route-relation-structure-row';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { selectMonitorAdmin } from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-route-details-structure',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <table mat-table [dataSource]="dataSource">
      <ng-container matColumnDef="nr">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@monitor.route.relation-table.nr"
        >
          Nr
        </th>
        <td mat-cell *matCellDef="let row; let i = index">
          {{ i + 1 }}
        </td>
      </ng-container>

      <ng-container matColumnDef="name">
        <th
          mat-header-cell
          *matHeaderCellDef
          [colSpan]="2"
          i18n="@@monitor.route.relation-table.name"
        >
          Name
        </th>
        <td mat-cell *matCellDef="let row">
          <div *ngIf="row.level === 1" class="level-1">{{ row.name }}</div>
          <div *ngIf="row.level === 2" class="level-2">{{ row.name }}</div>
          <div *ngIf="row.level === 3" class="level-3">{{ row.name }}</div>
          <div *ngIf="row.level === 4" class="level-4">{{ row.name }}</div>
          <div *ngIf="row.level === 5" class="level-5">{{ row.name }}</div>
        </td>
      </ng-container>

      <ng-container matColumnDef="happy">
        <th mat-header-cell *matHeaderCellDef></th>
        <td mat-cell *matCellDef="let row">
          <mat-icon *ngIf="row.happy" svgIcon="happy"></mat-icon>
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
        <td mat-cell *matCellDef="let row">
          <a
            *ngIf="row.physical"
            [routerLink]="mapLink()"
            [queryParams]="mapQueryParams(row)"
            i18n="@@monitor.group.route-table.map-link"
            >map</a
          >
        </td>
      </ng-container>

      <ng-container matColumnDef="relation">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@monitor.route.relation-table.relation"
        >
          Relation
        </th>
        <td mat-cell *matCellDef="let row">
          <kpn-osm-link-relation
            [relationId]="row.relationId"
            [title]="row.relationId.toString()"
          >
          </kpn-osm-link-relation>
        </td>
      </ng-container>

      <ng-container matColumnDef="role">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@monitor.route.relation-table.role"
        >
          Role
        </th>
        <td mat-cell *matCellDef="let row">
          {{ row.role }}
        </td>
      </ng-container>

      <ng-container matColumnDef="distance">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@monitor.route.relation-table.distance"
        >
          Distance
        </th>
        <td mat-cell *matCellDef="let row">
          <div *ngIf="row.physical" class="distance">
            {{ row.osmDistance | distance }}
          </div>
        </td>
      </ng-container>

      <ng-container matColumnDef="survey">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@monitor.route.relation-table.survey"
        >
          Survey
        </th>
        <td mat-cell *matCellDef="let row">
          {{ row.survey | day }}
        </td>
      </ng-container>

      <ng-container matColumnDef="reference-day">
        <th
          mat-header-cell
          [colSpan]="3"
          *matHeaderCellDef
          i18n="@@monitor.group.route-table.reference"
        >
          Reference
        </th>
        <td mat-cell *matCellDef="let row">
          <ng-container *ngIf="row.physical">
            {{ row.referenceDay | day }}
          </ng-container>
        </td>
      </ng-container>

      <ng-container matColumnDef="reference-filename">
        <th mat-header-cell *matHeaderCellDef></th>
        <td mat-cell *matCellDef="let row">
          <ng-container *ngIf="row.physical">
            {{ row.referenceFilename }}
          </ng-container>
        </td>
      </ng-container>

      <ng-container matColumnDef="reference-distance">
        <th mat-header-cell *matHeaderCellDef></th>
        <td mat-cell *matCellDef="let row">
          <ng-container *ngIf="row.physical">
            {{ row.referenceDistance | distance }}
          </ng-container>
        </td>
      </ng-container>

      <ng-container matColumnDef="deviation-count">
        <th
          mat-header-cell
          *matHeaderCellDef
          [colSpan]="2"
          i18n="@@monitor.group.route-table.deviations"
        >
          Deviations
        </th>
        <td mat-cell *matCellDef="let row">
          <ng-container *ngIf="row.physical">
            {{ row.deviationCount }}
          </ng-container>
        </td>
      </ng-container>

      <ng-container matColumnDef="deviation-distance">
        <th mat-header-cell *matHeaderCellDef></th>
        <td mat-cell *matCellDef="let row">
          <ng-container *ngIf="row.physical">
            <span *ngIf="row.deviationCount > 0">
              {{ row.deviationDistance | distance }}
            </span>
            <span *ngIf="row.deviationCount === 0"> - </span>
          </ng-container>
        </td>
      </ng-container>

      <ng-container matColumnDef="osm-segment-count">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@monitor.group.route-table.osm-segment-count"
        >
          OSM segments
        </th>
        <td mat-cell *matCellDef="let row">
          <ng-container *ngIf="row.physical">
            {{ row.osmSegmentCount }}
          </ng-container>
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
        <td mat-cell *matCellDef="let row" class="kpn-action-cell">
          <button
            mat-icon-button
            [routerLink]="uploadGpx(row)"
            title="Upload"
            i18n-title="@@action.upload"
            class="kpn-action-button kpn-link"
          >
            <mat-icon svgIcon="upload" />
          </button>
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="displayedHeaders$ | async"></tr>
      <tr mat-row *matRowDef="let row; columns: displayedColumns$ | async"></tr>
    </table>
  `,
  styles: [
    `
      .distance {
        white-space: nowrap;
        text-align: right;
        width: 100%;
      }

      .level-1 {
      }

      .level-2 {
        margin-left: 1.5em;
      }

      .level-3 {
        margin-left: 3em;
      }

      .level-4 {
        margin-left: 4.5em;
      }

      .level-5 {
        margin-left: 6em;
      }
    `,
  ],
})
export class MonitorRouteDetailsStructureComponent implements OnInit {
  @Input() groupName: String;
  @Input() routeName: String;
  @Input() structureRows: MonitorRouteRelationStructureRow[];
  @Input() referenceType: String;

  private readonly admin$ = this.store.select(selectMonitorAdmin);

  readonly dataSource =
    new MatTableDataSource<MonitorRouteRelationStructureRow>();

  private readonly mainColumns = [
    'nr',
    'name',
    'happy',
    'map',
    'relation',
    'role',
    'distance',
    'survey',
  ];

  private readonly referenceColumns = [
    'reference-day',
    'reference-filename',
    'reference-distance',
  ];

  private readonly analysisColumns = [
    'deviation-count',
    'deviation-distance',
    'osm-segment-count',
  ];

  private readonly columnsWithoutHeader = [
    'happy',
    'reference-filename',
    'reference-distance',
    'deviation-distance',
  ];

  readonly displayedColumns$ = this.admin$.pipe(
    map((admin) => {
      let columns = [...this.mainColumns];
      if (this.referenceType === 'multi-gpx') {
        columns = columns.concat(this.referenceColumns);
      }
      columns = columns.concat(this.analysisColumns);
      if (admin) {
        columns = [...columns, 'actions'];
      }
      return columns;
    })
  );

  readonly displayedHeaders$ = this.displayedColumns$.pipe(
    map((columnNames) =>
      columnNames.filter((name) => !this.columnsWithoutHeader.includes(name))
    )
  );

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.dataSource.data = this.structureRows;
  }

  mapLink(): any {
    return `/monitor/groups/${this.groupName}/routes/${this.routeName}/map`;
  }

  mapQueryParams(row: MonitorRouteRelationStructureRow): Params {
    if (row.level === 1) {
      return {};
    }
    return { 'sub-relation-id': row.relationId };
  }

  uploadGpx(route: MonitorRouteRelationStructureRow): string {
    // return `/monitor/admin/groups/${this.groupName}/routes/${route.name}`;
    return '';
  }
}
