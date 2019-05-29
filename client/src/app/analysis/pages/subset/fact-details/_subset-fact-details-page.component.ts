import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../../../../app.service";
import {PageService} from "../../../../components/shared/page.service";
import {Util} from "../../../../components/shared/util";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {Subset} from "../../../../kpn/shared/subset";
import {SubsetFactDetailsPage} from "../../../../kpn/shared/subset/subset-fact-details-page";
import {SubsetCacheService} from "../../../../services/subset-cache.service";
import {Subscriptions} from "../../../../util/Subscriptions";

@Component({
  selector: "kpn-subset-fact-details-page",
  template: `
    <kpn-subset-page-header [subset]="subset" pageName="facts"></kpn-subset-page-header>

    <h2>
      <kpn-fact-name [factName]="factName"></kpn-fact-name>
    </h2>

    <p>
      <kpn-fact-description [factName]="factName"></kpn-fact-description>
    </p>

    <div *ngIf="response">
      <p>
        <kpn-situation-on [timestamp]="response.situationOn"></kpn-situation-on>
      </p>
      <div *ngIf="!hasFacts()">
        <i>No facts</i>
      </div>
      <div *ngIf="hasFacts()">
        <p>
          {{routeCount()}} routes in {{response.result.networks.size}} networks.
        </p>

        <kpn-items>
          <kpn-item *ngFor="let networkFactRefs of response.result.networks; let i=index" index="{{i}}">
            <a [routerLink]="'/analysis/network-details/' + networkFactRefs.networkId">
              {{networkFactRefs.networkName}}
            </a>
            <br/>
            {{networkFactRefs.factRefs.size}} routes:
            <br/>
            <span *ngFor="let ref of networkFactRefs.factRefs">
              <a [routerLink]="'/analysis/route/' + ref.id">
                {{ref.name}}
              </a>
            </span>
          </kpn-item>
        </kpn-items>
      </div>
      <json [object]="response"></json>
    </div>
  `
})
export class SubsetFactDetailsPageComponent implements OnInit, OnDestroy {

  private readonly subscriptions = new Subscriptions();

  subset: Subset;
  factName: String;
  response: ApiResponse<SubsetFactDetailsPage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService,
              private subsetCacheService: SubsetCacheService) {
  }

  ngOnInit() {
    this.pageService.initSubsetPage();
    this.subscriptions.add(this.activatedRoute.params.subscribe(params => {
      this.subset = Util.subsetInRoute(params);
      this.factName = params["fact"];
      this.pageService.subset = this.subset;
      this.response = null;
      this.subscriptions.add(this.appService.subsetFactDetails(this.subset).subscribe(response => {
        this.response = response;
        this.subsetCacheService.setSubsetInfo(this.subset.key(), this.response.result.subsetInfo)
      }));
    }));
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  hasFacts() {
    return this.response && this.response.result && this.response.result.networks.size > 0;
  }

  routeCount(): number {
    return this.response.result.networks.map(n => n.factRefs.size).reduce((sum, current) => sum + current);
  }

}
