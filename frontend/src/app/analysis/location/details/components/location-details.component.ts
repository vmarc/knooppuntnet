import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { LocationDetailsPage } from '@api/common/location/location-details-page';
import { ApiResponse } from '@api/custom';
import { DataComponent } from '@app/shared/components/data/data.component';
import { LocationPipe } from '@app/shared/components/format/location.pipe';
import { InterpretedTags } from '@app/shared/components/tags/interpreted-tags';
import { TagTableComponent } from '@app/shared/components/tags/tag-table.component';
import { TimestampComponent } from '@app/shared/components/timestamp/timestamp.component';
import { ActionButtonRelationComponent } from '../../../components/action/action-button-relation.component';
import { LocationSummaryComponent } from './location-summary.component';

@Component({
  selector: 'kpn-location-details',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-data title="Summary" i18n-title="@@location-details.summary">
      <kpn-location-summary [page]="response().result" />
      <p class="location-names">
        @for (
          locationInfo of response().result.locationInfos;
          track locationInfo.link;
          let last = $last
        ) {
          <div class="location-name">
            @if (last) {
              {{ locationInfo.name | location }}
            } @else {
              <a [routerLink]="locationLink(locationInfo.link)">
                {{ locationInfo.name | location }}</a
              >
            }
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
  styleUrl: '../../../../shared/components/data/data.component.scss',
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
  imports: [
    DataComponent,
    LocationSummaryComponent,
    TagTableComponent,
    TimestampComponent,
    LocationPipe,
    RouterLink,
    ActionButtonRelationComponent,
    LocationPipe,
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
