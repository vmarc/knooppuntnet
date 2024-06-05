import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { LocationDetailsPage } from '@api/common/location/location-details-page';
import { ApiResponse } from '@api/custom';
import { DataComponent } from '@app/components/shared/data';
import { TagsTableComponent } from '@app/components/shared/tags';
import { TimestampComponent } from '@app/components/shared/timestamp';
import { LocationPipe } from '../../../../shared/components/shared/format/location.pipe';
import { LocationSummaryComponent } from './location-summary.component';

@Component({
  selector: 'kpn-location-details',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-data title="Summary" i18n-title="@@network-details.summary">
      <kpn-location-summary [page]="response().result" />
      <p class="location-names">
        @for (locationInfo of response().result.locationInfos; track locationInfo.link) {
          <div class="location-name">
            <a
              [routerLink]="locationLink(locationInfo.link)"
              [skipLocationChange]="true"
              [replaceUrl]=""
            >
              {{ locationInfo.name | location }}</a
            >
          </div>
        }
      </p>
    </kpn-data>

    <div class="data2">
      <div class="title">
        <span i18n="@@network-details.situation-on">Situation on</span>
      </div>
      <div class="body">
        <kpn-timestamp [timestamp]="response().situationOn" />
      </div>
    </div>
  `,
  styleUrl: '../../../../shared/components/shared/data/data.component.scss',
  styles: `
    .location-name {
      display: inline;
    }

    .location-names :not(:last-child):after {
      content: ' \\2192 \\0020 ';
    }
  `,
  standalone: true,
  imports: [
    DataComponent,
    LocationSummaryComponent,
    TagsTableComponent,
    TimestampComponent,
    LocationPipe,
    RouterLink,
  ],
})
export class LocationDetailsComponent {
  response = input.required<ApiResponse<LocationDetailsPage>>();

  locationLink(link: string): string {
    return `/analysis/${link}/details`;
  }
}
