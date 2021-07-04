import { Injectable } from '@angular/core';
import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { Store } from '@ngrx/store';
import { BehaviorSubject } from 'rxjs';
import { Observable } from 'rxjs';
import { Util } from '../../../components/shared/util';
import { AppState } from '../../../core/core.state';
import { ChangeFilterOptions } from '../../components/changes/filter/change-filter-options';

@Injectable()
export class RouteChangesService {
  readonly parameters$: Observable<ChangesParameters>;
  readonly filterOptions$: Observable<ChangeFilterOptions>;

  /*private*/
  readonly _parameters$ = new BehaviorSubject<ChangesParameters>(
    Util.defaultChangesParameters()
  );
  private readonly _filterOptions$ = new BehaviorSubject<ChangeFilterOptions>(
    ChangeFilterOptions.empty()
  );

  constructor(private store: Store<AppState>) {
    this.parameters$ = this._parameters$.asObservable();
    this.filterOptions$ = this._filterOptions$.asObservable();
  }

  setFilterOptions(options: ChangeFilterOptions): void {
    this._filterOptions$.next(options);
  }

  resetFilterOptions() {
    this.setFilterOptions(ChangeFilterOptions.empty());
  }

  updateParameters(parameters: ChangesParameters) {
    this._parameters$.next(parameters);
  }
}
