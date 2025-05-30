import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { LocationInfo } from '@api/common/location-info';
import { LocationCandidateInfo } from '@api/common/location/location-candidate-info';
import { RouteType } from '@api/common/route-type';

@Component({
  selector: 'ui-route-location',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (!locationCandidateInfos()) {
      <p i18n="@@route.location.none">None</p>
    } @else {
      @for (candidate of locationCandidateInfos(); track candidate) {
        <div class="candidates">
          <div class="kpn-comma-list">
            @for (
              locationInfo of candidate.locationInfos;
              track locationInfo.name;
              let i = $index
            ) {
              <a [routerLink]="link(locationInfo)">{{ locationInfo.name }}</a>
            }
          </div>
          <div class="percentage">{{ percentage(candidate) }}</div>
        </div>
      }
    }
  `,
  styles: `
    .candidates {
      margin-bottom: 0.5em;
    }

    .percentage {
      display: inline-block;
      padding-left: 20px;
    }
  `,
  imports: [RouterLink],
})
export class RouteLocationComponent {
  routeType = input.required<RouteType>();
  locationCandidateInfos = input.required<LocationCandidateInfo[]>();

  link(locationInfo: LocationInfo) {
    return `/analysis/${this.routeType()}/${locationInfo.link}/details`;
  }

  percentage(locationCandidateInfo: LocationCandidateInfo): string {
    if (locationCandidateInfo.percentage === 100) {
      return '';
    }
    return `${locationCandidateInfo.percentage}%`;
  }
}
