import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {AppService} from "../../../app.service";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {NetworkChangesPage} from "../../../kpn/shared/network/network-changes-page";
import {Subset} from "../../../kpn/shared/subset";
import {Country} from "../../../kpn/shared/country";
import {NetworkType} from "../../../kpn/shared/network-type";

@Component({
  selector: 'kpn-network-changes-page',
  template: `
    <kpn-page>
      <kpn-toolbar toolbar></kpn-toolbar>
      <kpn-network-sidenav sidenav [subset]="subset" [networkId]="networkId"></kpn-network-sidenav>
      <div content>
        <h1>
          Network changes
        </h1>
        <div *ngIf="response">
          <json [object]="response"></json>
        </div>
      </div>
    </kpn-page>
  `
})
export class NetworkChangesPageComponent implements OnInit, OnDestroy {

  subset: Subset;
  networkId: string;

  response: ApiResponse<NetworkChangesPage>;
  paramsSubscription: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      this.networkId = params['networkId'];
      this.appService.networkChanges(this.networkId).subscribe(response => {
        // TODO this.subset = response.result.network.attributes.country + networkType
        this.subset = new Subset(new Country("nl"), new NetworkType("rwn"));
        this.response = response;
      });
    });
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }

}
