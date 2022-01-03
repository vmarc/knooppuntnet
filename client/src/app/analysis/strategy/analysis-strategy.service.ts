import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { AppState } from '../../core/core.state';
import { selectPreferencesAnalysisStrategy } from '../../core/preferences/preferences.selectors';
import { AnalysisStrategy } from '../../core/preferences/preferences.state';

@Injectable()
export class AnalysisStrategyService {
  constructor(private store: Store<AppState>) {}

  link(networkType: string, country: string): Observable<string> {
    return this.store
      .select(selectPreferencesAnalysisStrategy)
      .pipe(
        map(
          (strategy) =>
            `/analysis/${networkType}/${country}` +
            (strategy === AnalysisStrategy.network ? '/networks' : '')
        )
      );
  }
}
