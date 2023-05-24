import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ChangeFilterComponent } from '@app/analysis/components/changes/filter';
import { AnalysisStrategyComponent } from '@app/analysis/strategy';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { AnalysisStrategy } from '@app/core';
import { ChangeOption } from '@app/kpn/common';
import { Store } from '@ngrx/store';
import { filter } from 'rxjs/operators';
import { actionChangesAnalysisStrategy } from '../store/changes.actions';
import { actionChangesFilterOption } from '../store/changes.actions';
import { selectChangesFilterOptions } from '../store/changes.selectors';

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
  standalone: true,
  imports: [
    AnalysisStrategyComponent,
    AsyncPipe,
    ChangeFilterComponent,
    SidebarComponent,
  ],
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
