import {ChangeDetectionStrategy} from '@angular/core';
import {Input} from '@angular/core';
import {Component} from '@angular/core';
import {Location} from '@api/common/location/location';
import {LocationCandidate} from '@api/common/location/location-candidate';
import {RouteLocationAnalysis} from '@api/common/route-location-analysis';

@Component({
  selector: 'kpn-route-location',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p *ngIf="!hasLocations()" i18n="@@route.location.none">None</p>
    <div *ngIf="hasLocations()">
      <div *ngFor="let candidate of locationAnalysis.candidates" class="candidates">
        <div class="kpn-comma-list">
          <span *ngFor="let name of locationNames(candidate.location)">{{name}}</span>
        </div>
        <div class="percentage">{{percentage(candidate)}}</div>
      </div>
    </div>
  `,
  styles: [`
    .candidates {
      margin-bottom: 0.5em;
    }

    .percentage {
      display: inline-block;
      padding-left: 20px;
    }
  `]
})
export class RouteLocationComponent {

  @Input() locationAnalysis: RouteLocationAnalysis;

  locationNames(location: Location): string[] {
    const country = location.names[0].toUpperCase();
    const names = [country].concat(location.names.slice(1));
    return names.reverse();
  }

  hasLocations(): boolean {
    return this.locationAnalysis && this.locationAnalysis.candidates && this.locationAnalysis.candidates.length > 0;
  }

  percentage(locationCandidate: LocationCandidate): string {
    if (locationCandidate.percentage === 100) {
      return '';
    }
    return `${locationCandidate.percentage}%`;
  }

}
