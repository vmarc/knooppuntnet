import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { combineLatest } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
import { Observable } from 'rxjs';
import { first } from 'rxjs/operators';
import { AppService } from '../../../app.service';
import { Util } from '../../../components/shared/util';
import { AppState } from '../../../core/core.state';
import { selectPreferencesItemsPerPage } from '../../../core/preferences/preferences.selectors';
import { selectPreferencesImpact } from '../../../core/preferences/preferences.selectors';
import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { ChangeFilterOptions } from '../../components/changes/filter/change-filter-options';

@Injectable()
export class NodeChangesService {
  readonly parameters$: Observable<ChangesParameters>;
  readonly filterOptions$: Observable<ChangeFilterOptions>;

  /*private*/ readonly _parameters$ = new BehaviorSubject<ChangesParameters>(
    Util.defaultChangesParameters()
  );
  private readonly _filterOptions$ = new BehaviorSubject<ChangeFilterOptions>(
    ChangeFilterOptions.empty()
  );

  constructor(private appService: AppService, private store: Store<AppState>) {
    this.filterOptions$ = this._filterOptions$.asObservable();
    this.parameters$ = this._parameters$.asObservable();
  }

  setFilterOptions(options: ChangeFilterOptions) {
    this._filterOptions$.next(options);
  }

  resetFilterOptions() {
    this.setFilterOptions(ChangeFilterOptions.empty());
  }

  updateParameters(parameters: ChangesParameters) {
    this._parameters$.next(parameters);
  }
}
