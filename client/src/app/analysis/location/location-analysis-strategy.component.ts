import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { LocationKey } from '@api/custom/location-key';
import { Store } from '@ngrx/store';
import { tap } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { filter } from 'rxjs/operators';
import { AppState } from '../../core/core.state';
import { WindowService } from '../../services/window.service';
import { selectLocationKey } from './store/location.selectors';

@Component({
  selector: 'kpn-location-analysis-strategy',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-analysis-strategy [url]="url$ | async"></kpn-analysis-strategy>
  `,
})
export class LocationAnalysisStrategyComponent {
  readonly url$ = this.store.select(selectLocationKey).pipe(
    tap((locationKey: LocationKey) =>
      console.log(`locationKey=${JSON.stringify(locationKey, null, 2)}`)
    ),
    filter((locationKey: LocationKey) => locationKey !== null),
    map((locationKey: LocationKey) => this.url(locationKey))
  );

  constructor(
    private store: Store<AppState>,
    private windowService: WindowService
  ) {}

  url(locationKey: LocationKey): string {
    console.log(
      `locationKey networkType=${locationKey.networkType}, country=${locationKey.country}`
    );

    let url = `/analysis/${locationKey.networkType}/${locationKey.country}/networks`;
    const language = this.windowService.language();
    if (language.length > 0) {
      url = `/${language}${url}`;
    }
    return url;
  }
}
