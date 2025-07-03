import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { StructureRow } from '@api/common/route/structure-row';
import { DistancePipe } from '@app/shared/components/format/distance.pipe';

@Component({
  selector: 'ui-route-structure-deviations',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let row = structureRow();
    @if (row.deviationCount > 0) {
      <div class="kpn-line">
        <span>
          {{ row.deviationCount }}
        </span>
        <span i18n="@@monitor.group.route-table.deviations"> deviations </span>
        <span>
          {{ row.deviationDistance | distance }}
        </span>
      </div>
    }
  `,
  imports: [DistancePipe],
})
export class RouteStructureDeviationsComponent {
  readonly structureRow = input.required<StructureRow>();
}
