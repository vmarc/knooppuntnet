import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {AppService} from "../../../app.service";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {NetworkRoutesPage} from "../../../kpn/shared/network/network-routes-page";
import {Subset} from "../../../kpn/shared/subset";
import {Country} from "../../../kpn/shared/country";
import {NetworkType} from "../../../kpn/shared/network-type";

@Component({
  selector: 'kpn-network-routes-page',
  template: `
    <kpn-page>
      <kpn-toolbar toolbar></kpn-toolbar>
      <kpn-network-sidenav sidenav [subset]="subset" [networkId]="networkId"></kpn-network-sidenav>
      <div content>
        <h1>
          Network routes
        </h1>
        <div *ngIf="response">
          <json [object]="response"></json>
        </div>
      </div>
    </kpn-page>
  `
})
export class NetworkRoutesPageComponent implements OnInit, OnDestroy {

  subset: Subset;
  networkId: string;

  response: ApiResponse<NetworkRoutesPage>;
  paramsSubscription: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      this.networkId = params['networkId'];
      // TODO this.subset = response.result.network.attributes.country + networkType
      this.subset = new Subset(new Country("nl"), new NetworkType("rwn"));
      this.appService.networkRoutes(this.networkId).subscribe(response => {
        this.response = response;
      });
    });
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }

}
