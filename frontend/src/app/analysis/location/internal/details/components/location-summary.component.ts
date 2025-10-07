import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { LocationDetailsPage } from '@api/common/location/location-details-page';
import { DistancePipe } from '@app/shared/components/format/distance.pipe';
import { ZeroIntegerFormatPipe } from '@app/shared/components/format/zero-integer-format.pipe';

@Component({
  selector: 'ui-location-summary',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <span class="kpn-comma-list">
      @if (page().distance > 0) {
        <span>
          {{ page().distance | distance }}
        </span>
      }
      <span>
        {{ page().summary.nodeCount | zeroInteger }}
        <ng-container i18n="@@network-details.nodes">nodes</ng-container>
      </span>
      <span>
        {{ page().summary.routeCount | zeroInteger }}
        <ng-container i18n="@@network-details.routes">routes</ng-container>
      </span>
    </span>
  `,
  imports: [DistancePipe, ZeroIntegerFormatPipe],
})
export class LocationSummaryComponent {
  readonly page = input.required<LocationDetailsPage>();
}
