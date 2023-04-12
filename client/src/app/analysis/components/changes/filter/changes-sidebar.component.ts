import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { AnalysisStrategy } from '@app/core/preferences';
import { ChangeOption } from '@app/kpn/common';
import { Store } from '@ngrx/store';
import { filter } from 'rxjs/operators';
import { actionChangesAnalysisStrategy } from '../../../changes/store/changes.actions';
import { actionChangesFilterOption } from '../../../changes/store/changes.actions';
import { selectChangesFilterOptions } from '../../../changes/store/changes.selectors';

@Component({
  selector: 'kpn-changes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-analysis-strategy (strategyChange)="onStrategyChange($event)" />
      <kpn-change-filter
        [filterOptions]="filterOptions$ | async"
        (optionSelected)="onOptionSelected($event)"
      />
    </kpn-sidebar>
  `,
})
export class ChangesSidebarComponent {
  filterOptions$ = this.store
    .select(selectChangesFilterOptions)
    .pipe(filter((filterOptions) => !!filterOptions));

  constructor(private store: Store) {}

  onOptionSelected(option: ChangeOption): void {
    this.store.dispatch(actionChangesFilterOption({ option }));
  }

  onStrategyChange(strategy: AnalysisStrategy) {
    this.store.dispatch(actionChangesAnalysisStrategy({ strategy }));
  }
}
