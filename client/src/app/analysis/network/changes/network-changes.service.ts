import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {Observable} from 'rxjs';
import {Util} from '../../../components/shared/util';
import {ChangesParameters} from '../../../kpn/api/common/changes/filter/changes-parameters';
import {ChangeFilterOptions} from '../../components/changes/filter/change-filter-options';

@Injectable({
  providedIn: 'root',
})
export class NetworkChangesService {

  readonly parameters$: Observable<ChangesParameters>;
  readonly filterOptions$: Observable<ChangeFilterOptions>;

  /*private*/
  readonly _parameters$ = new BehaviorSubject<ChangesParameters>(Util.defaultChangesParameters());
  private readonly _filterOptions$ = new BehaviorSubject<ChangeFilterOptions>(ChangeFilterOptions.empty());

  constructor() {
    this.parameters$ = this._parameters$.asObservable();
    this.filterOptions$ = this._filterOptions$.asObservable();
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
