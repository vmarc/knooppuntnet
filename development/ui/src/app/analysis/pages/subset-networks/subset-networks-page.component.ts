import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {AppService} from "../../../app.service";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {SubsetNetworksPage} from "../../../kpn/shared/subset/subset-networks-page";
import {Util} from "../../../shared/util";
import {Subset} from "../../../kpn/shared/subset";

@Component({
  selector: 'kpn-subset-networks-page',
  template: `
    <kpn-page>
      <kpn-toolbar toolbar></kpn-toolbar>
      <kpn-subset-sidenav sidenav [subset]="subset"></kpn-subset-sidenav>
      <div content>
        <h1>
          Subset networks
        </h1>

        <kpn-subset-network-list
          *ngIf="response"
          [networks]="response.result.networks">
        </kpn-subset-network-list>

        <br/>
        <br/>
        <br/>

        <kpn-subset-network-table
          *ngIf="response"
          [networks]="response.result.networks">
        </kpn-subset-network-table>

        <div *ngIf="response">
          <json [object]="response"></json>
        </div>
      </div>
    </kpn-page>
  `
})
export class SubsetNetworksPageComponent implements OnInit, OnDestroy {

  subset: Subset;
  response: ApiResponse<SubsetNetworksPage>;
  paramsSubscription: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      this.subset = Util.subsetInRoute(params);
      this.response = null;
      this.appService.subsetNetworks(this.subset).subscribe(response => {
        this.response = response;
      });
    });
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }

}
