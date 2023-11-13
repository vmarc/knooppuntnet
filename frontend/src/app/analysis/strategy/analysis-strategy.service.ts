import { Injectable } from '@angular/core';
import { selectPreferencesAnalysisStrategy } from '@app/core';
import { AnalysisStrategy } from '@app/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable()
export class AnalysisStrategyService {
  constructor(private store: Store) {}

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
