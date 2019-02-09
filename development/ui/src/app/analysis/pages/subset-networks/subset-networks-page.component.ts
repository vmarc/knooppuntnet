import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {AppService} from "../../../app.service";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {SubsetNetworksPage} from "../../../kpn/shared/subset/subset-networks-page";
import {Util} from "../../../components/shared/util";
import {Subset} from "../../../kpn/shared/subset";
import {PageService} from "../../../components/shared/page.service";
import {NetworkCacheService} from "../../../services/network-cache.service";

@Component({
  selector: 'kpn-subset-networks-page',
  template: `

    <div>
      <a routerLink="/">Home</a> >
      <a routerLink="/analysis">Analysis</a> >

      <a routerLink="/analysis/nl" *ngIf="subset.country.domain === 'nl'">The Netherlands</a>
      <a routerLink="/analysis/be" *ngIf="subset.country.domain === 'be'">Belgium</a>
      <a routerLink="/analysis/de" *ngIf="subset.country.domain === 'de'">Germany</a> >

      <span *ngIf="subset.networkType.name === 'rcn'">Cycling</span>
      <span *ngIf="subset.networkType.name === 'rwn'">Hiking</span>
      <span *ngIf="subset.networkType.name === 'rhn'">Horse</span>
      <span *ngIf="subset.networkType.name === 'rmn'">Motorboat</span>
      <span *ngIf="subset.networkType.name === 'rpn'">Canoe</span>
      <span *ngIf="subset.networkType.name === 'rin'">Inline skating</span>
    </div>
    
    <h1>
      <kpn-subset-name [subset]="subset"></kpn-subset-name>
    </h1>

    <div>
      <div class="kpn-thick">Networks</div> | 
      <a routerLink="/">Facts</a> |
      <a routerLink="/analysis">Orphan Nodes</a> |
      <a routerLink="/analysis">Orphan Routes</a> |
      <a routerLink="/analysis">History</a>
    </div>
    <br/>
    
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
  `
})
export class SubsetNetworksPageComponent implements OnInit, OnDestroy {

  subset: Subset;
  response: ApiResponse<SubsetNetworksPage>;
  paramsSubscription: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService,
              private networkCacheService: NetworkCacheService) {
  }

  ngOnInit() {
    this.pageService.initSubsetPage();
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      this.subset = Util.subsetInRoute(params);
      this.pageService.subset = this.subset;
      this.response = null;
      this.appService.subsetNetworks(this.subset).subscribe(response => {
        this.response = response;
        response.result.networks.forEach(networkAttributes => {
          this.networkCacheService.setNetworkName(networkAttributes.id.toString(), networkAttributes.name);
        });
      });
    });
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }

}
