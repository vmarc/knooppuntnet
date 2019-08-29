import {Component, OnDestroy, OnInit} from "@angular/core";
import {FilterOptions} from "../../../kpn/filter/filter-options";
import {Subscriptions} from "../../../util/Subscriptions";
import {NetworkRoutesService} from "./network-routes.service";

@Component({
  selector: "kpn-network-routes-sidebar",
  template: `
    <kpn-sidebar>
      <kpn-filter [filterOptions]="filterOptions"></kpn-filter>
    </kpn-sidebar>
  `
})
export class NetworkRoutesSidebarComponent implements OnInit, OnDestroy {

  filterOptions: FilterOptions;
  private readonly subscriptions = new Subscriptions();

  constructor(private networkRoutesService: NetworkRoutesService) {
  }

  ngOnInit(): void {
    this.subscriptions.add(
      this.networkRoutesService.filterOptions.subscribe(filterOptions => this.filterOptions = filterOptions)
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

}
