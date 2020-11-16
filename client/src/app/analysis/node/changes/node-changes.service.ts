import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {Observable} from 'rxjs';
import {AppService} from '../../../app.service';
import {Util} from '../../../components/shared/util';
import {ChangesParameters} from '../../../kpn/api/common/changes/filter/changes-parameters';
import {ChangeFilterOptions} from '../../components/changes/filter/change-filter-options';

@Injectable()
export class NodeChangesService {

  readonly parameters$ = new BehaviorSubject<ChangesParameters>(this.initialParameters());
  readonly filterOptions$: Observable<ChangeFilterOptions>;

  private readonly _filterOptions$ = new BehaviorSubject<ChangeFilterOptions>(ChangeFilterOptions.empty());

  constructor(private appService: AppService) {
    this.filterOptions$ = this._filterOptions$.asObservable();
  }

  setFilterOptions(options: ChangeFilterOptions) {
    this._filterOptions$.next(options);
  }

  resetFilterOptions() {
    this.setFilterOptions(ChangeFilterOptions.empty());
  }

  updateParameters(parameters: ChangesParameters) {
    this.appService.storeChangesParameters(parameters);
    this.parameters$.next(parameters);
  }

  private initialParameters(): ChangesParameters {
    const initialParameters = Util.defaultChangesParameters();
    return this.appService.changesParameters(initialParameters);
  }

}
