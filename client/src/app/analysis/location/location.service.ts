import {Injectable} from "@angular/core";
import {Map} from "immutable";
import {LocationSummary} from "../../kpn/api/common/location/location-summary";

@Injectable()
export class LocationService {

  private locationSummaries = Map<string, LocationSummary>();

  getSummary(locationName: string): LocationSummary {
    return this.locationSummaries.get(locationName);
  }

  setSummary(locationName: string, locationSummary: LocationSummary) {
    this.locationSummaries = this.locationSummaries.set(locationName, locationSummary);
  }
}
