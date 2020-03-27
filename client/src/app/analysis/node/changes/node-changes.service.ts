import {Injectable} from "@angular/core";
import {BehaviorSubject} from "rxjs";
import {ChangesParameters} from "../../../kpn/api/common/changes/filter/changes-parameters";
import {ChangeFilterOptions} from "../../components/changes/filter/change-filter-options";

@Injectable()
export class NodeChangesService {

  readonly parameters$ = new BehaviorSubject<ChangesParameters>(this.initialParameters());

  readonly filterOptions$ = new BehaviorSubject<ChangeFilterOptions>(ChangeFilterOptions.empty());

  resetFilterOptions() {
    this.filterOptions$.next(ChangeFilterOptions.empty());
  }

  updateParameters(parameters: ChangesParameters) {
    this.parameters$.next(parameters);
  }

  private initialParameters(): ChangesParameters {
    // TODO read from localstorage
    return new ChangesParameters(null, null, null, null, null, null, null, 5, 0, false);
  }

}
