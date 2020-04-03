import {Component, OnDestroy, OnInit} from "@angular/core";
import {Subscriptions} from "../../../util/Subscriptions";
import {ChangeFilterOptions} from "../../components/changes/filter/change-filter-options";
import {RouteChangesService} from "./route-changes.service";

@Component({
  selector: "kpn-route-changes-sidebar",
  template: `
    <kpn-sidebar>
      <kpn-change-filter [filterOptions]="filterOptions"></kpn-change-filter>
    </kpn-sidebar>
  `
})
export class RouteChangesSidebarComponent implements OnInit, OnDestroy {

  filterOptions: ChangeFilterOptions;
  private readonly subscriptions = new Subscriptions();

  constructor(private routeChangesService: RouteChangesService) {
  }

  ngOnInit(): void {
    this.subscriptions.add(
      this.routeChangesService.filterOptions$.subscribe(filterOptions => this.filterOptions = filterOptions)
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

}

