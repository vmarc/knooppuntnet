import { signal } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { LocationRoutesParameters } from '@api/common/location';
import { LocationRoutesPage } from '@api/common/location';
import { BooleanParameter } from '@api/common/location/boolean-parameter';
import { LastUpdatedParameter } from '@api/common/location/last-updated-parameter';
import { SurveyParameter } from '@api/common/location/survey-parameter';
import { ApiResponse } from '@api/custom';
import { Util } from '@app/components/shared';
import { PreferencesService } from '@app/core';
import { ApiService } from '@app/services';
import { RouterService } from '../../../shared/services/router.service';
import { LocationService } from '../location.service';

export class LocationRoutesPageService {
  private readonly apiService = inject(ApiService);
  private readonly locationService = inject(LocationService);
  private readonly preferencesService = inject(PreferencesService);
  private readonly routerService = inject(RouterService);

  private readonly _fact = signal<string | null>(null);
  private readonly _survey = signal<SurveyParameter | null>(null);
  private readonly _lastUpdated = signal<LastUpdatedParameter | null>(null);
  private readonly _proposed = signal<BooleanParameter | null>(null);

  private readonly _pageIndex = signal<number>(0);
  private readonly _response = signal<ApiResponse<LocationRoutesPage> | null>(null);

  readonly pageIndex = this._pageIndex.asReadonly();
  readonly response = this._response.asReadonly();
  readonly networkType = computed(() => this.locationService.key().networkType);
  readonly pageSize = computed(() => this.preferencesService.pageSize());

  onInit(): void {
    this.locationService.initPage(this.routerService);

    const uniqueQueryParams = Util.uniqueParams(this.routerService.queryParams());

    const fact = uniqueQueryParams['fact'];
    const survey = uniqueQueryParams['survey'];
    const lastUpdated = uniqueQueryParams['lastUpdated'];
    const proposed = uniqueQueryParams['proposed'];

    this._fact.set(fact);
    this._survey.set(survey);
    this._lastUpdated.set(lastUpdated);
    this._proposed.set(proposed);

    this.load();
  }

  setPageSize(pageSize: number): void {
    this.preferencesService.setPageSize(pageSize);
    this._pageIndex.set(0);
    this.load();
  }

  setPageIndex(pageIndex: number): void {
    this._pageIndex.set(pageIndex);
    this.load();
  }

  setFact(value: string): void {
    this._fact.set(value);
    this.load();
  }

  setSurvey(value: SurveyParameter): void {
    this._survey.set(value);
    this.load();
  }

  setLastUpdated(value: LastUpdatedParameter): void {
    this._lastUpdated.set(value);
    this.load();
  }

  setProposed(value: BooleanParameter): void {
    this._proposed.set(value);
    this.load();
  }

  private load(): void {
    const parameters: LocationRoutesParameters = {
      fact: this._fact(),
      survey: this._survey(),
      lastUpdated: this._lastUpdated(),
      proposed: this._proposed(),
      pageSize: this.preferencesService.pageSize(),
      pageIndex: this.pageIndex(),
    };
    this.routerService.updateQueryParams(parameters).then(() => {
      this.apiService
        .locationRoutes(this.locationService.key(), parameters)
        .subscribe((response) => {
          if (response.result) {
            this.locationService.setSummary(response.result.summary);
          }
          this._response.set(response);
        });
    });
  }
}
