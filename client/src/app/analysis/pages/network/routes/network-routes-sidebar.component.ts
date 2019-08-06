import {Component, OnDestroy, OnInit} from "@angular/core";
import {NetworkRoutesService} from "./network-routes.service";
import {FilterOptions} from "../../../../kpn/filter/filter-options";
import {Subscriptions} from "../../../../util/Subscriptions";

@Component({
  selector: "kpn-network-routes-sidebar",
  template: `
    <kpn-filter [filterOptions]="filterOptions"></kpn-filter>
    <kpn-sidebar-footer></kpn-sidebar-footer>
  `
})
export class NetworkRoutesSidebarComponent implements OnInit, OnDestroy {

  private readonly subscriptions = new Subscriptions();

  filterOptions: FilterOptions;

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
