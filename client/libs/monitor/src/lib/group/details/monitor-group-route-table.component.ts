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
import { RouterLink } from '@angular/router';
import { MonitorRouteDetail } from '@api/common/monitor';
import { PageWidthService } from '@app/components/shared';
import { DayPipe } from '@app/components/shared/format';
import { DistancePipe } from '@app/components/shared/format';
import { OsmLinkRelationComponent } from '@app/components/shared/link';
import { Store } from '@ngrx/store';
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
          <mat-icon *ngIf="route.happy" svgIcon="happy" />
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
          />
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

      <ng-container matColumnDef="reference-type">
        <th
          mat-header-cell
          [colSpan]="3"
          *matHeaderCellDef
          i18n="@@monitor.group.route-table.reference"
        >
          Reference
        </th>
        <td mat-cell *matCellDef="let route">
          {{ route.referenceType }}
        </td>
      </ng-container>

      <ng-container matColumnDef="reference-day">
        <th mat-header-cell *matHeaderCellDef></th>
        <td mat-cell *matCellDef="let route">
          {{ route.referenceDay | day }}
        </td>
      </ng-container>

      <ng-container matColumnDef="reference-distance">
        <th mat-header-cell *matHeaderCellDef></th>
        <td mat-cell *matCellDef="let route">
          <span *ngIf="route.referenceType">
            {{ route.referenceDistance | distance }}
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
          <span *ngIf="route.referenceType && route.relationId">
            {{ route.deviationCount }}
          </span>
        </td>
      </ng-container>

      <ng-container matColumnDef="deviation-distance">
        <th mat-header-cell *matHeaderCellDef></th>
        <td mat-cell *matCellDef="let route">
          <span *ngIf="route.referenceType && route.deviationCount > 0">
            {{ route.deviationDistance | distance }}
          </span>
          <span
            *ngIf="
              route.referenceType &&
              route.relationId &&
              route.deviationCount === 0
            "
          >
            -
          </span>
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
          <span *ngIf="!!route.relationId">
            {{ route.osmSegmentCount }}
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
            [routerLink]="routeUpdateLink(route)"
            title="Update"
            i18n-title="@@action.update"
            class="kpn-action-button kpn-link"
          >
            <mat-icon svgIcon="pencil" />
          </button>
          <button
            mat-icon-button
            [routerLink]="routeDeleteLink(route)"
            title="delete"
            i18n-title="@@action.delete"
            class="kpn-action-button kpn-warning"
          >
            <mat-icon svgIcon="garbage" />
          </button>
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="displayedHeaders()"></tr>
      <tr mat-row *matRowDef="let group; columns: displayedColumns()"></tr>
    </table>
  `,
  styles: [
    `
      .id {
        width: 12em;
      }

      .mat-column-name {
        white-space: nowrap;
      }

      .mat-column-description {
        min-width: 12em;
      }

      .mat-column-reference-day {
        white-space: nowrap;
      }

      .mat-column-reference-distance {
        text-align: right !important;
        white-space: nowrap;
      }

      .mat-column-deviation-count {
        text-align: right !important;
        white-space: nowrap;
      }

      .mat-column-deviation-distance {
        text-align: right !important;
        white-space: nowrap;
      }

      .mat-column-osm-segment-count {
        text-align: right !important;
        white-space: nowrap;
      }
    `,
  ],
  standalone: true,
  imports: [
    MatTableModule,
    RouterLink,
    NgIf,
    MatIconModule,
    OsmLinkRelationComponent,
    MatButtonModule,
    AsyncPipe,
    DayPipe,
    DistancePipe,
  ],
})
export class MonitorGroupRouteTableComponent implements OnInit {
  @Input() routes: MonitorRouteDetail[];
  @Input() groupName: string;

  readonly admin = this.store.selectSignal(selectMonitorAdmin);

  readonly dataSource = new MatTableDataSource<MonitorRouteDetail>();

  private readonly columns = [
    'nr',
    'name',
    'happy',
    'map',
    'relationId',
    'description',
    'reference-type',
    'reference-day',
    'reference-distance',
    'deviation-count',
    'deviation-distance',
    'osm-segment-count',
  ];

  private readonly columnsWithoutHeader = [
    'happy',
    'reference-day',
    'reference-distance',
    'deviation-distance',
  ];

  readonly displayedColumns = computed(() => {
    if (this.admin()) {
      return [...this.columns, 'actions'];
    }
    return this.columns;
  });

  readonly displayedHeaders = computed(() =>
    this.displayedColumns().filter(
      (name) => !this.columnsWithoutHeader.includes(name)
    )
  );

  constructor(
    private pageWidthService: PageWidthService,
    private store: Store
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
