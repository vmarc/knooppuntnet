import { Component } from '@angular/core';
import { ChangeOption } from '@app/kpn/common';
import { Store } from '@ngrx/store';
import { filter } from 'rxjs/operators';
import { actionSubsetChangesFilterOption } from '../store/subset.actions';
import { selectSubsetChangesFilterOptions } from '../store/subset.selectors';

@Component({
  selector: 'kpn-subset-changes-sidebar',
  template: `
    <kpn-sidebar>
      <kpn-change-filter
        [filterOptions]="filterOptions$ | async"
        (optionSelected)="onOptionSelected($event)"
      />
    </kpn-sidebar>
  `,
})
export class SubsetChangesSidebarComponent {
  readonly filterOptions$ = this.store
    .select(selectSubsetChangesFilterOptions)
    .pipe(filter((filterOptions) => !!filterOptions));

  constructor(private store: Store) {}

  onOptionSelected(option: ChangeOption): void {
    this.store.dispatch(actionSubsetChangesFilterOption({ option }));
  }
}
