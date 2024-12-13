import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { LocationInfo } from '@api/common';
import { LocationCandidateInfo } from '@api/common/location/location-candidate-info';
import { NetworkType } from '@api/common';

@Component({
  selector: 'kpn-route-location',
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
  networkType = input.required<NetworkType>();
  locationCandidateInfos = input.required<LocationCandidateInfo[]>();

  link(locationInfo: LocationInfo) {
    return `/analysis/${this.networkType()}/${locationInfo.link}/details`;
  }

  percentage(locationCandidateInfo: LocationCandidateInfo): string {
    if (locationCandidateInfo.percentage === 100) {
      return '';
    }
    return `${locationCandidateInfo.percentage}%`;
  }
}
