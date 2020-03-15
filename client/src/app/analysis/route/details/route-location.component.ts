import {Input} from "@angular/core";
import {Component} from "@angular/core";
import {List} from "immutable";
import {Location} from "../../../kpn/api/common/location/location";
import {LocationCandidate} from "../../../kpn/api/common/location/location-candidate";
import {RouteLocationAnalysis} from "../../../kpn/api/common/route-location-analysis";

@Component({
  selector: "kpn-route-location",
  template: `
    <p *ngIf="!locationAnalysis || locationAnalysis.candidates.isEmpty()" i18n="@@route.location.none">None</p>
    <div *ngFor="let candidate of locationAnalysis.candidates" class="candidates">
      <div class="kpn-comma-list">
        <span *ngFor="let name of locationNames(candidate.location)">{{name}}</span>
      </div>
      <div class="percentage">{{percentage(candidate)}}</div>
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

  locationNames(location: Location): List<string> {
    const country = location.names.get(0).toUpperCase();
    const names = location.names.set(0, country);
    return names.reverse();
  }

  percentage(locationCandidate: LocationCandidate): string {
    if (locationCandidate.percentage === 100) {
      return "";
    }
    return `${locationCandidate.percentage}%`;
  }

}
