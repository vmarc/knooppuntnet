import { signal } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { Fact } from '@api/common';
import { SurveyParameter } from '@api/common/location';
import { LocationNodesParameters } from '@api/common/location';
import { LocationNodesPage } from '@api/common/location';
import { BooleanParameter } from '@api/common/location/boolean-parameter';
import { LastUpdatedParameter } from '@api/common/location/last-updated-parameter';
import { ApiResponse } from '@api/custom';
import { Util } from '@app/components/shared';
import { PreferencesService } from '@app/core';
import { ApiService } from '@app/services';
import { RouterService } from '../../../shared/services/router.service';
import { LocationService } from '../location.service';

export class LocationNodesPageService {
  private readonly apiService = inject(ApiService);
  private readonly locationService = inject(LocationService);
  private readonly preferencesService = inject(PreferencesService);
  private readonly routerService = inject(RouterService);

  private readonly _integrityCheck = signal<BooleanParameter | null>(null);
  private readonly _integrityCheckFailed = signal<BooleanParameter | null>(null);
  private readonly _fact = signal<Fact | null>(null);
  private readonly _survey = signal<SurveyParameter | null>(null);
  private readonly _lastUpdated = signal<LastUpdatedParameter | null>(null);
  private readonly _proposed = signal<BooleanParameter | null>(null);
  private readonly _referencedInRoutes = signal<BooleanParameter | null>(null);
  private readonly _response = signal<ApiResponse<LocationNodesPage> | null>(null);
  private readonly _pageIndex = signal<number>(0);

  readonly response = this._response.asReadonly();
  readonly pageIndex = this._pageIndex.asReadonly();
  readonly networkType = computed(() => this.locationService.key().networkType);
  readonly pageSize = this.preferencesService.pageSize;

  onInit(): void {
    this.locationService.initPage(this.routerService);
    const uniqueQueryParams = Util.uniqueParams(this.routerService.queryParams());

    const integrityCheck = uniqueQueryParams['integrityCheck'];
    const integrityCheckFailed = uniqueQueryParams['integrityCheckFailed'];
    const fact = uniqueQueryParams['fact'];
    const survey = uniqueQueryParams['survey'];
    const lastUpdated = uniqueQueryParams['lastUpdated'];
    const proposed = uniqueQueryParams['proposed'];
    const referencedInRoutes = uniqueQueryParams['referencedInRoutes'];

    this._integrityCheck.set(integrityCheck);
    this._integrityCheckFailed.set(integrityCheckFailed);
    this._fact.set(fact);
    this._survey.set(survey);
    this._lastUpdated.set(lastUpdated);
    this._proposed.set(proposed);
    this._referencedInRoutes.set(referencedInRoutes);

    this.load();
  }

  setPageSize(pageSize: number): void {
    this.preferencesService.setPageSize(pageSize);
    this.load();
  }

  setPageIndex(pageIndex: number): void {
    this._pageIndex.set(pageIndex);
    this.load();
  }

  setIntegrityCheck(value: BooleanParameter): void {
    this._integrityCheck.set(value);
    this.load();
  }

  setIntegrityCheckFailed(value: BooleanParameter): void {
    this._integrityCheckFailed.set(value);
    this.load();
  }

  setFact(value: Fact): void {
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

  setReferencedInRoutes(value: BooleanParameter): void {
    this._referencedInRoutes.set(value);
    this.load();
  }

  private load(): void {
    const parameters: LocationNodesParameters = {
      integrityCheck: this._integrityCheck(),
      integrityCheckFailed: this._integrityCheckFailed(),
      fact: this._fact(),
      survey: this._survey(),
      lastUpdated: this._lastUpdated(),
      proposed: this._proposed(),
      referencedInRoutes: this._referencedInRoutes(),
      pageSize: this.preferencesService.pageSize(),
      pageIndex: this.pageIndex(),
    };
    this.routerService.updateQueryParams(parameters).then(() => {
      this.apiService
        .locationNodes(this.locationService.key(), parameters)
        .subscribe((response) => {
          if (response.result) {
            this.locationService.setSummary(response.result.summary);
          }
          this._response.set(response);
        });
    });
  }
}
