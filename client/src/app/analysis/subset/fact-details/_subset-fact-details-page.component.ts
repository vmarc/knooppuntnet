import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute, Params} from "@angular/router";
import {AppService} from "../../../app.service";
import {PageService} from "../../../components/shared/page.service";
import {Util} from "../../../components/shared/util";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {Subset} from "../../../kpn/shared/subset";
import {SubsetFactDetailsPage} from "../../../kpn/shared/subset/subset-fact-details-page";
import {SubsetCacheService} from "../../../services/subset-cache.service";
import {Subscriptions} from "../../../util/Subscriptions";
import {flatMap, map} from "rxjs/operators";

class SubsetFact {
  constructor(readonly subset: Subset,
              readonly factName: string) {
  }
}

@Component({
  selector: "kpn-subset-fact-details-page",
  template: `

    <kpn-subset-page-header-block
        [subset]="subset"
        pageName="facts"
        pageTitle="Facts"
        i18n-pageTitle="@@subset-facts.title">
    </kpn-subset-page-header-block>

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
            <a [routerLink]="'/analysis/network/' + networkFactRefs.networkId">
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
      <kpn-json [object]="response"></kpn-json>
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

    this.subscriptions.add(
      this.activatedRoute.params.pipe(
        map(params => this.interpreteParams(params)),
        flatMap(subsetFact => this.appService.subsetFactDetails(subsetFact.subset /* TODO: add parameter, subsetFact.factName*/))
      ).subscribe(response => this.processResponse(response))
    );
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

  private interpreteParams(params: Params): SubsetFact {
    const subset = Util.subsetInRoute(params);
    const factName = params["fact"];
    this.subset = subset;
    this.factName = factName;
    this.pageService.subset = subset;
    return new SubsetFact(subset, factName);
  }

  private processResponse(response: ApiResponse<SubsetFactDetailsPage>) {
    this.response = response;
    this.subsetCacheService.setSubsetInfo(this.subset.key(), this.response.result.subsetInfo)
  }

}
