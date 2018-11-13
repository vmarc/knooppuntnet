import {Component, OnDestroy, OnInit} from '@angular/core';
import {AppService} from "../../../../app.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {NetworkFactsPage} from "../../../../kpn/shared/network/network-facts-page";
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {Subset} from "../../../../kpn/shared/subset";
import {Country} from "../../../../kpn/shared/country";
import {NetworkType} from "../../../../kpn/shared/network-type";

@Component({
  selector: 'kpn-network-facts-page',
  templateUrl: './network-facts-page.component.html',
  styleUrls: ['./network-facts-page.component.scss']
})
export class NetworkFactsPageComponent implements OnInit, OnDestroy {

  subset: Subset;
  networkId: string;

  response: ApiResponse<NetworkFactsPage>;
  paramsSubscription: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      this.networkId = params['networkId'];
      // TODO this.subset = response.result.network.attributes.country + networkType
      this.subset = new Subset(new Country("nl"), new NetworkType("rwn"));
      this.appService.networkFacts(this.networkId).subscribe(response => {
        this.response = response;
      });
    });
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }

}
