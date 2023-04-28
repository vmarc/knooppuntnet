import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { FactInfo } from '@app/analysis/fact';
import { FactDescriptionComponent } from '@app/analysis/fact';
import { FactNameComponent } from '@app/analysis/fact';
import { ErrorComponent } from '@app/components/shared/error';
import { SubsetFact } from '@app/kpn/common';
import { Store } from '@ngrx/store';
import { SubsetPageHeaderBlockComponent } from '../components/subset-page-header-block.component';
import { actionSubsetFactDetailsPageInit } from '../store/subset.actions';
import { selectSubsetFact } from '../store/subset.selectors';
import { selectSubsetFactDetailsPage } from '../store/subset.selectors';
import { SubsetFactDetailsComponent } from './subset-fact-details.component';

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
        />
        <h2>
          <kpn-fact-name [fact]="subsetFact.factName" />
        </h2>
        <div class="fact-description">
          <kpn-fact-description [factInfo]="factInfo(subsetFact)" />
        </div>
      </div>

      <kpn-error />

      <div *ngIf="response$ | async as response">
        <div *ngIf="response.result">
          <kpn-subset-fact-details [page]="response.result" />
        </div>
      </div>
    </div>
  `,
  styleUrls: ['./_subset-fact-details-page.component.scss'],
  standalone: true,
  imports: [
    NgIf,
    SubsetPageHeaderBlockComponent,
    FactNameComponent,
    FactDescriptionComponent,
    ErrorComponent,
    SubsetFactDetailsComponent,
    AsyncPipe,
  ],
})
export class SubsetFactDetailsPageComponent implements OnInit {
  readonly subsetFact$ = this.store.select(selectSubsetFact);
  readonly response$ = this.store.select(selectSubsetFactDetailsPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionSubsetFactDetailsPageInit());
  }

  factInfo(subsetFact: SubsetFact): FactInfo {
    return new FactInfo(subsetFact.factName);
  }
}
