import { Injectable } from '@angular/core';
import { Params } from '@angular/router';
import { Map } from 'immutable';
import { ReplaySubject } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
import { Observable } from 'rxjs';
import { LocationSummary } from '@api/common/location/location-summary';
import { LocationKey } from '@api/custom/location-key';
import { NetworkType } from '@api/custom/network-type';
import { LocationParams } from './components/location-params';

@Injectable()
export class LocationService {
  readonly summary$: Observable<LocationSummary>;
  readonly locationKey$: Observable<LocationKey>;

  private readonly _locationKey$: BehaviorSubject<LocationKey>;
  private readonly _summary$: ReplaySubject<LocationSummary>;

  private locationSummaries = Map<string, LocationSummary>();

  constructor() {
    this._locationKey$ = new BehaviorSubject<LocationKey>(null);
    this.locationKey$ = this._locationKey$.asObservable();
    this._summary$ = new ReplaySubject<LocationSummary>(1);
    this.summary$ = this._summary$.asObservable();
    this.locationKey$.subscribe((locationKey) => {
      if (locationKey !== null) {
        const locationSummary = this.locationSummaries.get(
          this._locationKey$.getValue().name
        );
        if (locationSummary) {
          this._summary$.next(locationSummary);
        }
      }
    });
  }

  get name(): string {
    if (this._locationKey$.getValue()) {
      return this._locationKey$.getValue().name;
    }
    return null;
  }

  get key(): string {
    if (this._locationKey$.getValue()) {
      const key = this._locationKey$.getValue();
      return `${key.networkType}/${key.country}/${key.name}`;
    }
    return null;
  }

  get networkType(): NetworkType {
    if (this._locationKey$.getValue()) {
      return this._locationKey$.getValue().networkType;
    }
    return null;
  }

  nextSummary(locationName: string, locationSummary: LocationSummary) {
    this.locationSummaries = this.locationSummaries.set(
      locationName,
      locationSummary
    );
    this._summary$.next(locationSummary);
  }

  location(params: Params): void {
    this._locationKey$.next(LocationParams.toKey(params));
  }
}
