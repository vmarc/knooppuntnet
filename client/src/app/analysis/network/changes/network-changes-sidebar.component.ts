import {ChangeDetectionStrategy} from "@angular/core";
import {Component, OnDestroy, OnInit} from "@angular/core";
import {Subscriptions} from "../../../util/Subscriptions";
import {ChangeFilterOptions} from "../../components/changes/filter/change-filter-options";
import {NetworkChangesService} from "./network-changes.service";

@Component({
  selector: "kpn-network-changes-sidebar",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-change-filter [filterOptions]="filterOptions"></kpn-change-filter>
    </kpn-sidebar>
  `
})
export class NetworkChangesSidebarComponent implements OnInit, OnDestroy {

  filterOptions: ChangeFilterOptions;
  private readonly subscriptions = new Subscriptions();

  constructor(private networkChangesService: NetworkChangesService) {
  }

  ngOnInit(): void {
    this.subscriptions.add(
      this.networkChangesService.filterOptions.subscribe(filterOptions => this.filterOptions = filterOptions)
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

}

