import {Injectable} from "@angular/core";
import {Params} from "@angular/router";
import {Map} from "immutable";
import {BehaviorSubject} from "rxjs";
import {Observable} from "rxjs";
import {LocationSummary} from "../../kpn/api/common/location/location-summary";
import {LocationKey} from "../../kpn/api/custom/location-key";
import {LocationParams} from "./components/location-params";

@Injectable()
export class LocationService {

  readonly locationKey$: Observable<LocationKey>;

  private readonly _locationKey$: BehaviorSubject<LocationKey>;
  private locationSummaries = Map<string, LocationSummary>();

  constructor() {
    this._locationKey$ = new BehaviorSubject<LocationKey>(null);
    this.locationKey$ = this._locationKey$.asObservable();
  }

  get name(): string {
    if (this._locationKey$.getValue()) {
      return this._locationKey$.getValue().name;
    }
    return null;
  }

  get key(): string {
    if (this._locationKey$.getValue()) {
      return this._locationKey$.getValue().key();
    }
    return null;
  }

  get summary(): LocationSummary {
    if (this._locationKey$.getValue()) {
      return this.locationSummaries.get(this._locationKey$.getValue().name);
    }
    return null;
  }

  setSummary(locationName: string, locationSummary: LocationSummary) {
    this.locationSummaries = this.locationSummaries.set(locationName, locationSummary);
  }

  location(params: Params): void {
    this._locationKey$.next(LocationParams.toKey(params));
  }
}
