import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { ApiService } from '@app/services';
import { SurveyDateValues } from './survey-date-values';

@Injectable({
  providedIn: 'root',
})
export class SharedStateService {
  private readonly apiService = inject(ApiService);

  private readonly _httpError = signal<string | null>(null);
  private readonly _surveyDateValues = signal<SurveyDateValues | null>(null);

  readonly httpError = this._httpError.asReadonly();
  readonly surveyDateValues = this._surveyDateValues.asReadonly();

  // TODO SIGNAL
  setHttpError(message: string | null): void {
    this._httpError.set(message);
  }

  loadSurveyDateValues(): void {
    if (this.surveyDateValues() === null) {
      this.apiService.surveyDateInfo().subscribe((response) => {
        const surveyDateValues = SurveyDateValues.from(response.result);
        this._surveyDateValues.set(surveyDateValues);
      });
    }
  }
}
