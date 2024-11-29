import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { LocationDetailsPage } from '@api/common/location/location-details-page';
import { DistancePipe } from '@app/components/shared/format';
import { MarkdownModule } from 'ngx-markdown';
import { ZeroIntegerFormatPipe } from '../../../../shared/components/shared/format/zero-integer-format.pipe';

@Component({
  selector: 'kpn-location-summary',
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
  imports: [MarkdownModule, MatIconModule, DistancePipe, ZeroIntegerFormatPipe],
})
export class LocationSummaryComponent {
  page = input.required<LocationDetailsPage>();
}
