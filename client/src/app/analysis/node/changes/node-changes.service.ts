import {Injectable} from "@angular/core";
import {BehaviorSubject} from "rxjs";
import {ChangeFilterOptions} from "../../components/changes/filter/change-filter-options";

@Injectable()
export class NodeChangesService {
  readonly filterOptions: BehaviorSubject<ChangeFilterOptions> = new BehaviorSubject(ChangeFilterOptions.empty());
}
