import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { LocationDetailsPage } from '@api/common/location/location-details-page';
import { ApiResponse } from '@api/custom';
import { DataComponent } from '@app/components/shared/data';
import { InterpretedTags } from '@app/components/shared/tags';
import { TagTableComponent } from '@app/components/shared/tags';
import { TimestampComponent } from '@app/components/shared/timestamp';
import { LocationPipe } from '../../../../shared/components/shared/format/location.pipe';
import { ActionButtonRelationComponent } from '../../../components/action/action-button-relation.component';
import { LocationSummaryComponent } from './location-summary.component';

@Component({
  selector: 'kpn-location-details',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-data title="Summary" i18n-title="@@location-details.summary">
      <kpn-location-summary [page]="response().result" />
      <p class="location-names">
        @for (locationInfo of response().result.locationInfos; track locationInfo.link) {
          <div class="location-name">
            <a [routerLink]="locationLink(locationInfo.link)">
              {{ locationInfo.name | location }}</a
            >
          </div>
        }
      </p>

      <div class="kpn-line">
        {{ relationId() }}
        <kpn-action-button-relation [relationId]="relationId()" />
      </div>
    </kpn-data>

    <kpn-data title="Situation on" i18n-title="@@location-details.situation-on">
      <kpn-timestamp [timestamp]="response().situationOn" />
    </kpn-data>

    <kpn-data title="Tags" i18n-title="@@location-details.tags">
      <kpn-tag-table [tags]="tags()" />
    </kpn-data>
  `,
  styleUrl: '../../../../shared/components/shared/data/data.component.scss',
  styles: `
    .location-name {
      display: inline;
    }

    .location-names {
      padding-top: 1em;
    }

    .location-names :not(:last-child):after {
      content: ' \\2192 \\0020 ';
    }
  `,
  standalone: true,
  imports: [
    DataComponent,
    LocationSummaryComponent,
    TagTableComponent,
    TimestampComponent,
    LocationPipe,
    RouterLink,
    ActionButtonRelationComponent,
  ],
})
export class LocationDetailsComponent {
  response = input.required<ApiResponse<LocationDetailsPage>>();
  tags = computed(() => InterpretedTags.locationTags(this.response().result.tags));
  relationId = computed(() => this.response().result.relationId);

  locationLink(link: string): string {
    return `/analysis/${link}/details`;
  }
}
