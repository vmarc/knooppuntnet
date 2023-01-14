import { OnInit } from '@angular/core';
import { Input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MonitorRouteDetail } from '@api/common/monitor/monitor-route-detail';
import { MonitorRouteRelation } from '@api/common/monitor/monitor-route-relation';
import { AppState } from '@app/core/core.state';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { selectMonitorAdmin } from '../../store/monitor.selectors';
import { MonitorRouteRelationRow } from './monitor-route-relation-row';

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
        <td mat-cell *matCellDef="let route">
          <mat-icon *ngIf="route.relation.happy" svgIcon="happy"></mat-icon>
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
            [relationId]="row.relation.relationId"
            [title]="row.relation.relationId.toString()"
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
          {{ row.relation.role }}
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
          <div class="distance">{{ row.relation.osmDistance | distance }}</div>
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
          {{ row.relation.survey | day }}
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
        <td mat-cell *matCellDef="let route">
          {{ route.relation.referenceDay | day }}
        </td>
      </ng-container>

      <ng-container matColumnDef="reference-filename">
        <th mat-header-cell *matHeaderCellDef></th>
        <td mat-cell *matCellDef="let route">
          <span>
            {{ route.relation.referenceFilename }}
          </span>
        </td>
      </ng-container>

      <ng-container matColumnDef="reference-distance">
        <th mat-header-cell *matHeaderCellDef></th>
        <td mat-cell *matCellDef="let route">
          <span>
            {{ route.relation.referenceDistance | distance }}
          </span>
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
        <td mat-cell *matCellDef="let route">
          <span>
            {{ route.relation.deviationCount }}
          </span>
        </td>
      </ng-container>

      <ng-container matColumnDef="deviation-distance">
        <th mat-header-cell *matHeaderCellDef></th>
        <td mat-cell *matCellDef="let route">
          <span *ngIf="route.deviationCount > 0">
            {{ route.deviationDistance | distance }}
          </span>
          <span *ngIf="route.relation.deviationCount === 0"> - </span>
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
        <td mat-cell *matCellDef="let route">
          <span>
            {{ route.relation.osmSegmentCount }}
          </span>
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
        <td mat-cell *matCellDef="let route" class="kpn-action-cell">
          <button
            mat-icon-button
            [routerLink]="uploadGpx(route)"
            title="Upload"
            i18n-title="@@action.upload"
            class="kpn-action-button kpn-link"
          >
            <mat-icon svgIcon="upload"/>
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
  @Input() relation: MonitorRouteRelation;
  @Input() referenceType: String;

  readonly admin$ = this.store.select(selectMonitorAdmin);

  readonly dataSource = new MatTableDataSource<MonitorRouteRelationRow>();

  readonly mainColumns = [
    'nr',
    'name',
    'happy',
    'map',
    'relation',
    'role',
    'distance',
    'survey',
  ];

  readonly referenceColumns = [
    'reference-day',
    'reference-filename',
    'reference-distance',
  ];

  readonly analysisColumns = [
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

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.dataSource.data = this.flattenRelationTree();
  }

  private flattenRelationTree(): MonitorRouteRelationRow[] {
    const rowsLevel2 = this.relation.relations.flatMap((relationLevel2) => {
      const rowsLevel3 = relationLevel2.relations.flatMap((relationLevel3) => {
        const rowsLevel4 = relationLevel3.relations.flatMap(
          (relationLevel4) => {
            const rowsLevel5 = relationLevel4.relations.map(
              (relationLevel5) => {
                return this.row(5, relationLevel5);
              }
            );
            return [this.row(4, relationLevel4), ...rowsLevel5];
          }
        );
        return [this.row(3, relationLevel3), ...rowsLevel4];
      });
      return [this.row(2, relationLevel2), ...rowsLevel3];
    });
    return [this.row(1, this.relation), ...rowsLevel2];
  }

  private row(
    level: number,
    relation: MonitorRouteRelation
  ): MonitorRouteRelationRow {
    return {
      level,
      name: relation.name,
      relation,
    };
  }

  routeMapLink(route: MonitorRouteDetail): string {
    // return `/monitor/groups/${this.groupName}/routes/${route.name}/map`;
    return '';
  }

  uploadGpx(route: MonitorRouteDetail): string {
    // return `/monitor/admin/groups/${this.groupName}/routes/${route.name}`;
    return '';
  }
}
