import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { LocationDetailsPage } from '@api/common/location/location-details-page';
import { CountryNameComponent } from '@app/components/shared';
import { DistancePipe } from '@app/components/shared/format';
import { IntegerFormatPipe } from '@app/components/shared/format';
import { MarkdownModule } from 'ngx-markdown';
import { ActionButtonRelationComponent } from '../../../components/action/action-button-relation.component';

@Component({
  selector: 'kpn-location-summary',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <span class="kpn-comma-list">
      <span>
        {{ page().distance | distance }}
      </span>
      <span>
        {{ page().summary.nodeCount | integer }}
        <ng-container i18n="@@network-details.nodes">nodes</ng-container>
      </span>
      <span>
        {{ page().summary.routeCount | integer }}
        <ng-container i18n="@@network-details.routes">routes</ng-container>
      </span>
    </span>
  `,
  standalone: true,
  imports: [
    CountryNameComponent,
    IntegerFormatPipe,
    MarkdownModule,
    MatIconModule,
    ActionButtonRelationComponent,
    DistancePipe,
  ],
})
export class LocationSummaryComponent {
  page = input.required<LocationDetailsPage>();
}
