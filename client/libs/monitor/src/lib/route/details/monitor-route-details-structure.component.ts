import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { computed } from '@angular/core';
import { OnInit } from '@angular/core';
import { Input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableDataSource } from '@angular/material/table';
import { MatTableModule } from '@angular/material/table';
import { Params, RouterLink } from '@angular/router';
import { MonitorRouteRelationStructureRow } from '@api/common/monitor';
import { DayPipe } from '@app/components/shared/format';
import { DistancePipe } from '@app/components/shared/format';
import { OsmLinkRelationComponent } from '@app/components/shared/link';
import { Store } from '@ngrx/store';
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
            [queryParams]="subRelationQueryParams(row)"
            i18n="@@monitor.group.route-table.map-link"
          >
            map
          </a>
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
          <ng-container *ngIf="row.physical">
            <button
              mat-icon-button
              [routerLink]="uploadGpx()"
              [queryParams]="subRelationQueryParams(row)"
              title="Upload GPX trace for this sub-relation"
              i18n-title="@@action.gpx.upload"
              class="kpn-action-button kpn-link"
            >
              <mat-icon svgIcon="pencil" />
            </button>
            <button
              mat-icon-button
              [routerLink]="deleteGpx()"
              [queryParams]="subRelationQueryParams(row)"
              title="Remove GPX trace for this sub-relation"
              i18n-title="@@action.gpx.delete"
              class="kpn-action-button  kpn-warning"
            >
              <mat-icon svgIcon="garbage" />
            </button>
          </ng-container>
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="displayedHeaders()"></tr>
      <tr mat-row *matRowDef="let row; columns: displayedColumns()"></tr>
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
  standalone: true,
  imports: [
    MatTableModule,
    NgIf,
    MatIconModule,
    RouterLink,
    OsmLinkRelationComponent,
    MatButtonModule,
    AsyncPipe,
    DayPipe,
    DistancePipe,
  ],
})
export class MonitorRouteDetailsStructureComponent implements OnInit {
  @Input() groupName: string;
  @Input() routeName: string;
  @Input() structureRows: MonitorRouteRelationStructureRow[];
  @Input() referenceType: string;

  private readonly admin = this.store.selectSignal(selectMonitorAdmin);

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

  readonly displayedColumns = computed(() => {
    let columns = [...this.mainColumns];
    if (this.referenceType === 'multi-gpx') {
      columns = columns.concat(this.referenceColumns);
    }
    columns = columns.concat(this.analysisColumns);
    if (this.admin()) {
      columns = [...columns, 'actions'];
    }
    return columns;
  });

  readonly displayedHeaders = computed(() =>
    this.displayedColumns().filter(
      (name) => !this.columnsWithoutHeader.includes(name)
    )
  );

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.dataSource.data = this.structureRows;
  }

  mapLink(): string {
    return `/monitor/groups/${this.groupName}/routes/${this.routeName}/map`;
  }

  subRelationQueryParams(row: MonitorRouteRelationStructureRow): Params {
    if (row.level === 1) {
      return {};
    }
    return { 'sub-relation-id': row.relationId };
  }

  uploadGpx(): string {
    return `/monitor/groups/${this.groupName}/routes/${this.routeName}/gpx`;
  }

  deleteGpx(): string {
    return `/monitor/groups/${this.groupName}/routes/${this.routeName}/gpx/delete`;
  }
}
