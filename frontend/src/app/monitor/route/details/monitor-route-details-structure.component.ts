import { NgClass } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
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
import { SymbolComponent } from '@app/symbol';
import { MonitorRouteGapComponent } from '../monitor-route-gap.component';

@Component({
  selector: 'kpn-monitor-route-details-structure',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <table mat-table [dataSource]="dataSource">
      <ng-container matColumnDef="nr">
        <th mat-header-cell *matHeaderCellDef i18n="@@monitor.route.relation-table.nr">Nr</th>
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
          @switch (row.level) {
            @case (1) {
              <div class="level-1">{{ row.name }}</div>
            }
            @case (2) {
              <div class="level-2">{{ row.name }}</div>
            }
            @case (3) {
              <div class="level-3">{{ row.name }}</div>
            }
            @case (4) {
              <div class="level-4">{{ row.name }}</div>
            }
            @case (5) {
              <div class="level-5">{{ row.name }}</div>
            }
          }
        </td>
      </ng-container>

      <ng-container matColumnDef="happy">
        <th mat-header-cell *matHeaderCellDef></th>
        <td mat-cell *matCellDef="let row">
          @if (row.happy) {
            <mat-icon svgIcon="happy"></mat-icon>
          }
        </td>
      </ng-container>

      <ng-container matColumnDef="map">
        <th mat-header-cell *matHeaderCellDef i18n="@@monitor.group.route-table.map">Map</th>
        <td mat-cell *matCellDef="let row">
          @if (row.showMap) {
            <a
              [routerLink]="mapLink()"
              [queryParams]="subRelationQueryParams(row)"
              i18n="@@monitor.group.route-table.map-link"
            >
              map
            </a>
          }
        </td>
      </ng-container>

      <ng-container matColumnDef="relation">
        <th mat-header-cell *matHeaderCellDef i18n="@@monitor.route.relation-table.relation">
          Relation
        </th>
        <td mat-cell *matCellDef="let row">
          <kpn-osm-link-relation [relationId]="row.relationId" [title]="row.relationId.toString()">
          </kpn-osm-link-relation>
        </td>
      </ng-container>

      <ng-container matColumnDef="symbol">
        <th mat-header-cell *matHeaderCellDef i18n="@@monitor.route.relation-table.symbol">
          Symbol
        </th>
        <td mat-cell *matCellDef="let row" class="symbol">
          @if (row.symbol) {
            <kpn-symbol [description]="row.symbol" [width]="25" [height]="25" />
          }
        </td>
      </ng-container>

      <ng-container matColumnDef="role">
        <th mat-header-cell *matHeaderCellDef i18n="@@monitor.route.relation-table.role">Role</th>
        <td mat-cell *matCellDef="let row">
          {{ row.role }}
        </td>
      </ng-container>

      <ng-container matColumnDef="distance">
        <th mat-header-cell *matHeaderCellDef i18n="@@monitor.route.relation-table.distance">
          Distance
        </th>
        <td mat-cell *matCellDef="let row">
          <div class="distance">
            @if (row.osmDistanceSubRelations > 0) {
              <span
                class="cumulative-distance"
                matTooltip="Total length of ways in all subrelations"
              >
                {{ row.osmDistanceSubRelations | distance }}
              </span>
            }

            @if (row.osmDistanceSubRelations > 0 && row.osmDistance > 0) {
              <span> / </span>
            }

            @if (row.osmDistance > 0) {
              <span matTooltip="Total length of ways in this relation">
                {{ row.osmDistance | distance }}
              </span>
            }
          </div>
        </td>
      </ng-container>

      <ng-container matColumnDef="survey">
        <th mat-header-cell *matHeaderCellDef i18n="@@monitor.route.relation-table.survey">
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
          @if (row.physical) {
            {{ row.referenceTimestamp | yyyymmdd }}
          }
        </td>
      </ng-container>

      <ng-container matColumnDef="reference-filename">
        <th mat-header-cell *matHeaderCellDef></th>
        <td mat-cell *matCellDef="let row">
          @if (row.physical) {
            {{ row.referenceFilename }}
          }
        </td>
      </ng-container>

      <ng-container matColumnDef="reference-distance">
        <th mat-header-cell *matHeaderCellDef></th>
        <td mat-cell *matCellDef="let row">
          @if (row.physical) {
            {{ row.referenceDistance | distance }}
          }
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
          {{ row.deviationCount }}
        </td>
      </ng-container>

      <ng-container matColumnDef="deviation-distance">
        <th mat-header-cell *matHeaderCellDef></th>
        <td mat-cell *matCellDef="let row">
          @if (row.deviationCount > 0) {
            {{ row.deviationDistance | distance }}
          } @else {
            -
          }
        </td>
      </ng-container>

      <ng-container matColumnDef="segments">
        <th mat-header-cell *matHeaderCellDef i18n="@@monitor.group.route-table.segments">
          Segments
        </th>
        <td mat-cell *matCellDef="let row" [ngClass]="{ 'no-route-gap': row.gaps === undefined }">
          @if (row.gaps !== undefined) {
            <kpn-monitor-route-gap
              [description]="row.gaps"
              [osmSegmentCount]="row.osmSegmentCount"
            />
          }
        </td>
      </ng-container>

      <ng-container matColumnDef="actions">
        <th mat-header-cell *matHeaderCellDef i18n="@@monitor.group.route-table.actions">
          Actions
        </th>
        <td mat-cell *matCellDef="let row" class="kpn-action-cell">
          @if (row.physical) {
            <button
              mat-icon-button
              [routerLink]="uploadGpx()"
              [queryParams]="subRelationQueryParams(row)"
              [disabled]="!canUpload()"
              title="Upload GPX trace for this sub-relation"
              i18n-title="@@action.gpx.upload"
              class="kpn-action-button"
              [class.kpn-disabled]="!canUpload()"
            >
              <mat-icon svgIcon="upload" />
            </button>
            <button
              mat-icon-button
              [routerLink]="deleteGpx()"
              [queryParams]="subRelationQueryParams(row)"
              [disabled]="!canDelete(row)"
              title="Remove GPX trace for this sub-relation"
              i18n-title="@@action.gpx.delete"
              class="kpn-action-button"
              [class.kpn-disabled]="!row.referenceFilename"
              [class.kpn-warning]="row.referenceFilename"
            >
              <mat-icon svgIcon="garbage" />
            </button>
          }
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="displayedHeaders(admin())"></tr>
      <tr mat-row *matRowDef="let row; columns: displayedColumns(admin())"></tr>
    </table>
  `,
  styles: `
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
    OsmLinkRelationComponent,
    RouterLink,
    TimestampDayPipe,
    SymbolComponent,
    NgClass,
  ],
})
export class MonitorRouteDetailsStructureComponent implements OnInit {
  admin = input.required<boolean>();
  groupName = input.required<string>();
  routeName = input.required<string>();
  structureRows = input.required<MonitorRouteRelationStructureRow[]>();
  referenceType = input.required<string>();

  readonly dataSource = new MatTableDataSource<MonitorRouteRelationStructureRow>();

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

  private readonly referenceColumns = ['reference-day', 'reference-filename', 'reference-distance'];

  private readonly analysisColumns = ['deviation-count', 'deviation-distance', 'segments'];

  private readonly columnsWithoutHeader = [
    'happy',
    'reference-filename',
    'reference-distance',
    'deviation-distance',
  ];

  ngOnInit(): void {
    this.dataSource.data = this.structureRows();
  }

  displayedColumns(admin: boolean) {
    let columns = [...this.mainColumns];
    if (this.referenceType() === 'multi-gpx') {
      columns = columns.concat(this.referenceColumns);
    }
    columns = columns.concat(this.analysisColumns);
    if (admin) {
      columns = [...columns, 'actions'];
    }
    return columns;
  }

  displayedHeaders(admin: boolean) {
    return this.displayedColumns(admin).filter((name) => !this.columnsWithoutHeader.includes(name));
  }

  mapLink(): string {
    return `/monitor/groups/${this.groupName()}/routes/${this.routeName()}/map`;
  }

  subRelationQueryParams(row: MonitorRouteRelationStructureRow): Params {
    if (row.level === 1) {
      return {};
    }
    return { 'sub-relation-index': row.subRelationIndex };
  }

  uploadGpx(): string {
    return `/monitor/groups/${this.groupName()}/routes/${this.routeName()}/gpx`;
  }

  deleteGpx(): string {
    return `/monitor/groups/${this.groupName()}/routes/${this.routeName()}/gpx/delete`;
  }

  canUpload(): boolean {
    return this.referenceType() === 'multi-gpx';
  }

  canDelete(row: MonitorRouteRelationStructureRow): boolean {
    return this.referenceType() === 'multi-gpx' && !!row.referenceFilename;
  }
}
