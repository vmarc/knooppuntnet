import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ChangeOption } from '@app/kpn/common';
import { Store } from '@ngrx/store';
import { filter } from 'rxjs/operators';
import { actionNodeChangesFilterOption } from '../store/node.actions';
import { selectNodeChangesFilterOptions } from '../store/node.selectors';

@Component({
  selector: 'kpn-node-changes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-change-filter
        [filterOptions]="filterOptions$ | async"
        (optionSelected)="onOptionSelected($event)"
      />
    </kpn-sidebar>
  `,
})
export class NodeChangesSidebarComponent {
  filterOptions$ = this.store
    .select(selectNodeChangesFilterOptions)
    .pipe(filter((filterOptions) => !!filterOptions));

  constructor(private store: Store) {}

  onOptionSelected(option: ChangeOption): void {
    this.store.dispatch(actionNodeChangesFilterOption({ option }));
  }
}
