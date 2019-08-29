import {Component, OnDestroy, OnInit} from "@angular/core";
import {Subscriptions} from "../../../util/Subscriptions";
import {ChangeFilterOptions} from "../../components/changes/filter/change-filter-options";
import {SubsetChangesService} from "./subset-changes.service";

@Component({
  selector: "kpn-subset-changes-sidebar",
  template: `
    <kpn-sidebar>
      <kpn-change-filter [filterOptions]="filterOptions"></kpn-change-filter>
    </kpn-sidebar>
  `
})
export class SubsetChangesSidebarComponent implements OnInit, OnDestroy {

  filterOptions: ChangeFilterOptions;
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
