import {ChangeDetectionStrategy} from "@angular/core";
import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs";
import {flatMap} from "rxjs/operators";
import {tap} from "rxjs/operators";
import {map} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {Util} from "../../../components/shared/util";
import {SubsetFactsPage} from "../../../kpn/api/common/subset/subset-facts-page";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {Fact} from "../../../kpn/api/custom/fact";
import {Subset} from "../../../kpn/api/custom/subset";
import {SubsetCacheService} from "../../../services/subset-cache.service";
import {FactLevel} from "../../fact/fact-level";
import {Facts} from "../../fact/facts";

@Component({
  selector: "kpn-subset-facts-page",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <kpn-subset-page-header-block
      [subset]="subset$ | async"
      pageName="facts"
      pageTitle="Facts"
      i18n-pageTitle="@@subset-facts.title">
    </kpn-subset-page-header-block>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <p>
        <kpn-situation-on [timestamp]="response.situationOn"></kpn-situation-on>
      </p>
      <p *ngIf="!hasFacts" class="kpn-line">
        <span i18n="@@subset-facts.no-facts">No facts</span>
        <kpn-icon-happy></kpn-icon-happy>
      </p>
      <div *ngIf="hasFacts" class="kpn-line">
        <kpn-items>
          <kpn-item *ngFor="let factCount of response.result.factCounts; let i=index" [index]="i">
            <a [routerLink]="factCount.fact.name">
              <kpn-fact-name [factName]="factCount.fact.name"></kpn-fact-name>
            </a>
            ({{factCount.count}})
            <kpn-fact-level [factLevel]="factLevel(factCount.fact)"></kpn-fact-level>
            <kpn-fact-description [factName]="factCount.fact.name"></kpn-fact-description>
          </kpn-item>
        </kpn-items>
      </div>
    </div>
  `
})
export class SubsetFactsPageComponent implements OnInit {

  subset$: Observable<Subset>;
  response$: Observable<ApiResponse<SubsetFactsPage>>;

  hasFacts: boolean;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private subsetCacheService: SubsetCacheService) {
  }

  ngOnInit(): void {
    this.subset$ = this.activatedRoute.params.pipe(map(params => Util.subsetInRoute(params)));
    this.response$ = this.subset$.pipe(
      flatMap(subset => this.appService.subsetFacts(subset).pipe(
        tap(response => {
          this.hasFacts = response.result && response.result.subsetInfo.factCount > 0;
          this.subsetCacheService.setSubsetInfo(subset.key(), response.result.subsetInfo);
        })
      ))
    );
  }

  factLevel(fact: Fact): FactLevel {
    return Facts.factLevels.get(fact.name);
  }

}
