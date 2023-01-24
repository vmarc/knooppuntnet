import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { filter } from 'rxjs/operators';
import { AppState } from '../../../../core/core.state';
import { AnalysisStrategy } from '../../../../core/preferences/preferences.state';
import { actionChangesAnalysisStrategy } from '../../../changes/store/changes.actions';
import { actionChangesFilterOption } from '../../../changes/store/changes.actions';
import { ChangeOption } from '../../../changes/store/changes.actions';
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
