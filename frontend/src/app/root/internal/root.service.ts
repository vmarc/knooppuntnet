import { BreakpointObserver } from '@angular/cdk/layout';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { SurveyDateValues } from '@app/shared/core/shared/survey-date-values';
import { ApiService } from '@app/shared/services/api.service';
import { merge } from 'rxjs';
import { State } from '@app/state/state';

@Injectable({
  providedIn: 'root',
})
export class RootService {
  private readonly state = inject(State);
  private readonly apiService = inject(ApiService);
  private readonly breakpointObserver = inject(BreakpointObserver);
  private readonly smallMaxWidth = 1000;

  constructor() {
    const smallMediaQuery = `(max-width: ${this.smallMaxWidth}px)`;
    const breakpointState$ = merge(this.breakpointObserver.observe(smallMediaQuery));
    breakpointState$.subscribe(() => {
      const width = window.innerWidth;
      this.state.page.updateSmall(width <= this.smallMaxWidth);
    });
    this.loadSurveyDateValues();
  }

  loadSurveyDateValues(): void {
    if (!this.state.map.surveyDateValues()) {
      this.apiService.surveyDateInfo().subscribe((response) => {
        const surveyDateValues = SurveyDateValues.from(response.result);
        this.state.map.updateSurveyDateValues(surveyDateValues);
      });
    }
  }
}
