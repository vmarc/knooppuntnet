import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {flatMap, map, tap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {PageService} from "../../../components/shared/page.service";
import {Util} from "../../../components/shared/util";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {Subset} from "../../../kpn/api/custom/subset";
import {SubsetCacheService} from "../../../services/subset-cache.service";
import {Subscriptions} from "../../../util/Subscriptions";
import {FactCount} from "../../../kpn/api/common/fact-count";
import {SubsetFactsPage} from "../../../kpn/api/common/subset/subset-facts-page";

@Component({
  selector: "kpn-subset-facts-page",
  template: `

    <kpn-subset-page-header-block
      [subset]="subset"
      pageName="facts"
      pageTitle="Facts"
      i18n-pageTitle="@@subset-facts.title">
    </kpn-subset-page-header-block>

    <div *ngIf="response">
      <div *ngIf="!hasFacts()" class="kpn-line">
        <kpn-icon-happy></kpn-icon-happy>
        <span i18n="@@subset-facts.no-facts">No facts</span>
      </div>
      <div *ngIf="hasFacts()">
        <p>
          <kpn-situation-on [timestamp]="response.situationOn"></kpn-situation-on>
        </p>
        <kpn-items>
          <kpn-item *ngFor="let factCount of response.result.factCounts; let i=index" [index]="i">
            <a [routerLink]="factDetailLink(factCount)">
              <kpn-fact-name [factName]="factCount.fact.name"></kpn-fact-name>
            </a>
            ({{factCount.count}})
            <kpn-fact-description [factName]="factCount.fact.name"></kpn-fact-description>
          </kpn-item>
        </kpn-items>
      </div>
      <kpn-json [object]="response"></kpn-json>
    </div>
  `
})
export class SubsetFactsPageComponent implements OnInit, OnDestroy {

  subset: Subset;
  response: ApiResponse<SubsetFactsPage>;
  private readonly subscriptions = new Subscriptions();

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService,
              private subsetCacheService: SubsetCacheService) {
  }

  ngOnInit(): void {
    this.subscriptions.add(
      this.activatedRoute.params.pipe(
        map(params => Util.subsetInRoute(params)),
        tap(subset => this.subset = subset),
        flatMap(subset => this.appService.subsetFacts(subset))
      ).subscribe(response => this.processResponse(response))
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  hasFacts() {
    return this.response && this.response.result && this.response.result.subsetInfo.factCount > 0;
  }

  factDetailLink(factCount: FactCount): string {
    return "/analysis/" + this.subset.key() + "/" + factCount.fact.name;
  }

  private processResponse(response: ApiResponse<SubsetFactsPage>) {
    this.response = response;
    this.subsetCacheService.setSubsetInfo(this.subset.key(), this.response.result.subsetInfo);
  }

}
