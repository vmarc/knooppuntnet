import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Params } from '@angular/router';
import { StructureRow } from '@api/common/route/structure-row';
import { RouteDetailsService } from '@app/route/route-details-service';
import { DistancePipe } from '@app/shared/components/format/distance.pipe';
import { TimestampDayPipe } from '@app/shared/components/format/timestamp-day.pipe';
import { NzIconDirective } from 'ng-zorro-antd/icon';

@Component({
  selector: 'ui-route-structure-reference',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let row = structureRow();
    <span class="kpn-line">
      <span i18n="@@monitor.group.route-table.reference" class="kpn-label">Reference</span>
      <span>
        <a
          [routerLink]="uploadGpx()"
          [queryParams]="subRelationIdQueryParams(row)"
          title="Upload GPX trace for this sub-relation"
          i18n-title="@@action.gpx.upload"
          [class.kpn-disabled]="!canUpload()"
        >
          <nz-icon nzType="upload" />
        </a>
        <a
          [routerLink]="deleteGpx()"
          [queryParams]="subRelationIdQueryParams(row)"
          title="Remove GPX trace for this sub-relation"
          i18n-title="@@action.gpx.delete"
          [class.kpn-disabled]="!row.referenceFilename"
          [class.kpn-warning]="row.referenceFilename"
        >
          <nz-icon nzType="delete" />
        </a>
      </span>
      @if (row.referenceFilename) {
        <span>
          {{ row.referenceTimestamp | yyyymmdd }}
        </span>
        <span>
          {{ row.referenceDistance | distance }}
        </span>
        <span>
          {{ row.referenceFilename }}
        </span>
      }
    </span>
  `,
  imports: [DistancePipe, NzIconDirective, RouterLink, TimestampDayPipe],
})
export class RouteStructureReferenceComponent {
  readonly structureRow = input.required<StructureRow>();

  private readonly routeDetailsService = inject(RouteDetailsService);

  subRelationIdQueryParams(row: StructureRow): Params {
    return { 'sub-relation-id': row.id };
  }

  uploadGpx(): string {
    return `/monitor/groups/${this.routeDetailsService.groupName()}/routes/${this.routeDetailsService.routeName()}/gpx`;
  }

  deleteGpx(): string {
    return `/monitor/groups/${this.routeDetailsService.groupName()}/routes/${this.routeDetailsService.routeName()}/gpx/delete`;
  }

  canUpload(): boolean {
    return this.routeDetailsService.referenceType() === 'multi-gpx';
  }

  canDelete(row: StructureRow): boolean {
    return this.routeDetailsService.referenceType() === 'multi-gpx' && !!row.referenceFilename;
  }
}
