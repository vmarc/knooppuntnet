import {Component, OnDestroy, OnInit} from "@angular/core";
import {FilterOptions} from "../../../kpn/filter/filter-options";
import {Subscriptions} from "../../../util/Subscriptions";
import {SubsetOrphanNodesService} from "./subset-orphan-nodes.service";

@Component({
  selector: "kpn-subset-orphan-nodes-sidebar",
  template: `
    <kpn-sidebar>
      <kpn-filter [filterOptions]="filterOptions"></kpn-filter>
    </kpn-sidebar>
  `
})
export class SubsetOrphanNodesSidebarComponent implements OnInit, OnDestroy {

  filterOptions: FilterOptions;
  private readonly subscriptions = new Subscriptions();

  constructor(private subsetOrphanNodesService: SubsetOrphanNodesService) {
  }

  ngOnInit(): void {
    this.subscriptions.add(
      this.subsetOrphanNodesService.filterOptions.subscribe(filterOptions => this.filterOptions = filterOptions)
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

}
