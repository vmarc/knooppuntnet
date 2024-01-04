import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { RouteLocationAnalysis } from '@api/common';
import { Location } from '@api/common/location';
import { LocationCandidate } from '@api/common/location';
import { NetworkType } from '@api/custom';
import { Util } from '@app/components/shared';
import { I18nService } from '@app/i18n';

@Component({
  selector: 'kpn-route-location',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (!hasLocations()) {
      <p i18n="@@route.location.none">None</p>
    } @else {
      @for (candidate of locationAnalysis.candidates; track candidate) {
        <div class="candidates">
          <div class="kpn-comma-list">
            @for (name of locationNames(candidate.location); track name; let i = $index) {
              <a [routerLink]="locationLink(candidate.location, i)">{{ name }}</a>
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
  standalone: true,
  imports: [RouterLink],
})
export class RouteLocationComponent {
  @Input() networkType: NetworkType;
  @Input() locationAnalysis: RouteLocationAnalysis;

  constructor(private i18nService: I18nService) {}

  locationNames(location: Location): string[] {
    const country = location.names[0].toUpperCase();
    const names = [country].concat(location.names.slice(1));
    return names.reverse();
  }

  hasLocations(): boolean {
    return (
      this.locationAnalysis &&
      this.locationAnalysis.candidates &&
      this.locationAnalysis.candidates.length > 0
    );
  }

  percentage(locationCandidate: LocationCandidate): string {
    if (locationCandidate.percentage === 100) {
      return '';
    }
    return `${locationCandidate.percentage}%`;
  }

  locationLink(location: Location, index: number): string {
    const country = location.names[0].toLowerCase();
    const countryName = this.i18nService.translation('@@country.' + Util.safeGet(() => country));
    const locationParts = [countryName].concat(
      location.names.slice(1, location.names.length - index)
    );
    const locationString = locationParts.join(':');
    return `/analysis/${this.networkType}/${country}/${locationString}/nodes`;
  }
}
