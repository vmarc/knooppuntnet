import {Component} from "@angular/core";
import {Observable} from "rxjs";
import {ChangeFilterOptions} from "../../components/changes/filter/change-filter-options";
import {NodeChangesService} from "./node-changes.service";

@Component({
  selector: "kpn-node-changes-sidebar",
  template: `
    <kpn-sidebar>
      <kpn-change-filter [filterOptions]="filterOptions$ | async"></kpn-change-filter>
    </kpn-sidebar>
  `
})
export class NodeChangesSidebarComponent {

  readonly filterOptions$: Observable<ChangeFilterOptions>;

  constructor(private nodeChangesService: NodeChangesService) {
    this.filterOptions$ = this.nodeChangesService.filterOptions$;
  }
}

