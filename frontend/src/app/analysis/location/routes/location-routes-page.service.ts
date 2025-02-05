import { signal } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { Fact } from '@api/common';
import { LocationRoutesParameters } from '@api/common/location';
import { LocationRoutesPage } from '@api/common/location';
import { BooleanParameter } from '@api/common/location/boolean-parameter';
import { LastUpdatedParameter } from '@api/common/location/last-updated-parameter';
import { SurveyParameter } from '@api/common/location/survey-parameter';
import { ApiResponse } from '@api/custom';
import { ApiService } from '@app/services';
import { Util } from '@app/shared/components/util';
import { State } from '@app/state';
import { RouterService } from '../../../shared/services/router.service';
import { LocationService } from '../location.service';

export class LocationRoutesPageService {
  private readonly state = inject(State);
  private readonly apiService = inject(ApiService);
  private readonly locationService = inject(LocationService);
  private readonly routerService = inject(RouterService);

  private readonly _fact = signal<Fact | null>(null);
  private readonly _survey = signal<SurveyParameter | null>(null);
  private readonly _lastUpdated = signal<LastUpdatedParameter | null>(null);
  private readonly _proposed = signal<BooleanParameter | null>(null);

  private readonly _pageIndex = signal<number>(0);
  private readonly _response = signal<ApiResponse<LocationRoutesPage> | null>(null);

  readonly pageIndex = this._pageIndex.asReadonly();
  readonly response = this._response.asReadonly();
  readonly routeType = computed(() => this.locationService.key().routeType);
  readonly pageSize = computed(() => this.state.preferences.pageSize());

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

  updatePageSize(pageSize: number): void {
    this.state.preferences.updatePageSize(pageSize);
    this._pageIndex.set(0);
    this.load();
  }

  updatePageIndex(pageIndex: number): void {
    this._pageIndex.set(pageIndex);
    this.load();
  }

  updateFact(value: Fact): void {
    this._fact.set(value);
    this.load();
  }

  updateSurvey(value: SurveyParameter): void {
    this._survey.set(value);
    this.load();
  }

  updateLastUpdated(value: LastUpdatedParameter): void {
    this._lastUpdated.set(value);
    this.load();
  }

  updateProposed(value: BooleanParameter): void {
    this._proposed.set(value);
    this.load();
  }

  private load(): void {
    const parameters: LocationRoutesParameters = {
      fact: this._fact(),
      survey: this._survey(),
      lastUpdated: this._lastUpdated(),
      proposed: this._proposed(),
      pageSize: this.state.preferences.pageSize(),
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
