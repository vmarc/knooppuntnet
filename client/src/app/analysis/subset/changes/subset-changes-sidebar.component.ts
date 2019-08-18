import {Component, OnDestroy, OnInit} from "@angular/core";
import {FilterOptions} from "../../../kpn/filter/filter-options";
import {Subscriptions} from "../../../util/Subscriptions";
import {SubsetChangesService} from "./subset-changes.service";

@Component({
  selector: "kpn-subset-changes-sidebar",
  template: `
    <kpn-filter [filterOptions]="filterOptions"></kpn-filter>
    <kpn-sidebar-footer></kpn-sidebar-footer>
  `,
  styles: [`

    :host {
      display: flex;
      flex-direction: column;
      min-height: calc(100vh - 48px);
    }

    kpn-filter {
      flex: 1;
    }

    kpn-side-bar-footer {
      flex: 0;
    }

  `]
})
export class SubsetChangesSidebarComponent implements OnInit, OnDestroy {

  filterOptions: FilterOptions;
  private readonly subscriptions = new Subscriptions();

  constructor(private subsetChangesService: SubsetChangesService) {
  }

  ngOnInit(): void {
    this.subscriptions.add(
      this.subsetChangesService.filterOptions.subscribe(filterOptions => this.filterOptions = filterOptions)
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

}
