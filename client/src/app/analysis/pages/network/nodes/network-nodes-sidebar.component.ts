import {Component, OnDestroy, OnInit} from "@angular/core";
import {FilterOptions} from "../../../../kpn/filter/filter-options";
import {Subscriptions} from "../../../../util/Subscriptions";
import {NetworkNodesService} from "./network-nodes.service";

@Component({
  selector: "kpn-network-nodes-sidebar",
  template: `
    <kpn-filter [filterOptions]="filterOptions"></kpn-filter>
    <kpn-sidebar-footer></kpn-sidebar-footer>
  `
})
export class NetworkNodesSidebarComponent implements OnInit, OnDestroy {

  private readonly subscriptions = new Subscriptions();

  filterOptions: FilterOptions;

  constructor(private networkNodesService: NetworkNodesService) {
  }

  ngOnInit(): void {
    this.subscriptions.add(
      this.networkNodesService.filterOptions.subscribe(filterOptions => this.filterOptions = filterOptions)
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

}
