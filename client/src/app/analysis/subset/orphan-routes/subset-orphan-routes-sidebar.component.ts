import {ChangeDetectionStrategy} from "@angular/core";
import {Component, OnDestroy, OnInit} from "@angular/core";
import {FilterOptions} from "../../../kpn/filter/filter-options";
import {Subscriptions} from "../../../util/Subscriptions";
import {SubsetOrphanRoutesService} from "./subset-orphan-routes.service";

@Component({
  selector: "kpn-subset-orphan-routes-sidebar",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-filter [filterOptions]="filterOptions"></kpn-filter>
    </kpn-sidebar>
  `
})
export class SubsetOrphanRoutesSidebarComponent implements OnInit, OnDestroy {

  filterOptions: FilterOptions;
  private readonly subscriptions = new Subscriptions();

  constructor(private subsetOrphanRoutesService: SubsetOrphanRoutesService) {
  }

  ngOnInit(): void {
    this.subscriptions.add(
      this.subsetOrphanRoutesService.filterOptions.subscribe(filterOptions => this.filterOptions = filterOptions)
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

}
