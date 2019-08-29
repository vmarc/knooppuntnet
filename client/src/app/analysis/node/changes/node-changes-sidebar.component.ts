import {Component, OnDestroy, OnInit} from "@angular/core";
import {Subscriptions} from "../../../util/Subscriptions";
import {ChangeFilterOptions} from "../../components/changes/filter/change-filter-options";
import {NodeChangesService} from "./node-changes.service";

@Component({
  selector: "kpn-node-changes-sidebar",
  template: `
    <kpn-sidebar>
      <kpn-change-filter [filterOptions]="filterOptions"></kpn-change-filter>
    </kpn-sidebar>
  `
})
export class NodeChangesSidebarComponent implements OnInit, OnDestroy {

  filterOptions: ChangeFilterOptions;
  private readonly subscriptions = new Subscriptions();

  constructor(private nodeChangesService: NodeChangesService) {
  }

  ngOnInit(): void {
    this.subscriptions.add(
      this.nodeChangesService.filterOptions.subscribe(filterOptions => this.filterOptions = filterOptions)
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

}

