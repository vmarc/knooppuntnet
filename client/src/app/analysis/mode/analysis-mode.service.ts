import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { AppState } from '../../core/core.state';
import { selectPreferencesAnalysisMode } from '../../core/preferences/preferences.selectors';
import { AnalysisMode } from '../../core/preferences/preferences.state';

@Injectable()
export class AnalysisModeService {
  constructor(private store: Store<AppState>) {}

  link(networkType: string, country: string): Observable<string> {
    return this.store
      .select(selectPreferencesAnalysisMode)
      .pipe(
        map(
          (analysisMode) =>
            `/analysis/${networkType}/${country}` +
            (analysisMode === AnalysisMode.network ? '/networks' : '')
        )
      );
  }
}
