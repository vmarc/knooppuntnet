import { NgClass } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Params } from '@angular/router';
import { RouterLink } from '@angular/router';
import { StructureRow } from '@api/common/route/structure-row';
import { DayPipe } from '@app/shared/components/format/day.pipe';
import { DistancePipe } from '@app/shared/components/format/distance.pipe';
import { TimestampDayPipe } from '@app/shared/components/format/timestamp-day.pipe';
import { IconHappyComponent } from '@app/shared/components/icon/icon-happy.component';
import { SymbolComponent } from '@app/symbol/symbol.component';
import { ActionButtonRelationComponent } from '@app/analysis/components/action/action-button-relation.component';
import { MonitorRouteGapComponent } from '../monitor-route-gap.component';

@Component({
  selector: 'ui-monitor-route-structure-row',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- eslint-disable @angular-eslint/template/cyclomatic-complexity -->
    {{ structureRow().rowNumber }}

    <div>
      <span i18n="@@monitor.route.relation-table.name" class="kpn-label">Name</span>
      @switch (structureRow().level) {
        @case (1) {
          <span class="level-1">{{ structureRow().name }}</span>
        }
        @case (2) {
          <span class="level-2">{{ structureRow().name }}</span>
        }
        @case (3) {
          <span class="level-3">{{ structureRow().name }}</span>
        }
        @case (4) {
          <span class="level-4">{{ structureRow().name }}</span>
        }
        @case (5) {
          <span class="level-5">{{ structureRow().name }}</span>
        }
      }
    </div>

    @if (structureRow().happy) {
      <div>
        <ui-icon-happy />
      </div>
    }

    <div>
      <span i18n="@@monitor.route.relation-table.relation" class="kpn-label"> Relation </span>
      <span class="action-button-table-cell">
        <div class="kpn-align-center">
          <ui-action-button-relation [relationId]="structureRow().relationId" />
          {{ structureRow().relationId }}
        </div>
      </span>
    </div>

    <div>
      <span i18n="@@monitor.route.relation-table.symbol" class="kpn-label"> Symbol </span>
      <span class="symbol">
        @if (structureRow().symbol) {
          <ui-symbol [description]="structureRow().symbol" [width]="25" [height]="25" />
        }
      </span>
    </div>

    <div>
      <span i18n="@@monitor.route.relation-table.role" class="kpn-label">Role</span>
      <span>
        {{ structureRow().role }}
      </span>
    </div>

    <div>
      <span i18n="@@monitor.route.relation-table.distance" class="kpn-label"> Distance </span>
      <span class="distance">
        @if (structureRow().osmDistanceSubRelations > 0) {
          <span class="cumulative-distance" matTooltip="Total length of ways in all subrelations">
            {{ structureRow().osmDistanceSubRelations | distance }}
          </span>
        }

        @if (structureRow().osmDistanceSubRelations > 0 && structureRow().osmDistance > 0) {
          <span> / </span>
        }

        @if (structureRow().osmDistance > 0) {
          <span matTooltip="Total length of ways in this relation">
            {{ structureRow().osmDistance | distance }}
          </span>
        }
      </span>
    </div>

    <div>
      <span i18n="@@monitor.route.relation-table.survey" class="kpn-label"> Survey </span>
      <span>
        {{ structureRow().survey | day }}
      </span>
    </div>

    @if (structureRow().referenceFilename) {
      <div class="kpn-line">
        <span i18n="@@monitor.group.route-table.reference" class="kpn-label">Reference</span>
        <span>
          {{ structureRow().referenceTimestamp | yyyymmdd }}
        </span>
        <span>
          {{ structureRow().referenceDistance | distance }}
        </span>
        <span>
          {{ structureRow().referenceFilename }}
        </span>
      </div>
    }

    @if (structureRow().deviationCount > 0) {}
    <div class="kpn-line">
      <span i18n="@@monitor.group.route-table.deviations" class="kpn-label"> Deviations </span>
      <span>
        {{ structureRow().deviationCount }}
      </span>
      <span>
        {{ structureRow().deviationDistance | distance }}
      </span>
    </div>

    <div>
      <span i18n="@@monitor.group.route-table.segments" class="kpn-label"> Segments </span>
      <span [ngClass]="{ 'no-route-gap': structureRow().gaps === undefined }">
        @if (structureRow().gaps !== undefined) {
          <ui-monitor-route-gap
            [description]="structureRow().gaps"
            [osmSegmentCount]="structureRow().osmSegmentCount"
          />
        }
      </span>
    </div>

    <div>
      <span i18n="@@monitor.group.route-table.actions" class="kpn-label">Actions</span>
      <span class="kpn-action-cell">
        @if (structureRow().physical) {
          <button
            mat-icon-button
            [routerLink]="uploadGpx()"
            [queryParams]="subRelationIdQueryParams(structureRow())"
            [disabled]="!canUpload()"
            title="Upload GPX trace for this sub-relation"
            i18n-title="@@action.gpx.upload"
            class="kpn-action-button"
            [class.kpn-disabled]="!canUpload()"
          >
            <mat-icon svgIcon="upload" />
          </button>
          <button
            [routerLink]="deleteGpx()"
            [queryParams]="subRelationIdQueryParams(structureRow())"
            [disabled]="!canDelete(structureRow())"
            title="Remove GPX trace for this sub-relation"
            i18n-title="@@action.gpx.delete"
            class="kpn-action-button"
            [class.kpn-disabled]="!structureRow().referenceFilename"
            [class.kpn-warning]="structureRow().referenceFilename"
          >
            <mat-icon svgIcon="garbage" />
          </button>
        }
      </span>
    </div>
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
  imports: [
    ActionButtonRelationComponent,
    DayPipe,
    DistancePipe,
    DistancePipe,
    IconHappyComponent,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatTooltipModule,
    MonitorRouteGapComponent,
    NgClass,
    RouterLink,
    SymbolComponent,
    TimestampDayPipe,
    TimestampDayPipe,
  ],
})
export class MonitorRouteStructureRowComponent {
  readonly admin = input.required<boolean>();
  readonly groupName = input.required<string>();
  readonly routeName = input.required<string>();
  readonly structureRow = input.required<StructureRow>();
  readonly referenceType = input.required<string>();

  subRelationIdQueryParams(row: StructureRow): Params {
    if (row.level === 1) {
      return {};
    }
    return { 'sub-relation-id': row.relationId };
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

  canDelete(row: StructureRow): boolean {
    return this.referenceType() === 'multi-gpx' && !!row.referenceFilename;
  }
}
