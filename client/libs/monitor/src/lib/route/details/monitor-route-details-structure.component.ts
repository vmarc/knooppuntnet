import { NgClass } from '@angular/common';
import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { OnInit } from '@angular/core';
import { Input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableDataSource } from '@angular/material/table';
import { MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Params } from '@angular/router';
import { RouterLink } from '@angular/router';
import { MonitorRouteRelationStructureRow } from '@api/common/monitor';
import { TimestampDayPipe } from '@app/components/shared/format';
import { DayPipe } from '@app/components/shared/format';
import { DistancePipe } from '@app/components/shared/format';
import { OsmLinkRelationComponent } from '@app/components/shared/link';
import { SymbolComponent } from '../../../../../symbol/src/lib/symbol/symbol.component';
import { MonitorRouteGapComponent } from '../monitor-route-gap.component';

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
            *ngIf="row.showMap"
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

      <ng-container matColumnDef="symbol">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@monitor.route.relation-table.symbol"
        >
          Symbol
        </th>
        <td mat-cell *matCellDef="let row" class="symbol">
          <kpn-symbol
            *ngIf="row.symbol"
            [description]="row.symbol"
            [width]="25"
            [height]="25"
          />
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
          <div class="distance">
            <span
              *ngIf="row.osmDistanceSubRelations > 0"
              class="cumulative-distance"
              matTooltip="Total length of ways in all subrelations"
            >
              {{ row.osmDistanceSubRelations | distance }}
            </span>
            <span
              *ngIf="row.osmDistanceSubRelations > 0 && row.osmDistance > 0"
            >
              /
            </span>
            <span
              *ngIf="row.osmDistance > 0"
              matTooltip="Total length of ways in this relation"
            >
              {{ row.osmDistance | distance }}
            </span>
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
            {{ row.referenceTimestamp | yyyymmdd }}
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
          <ng-container>
            {{ row.deviationCount }}
          </ng-container>
        </td>
      </ng-container>

      <ng-container matColumnDef="deviation-distance">
        <th mat-header-cell *matHeaderCellDef></th>
        <td mat-cell *matCellDef="let row">
          <ng-container>
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
          colspan="2"
          i18n="@@monitor.group.route-table.osm-segment-count"
        >
          Segments
        </th>
        <td mat-cell *matCellDef="let row">
          <ng-container>
            {{ row.osmSegmentCount }}
          </ng-container>
        </td>
      </ng-container>

      <ng-container matColumnDef="gap">
        <th style="display: none" mat-header-cell *matHeaderCellDef></th>
        <td
          mat-cell
          *matCellDef="let row"
          [ngClass]="{ 'no-route-gap': row.gaps === undefined }"
        >
          <ng-container *ngIf="row.gaps !== undefined">
            <kpn-monitor-route-gap [description]="row.gaps" />
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
              <mat-icon svgIcon="upload" />
            </button>
            <button
              mat-icon-button
              [routerLink]="deleteGpx()"
              [queryParams]="subRelationQueryParams(row)"
              [disabled]="!row.referenceFilename"
              title="Remove GPX trace for this sub-relation"
              i18n-title="@@action.gpx.delete"
              class="kpn-action-button"
              [class.kpn-disabled]="!row.referenceFilename"
              [class.kpn-warning]="row.referenceFilename"
            >
              <mat-icon svgIcon="garbage" />
            </button>
          </ng-container>
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="displayedHeaders(admin)"></tr>
      <tr mat-row *matRowDef="let row; columns: displayedColumns(admin)"></tr>
    </table>
  `,
  styles: [
    `
      .distance {
        white-space: nowrap;
        text-align: right;
        width: 100%;
      }

      .cumulative-distance {
        font-weight: 800;
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

      .symbol {
        vertical-align: middle;
      }

      .no-route-gap {
        background-color: #f8f8f8;
      }
    `,
  ],
  standalone: true,
  imports: [
    AsyncPipe,
    DayPipe,
    DistancePipe,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatTooltipModule,
    MonitorRouteGapComponent,
    NgIf,
    OsmLinkRelationComponent,
    RouterLink,
    TimestampDayPipe,
    SymbolComponent,
    NgClass,
  ],
})
export class MonitorRouteDetailsStructureComponent implements OnInit {
  @Input({ required: true }) admin: boolean;
  @Input({ required: true }) groupName: string;
  @Input({ required: true }) routeName: string;
  @Input({ required: true }) structureRows: MonitorRouteRelationStructureRow[];
  @Input({ required: true }) referenceType: string;

  readonly dataSource =
    new MatTableDataSource<MonitorRouteRelationStructureRow>();

  private readonly mainColumns = [
    'nr',
    'name',
    'happy',
    'map',
    'relation',
    'symbol',
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
    'gap',
  ];

  private readonly columnsWithoutHeader = [
    'happy',
    'reference-filename',
    'reference-distance',
    'deviation-distance',
  ];

  ngOnInit(): void {
    this.dataSource.data = this.structureRows;
  }

  displayedColumns(admin: boolean) {
    let columns = [...this.mainColumns];
    if (this.referenceType === 'multi-gpx') {
      columns = columns.concat(this.referenceColumns);
    }
    columns = columns.concat(this.analysisColumns);
    if (admin) {
      columns = [...columns, 'actions'];
    }
    return columns;
  }

  displayedHeaders(admin: boolean) {
    return this.displayedColumns(admin).filter(
      (name) => !this.columnsWithoutHeader.includes(name)
    );
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
