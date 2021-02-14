import {ChangeDetectionStrategy} from '@angular/core';
import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {SubsetFactsPage} from '@api/common/subset/subset-facts-page';
import {SubsetInfo} from '@api/common/subset/subset-info';
import {ApiResponse} from '@api/custom/api-response';
import {Fact} from '@api/custom/fact';
import {Subset} from '@api/custom/subset';
import {Observable} from 'rxjs';
import {BehaviorSubject} from 'rxjs';
import {mergeMap} from 'rxjs/operators';
import {tap} from 'rxjs/operators';
import {map} from 'rxjs/operators';
import {AppService} from '../../../app.service';
import {Util} from '../../../components/shared/util';
import {Subsets} from '../../../kpn/common/subsets';
import {SubsetCacheService} from '../../../services/subset-cache.service';
import {FactLevel} from '../../fact/fact-level';
import {Facts} from '../../fact/facts';

@Component({
  selector: 'kpn-subset-facts-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <kpn-subset-page-header-block
      [subset]="subset$ | async"
      [subsetInfo$]="subsetInfo$"
      pageName="facts"
      pageTitle="Facts"
      i18n-pageTitle="@@subset-facts.title">
    </kpn-subset-page-header-block>

    <kpn-error></kpn-error>

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
            <a [routerLink]="factCount.fact">
              <kpn-fact-name [fact]="factCount.fact"></kpn-fact-name>
            </a>
            ({{factCount.count}})
            <kpn-fact-level [factLevel]="factLevel(factCount.fact)"></kpn-fact-level>
            <kpn-fact-description [factName]="factCount.fact"></kpn-fact-description>
          </kpn-item>
        </kpn-items>
      </div>
    </div>
  `
})
export class SubsetFactsPageComponent implements OnInit {

  subset$: Observable<Subset>;
  subsetInfo$ = new BehaviorSubject<SubsetInfo>(null);
  response$: Observable<ApiResponse<SubsetFactsPage>>;

  hasFacts: boolean;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private subsetCacheService: SubsetCacheService) {
  }

  ngOnInit(): void {
    this.subset$ = this.activatedRoute.params.pipe(
      map(params => Util.subsetInRoute(params)),
      tap(subset => this.subsetInfo$.next(this.subsetCacheService.getSubsetInfo(Subsets.key(subset))))
    );
    this.response$ = this.subset$.pipe(
      mergeMap(subset => this.appService.subsetFacts(subset).pipe(
        tap(response => {
          if (response.result) {
            this.hasFacts = response.result && response.result.subsetInfo.factCount > 0;
            this.subsetCacheService.setSubsetInfo(Subsets.key(subset), response.result.subsetInfo);
            this.subsetInfo$.next(response.result.subsetInfo);
          }
        })
      ))
    );
  }

  factLevel(fact: Fact): FactLevel {
    return Facts.factLevels.get(fact);
  }

}
