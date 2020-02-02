import {Injectable} from "@angular/core";
import {BehaviorSubject} from "rxjs";
import {ChangeFilterOptions} from "./change-filter-options";

@Injectable()
export class ChangesService {

  readonly filterOptions: BehaviorSubject<ChangeFilterOptions> = new BehaviorSubject(ChangeFilterOptions.empty());

  resetFilterOptions(): void {
    this.filterOptions.next(ChangeFilterOptions.empty());
  }
}
