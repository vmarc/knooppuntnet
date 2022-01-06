import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { actionSubsetFactDetailsPageInit } from '../store/subset.actions';
import { selectSubsetFact } from '../store/subset.selectors';
import { selectSubsetFactDetailsPage } from '../store/subset.selectors';

@Component({
  selector: 'kpn-subset-fact-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="subsetFact$ | async as subsetFact">
      <div>
        <kpn-subset-page-header-block
          pageName="facts"
          pageTitle="Facts"
          i18n-pageTitle="@@subset-facts.title"
        ></kpn-subset-page-header-block>
        <h2>
          <kpn-fact-name [fact]="subsetFact.factName"></kpn-fact-name>
        </h2>
        <div class="fact-description">
          <kpn-fact-description
            [factName]="subsetFact.factName"
          ></kpn-fact-description>
        </div>
      </div>

      <kpn-error></kpn-error>

      <div *ngIf="response$ | async as response">
        <div *ngIf="response.result">
          <kpn-subset-fact-details
            [page]="response.result"
          ></kpn-subset-fact-details>
        </div>
      </div>
    </div>
  `,
  styleUrls: ['./_subset-fact-details-page.component.scss'],
})
export class SubsetFactDetailsPageComponent implements OnInit {
  readonly subsetFact$ = this.store.select(selectSubsetFact);
  readonly response$ = this.store.select(selectSubsetFactDetailsPage);

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.store.dispatch(actionSubsetFactDetailsPageInit());
  }
}
