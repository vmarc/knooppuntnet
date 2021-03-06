import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { SubsetFactsPage } from '@api/common/subset/subset-facts-page';
import { ApiResponse } from '@api/custom/api-response';
import { Fact } from '@api/custom/fact';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { FactLevel } from '../../fact/fact-level';
import { Facts } from '../../fact/facts';
import { actionSubsetFactsPageInit } from '../store/subset.actions';
import { selectSubsetFactsPage } from '../store/subset.selectors';

@Component({
  selector: 'kpn-subset-facts-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-subset-page-header-block
      pageName="facts"
      pageTitle="Facts"
      i18n-pageTitle="@@subset-facts.title"
    ></kpn-subset-page-header-block>

    <kpn-error></kpn-error>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <p>
        <kpn-situation-on [timestamp]="response.situationOn"></kpn-situation-on>
      </p>
      <p *ngIf="!hasFacts(response)" class="kpn-line">
        <span i18n="@@subset-facts.no-facts">No facts</span>
        <kpn-icon-happy></kpn-icon-happy>
      </p>
      <div *ngIf="hasFacts(response)" class="kpn-line">
        <kpn-items>
          <kpn-item
            *ngFor="let factCount of response.result.factCounts; let i = index"
            [index]="i"
          >
            <a [routerLink]="factCount.fact">
              <kpn-fact-name [fact]="factCount.fact"></kpn-fact-name>
            </a>
            ({{ factCount.count }})
            <kpn-fact-level
              [factLevel]="factLevel(factCount.fact)"
            ></kpn-fact-level>
            <kpn-fact-description
              [factName]="factCount.fact"
            ></kpn-fact-description>
          </kpn-item>
        </kpn-items>
      </div>
    </div>
  `,
})
export class SubsetFactsPageComponent implements OnInit {
  readonly response$ = this.store.select(selectSubsetFactsPage);

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.store.dispatch(actionSubsetFactsPageInit());
  }

  hasFacts(response: ApiResponse<SubsetFactsPage>): boolean {
    return response.result && response.result.subsetInfo.factCount > 0;
  }

  factLevel(fact: Fact): FactLevel {
    return Facts.factLevels.get(fact);
  }
}
