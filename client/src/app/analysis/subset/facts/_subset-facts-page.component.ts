import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { FactCount } from '@api/common/fact-count';
import { SubsetFactsPage } from '@api/common/subset/subset-facts-page';
import { ApiResponse } from '@api/custom/api-response';
import { Fact } from '@api/custom/fact';
import { Store } from '@ngrx/store';
import { FactInfo } from '../../fact/fact-info';
import { FactLevel } from '../../fact/fact-level';
import { Facts } from '../../fact/facts';
import { actionSubsetFactRefsLoad } from '../store/subset.actions';
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
    />

    <kpn-error />

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <p>
        <kpn-situation-on [timestamp]="response.situationOn" />
      </p>
      <p *ngIf="!hasFacts(response)" class="kpn-line">
        <span i18n="@@subset-facts.no-facts">No facts</span>
        <kpn-icon-happy />
      </p>
      <div *ngIf="hasFacts(response)" class="kpn-line">
        <kpn-items>
          <kpn-item
            *ngFor="let factCount of response.result.factCounts; let i = index"
            [index]="i"
          >
            <div class="kpn-line">
              <a [routerLink]="factCount.fact">
                <kpn-fact-name [fact]="factCount.fact" />
              </a>
              <span>({{ factCount.count }})</span>
              <kpn-fact-level [factLevel]="factLevel(factCount.fact)" />
              <a
                rel="nofollow"
                (click)="edit(factCount.fact)"
                title="Open in editor (like JOSM)"
                i18n-title="@@edit.link.title"
                i18n="@@edit.link"
              >edit</a
              >
            </div>
            <kpn-fact-description [factInfo]="factInfo(factCount)" />
          </kpn-item>
        </kpn-items>
      </div>
    </div>
  `,
})
export class SubsetFactsPageComponent implements OnInit {
  readonly response$ = this.store.select(selectSubsetFactsPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionSubsetFactsPageInit());
  }

  hasFacts(response: ApiResponse<SubsetFactsPage>): boolean {
    return response.result && response.result.subsetInfo.factCount > 0;
  }

  factLevel(fact: Fact): FactLevel {
    return Facts.factLevels.get(fact);
  }

  edit(fact: Fact): void {
    this.store.dispatch(actionSubsetFactRefsLoad({ fact }));
  }

  factInfo(factCount: FactCount): FactInfo {
    return new FactInfo(factCount.fact);
  }
}
