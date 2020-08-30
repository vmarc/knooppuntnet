import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";
import {NetworkChangesService} from "./network-changes.service";

@Component({
  selector: "kpn-network-changes-sidebar",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-change-filter [filterOptions]="networkChangesService.filterOptions$ | async"></kpn-change-filter>
    </kpn-sidebar>
  `
})
export class NetworkChangesSidebarComponent {
  constructor(public networkChangesService: NetworkChangesService) {
  }

  ngOnInit() {
    this.networkChangesService.filterOptions$.subscribe(options => console.log(JSON.stringify(options, null, 2)));
  }

}

