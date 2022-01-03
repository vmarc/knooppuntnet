import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { filter } from 'rxjs/operators';
import { AppState } from '../../../core/core.state';
import { actionChangesFilterOption } from '../../changes/store/changes.actions';
import { ChangeOption } from '../../changes/store/changes.actions';
import { actionSubsetChangesFilterOption } from '../store/subset.actions';
import { selectSubsetChangesFilterOptions } from '../store/subset.selectors';

@Component({
  selector: 'kpn-subset-changes-sidebar',
  template: `
    <kpn-sidebar>
      <kpn-subset-analysis-strategy></kpn-subset-analysis-strategy>
      <kpn-change-filter
        [filterOptions]="filterOptions$ | async"
        (changeOption)="changed($event)"
      ></kpn-change-filter>
    </kpn-sidebar>
  `,
})
export class SubsetChangesSidebarComponent {
  readonly filterOptions$ = this.store
    .select(selectSubsetChangesFilterOptions)
    .pipe(filter((filterOptions) => !!filterOptions));

  constructor(private store: Store<AppState>) {}

  changed(option: ChangeOption): void {
    this.store.dispatch(actionSubsetChangesFilterOption({ option }));
  }
}
