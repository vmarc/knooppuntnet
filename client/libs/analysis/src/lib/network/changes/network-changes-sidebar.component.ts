import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ChangeFilterComponent } from '@app/analysis/components/changes/filter';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { ChangeOption } from '@app/kpn/common';
import { Store } from '@ngrx/store';
import { filter } from 'rxjs/operators';
import { actionNetworkChangesFilterOption } from '../store/network.actions';
import { selectNetworkChangesFilterOptions } from '../store/network.selectors';

@Component({
  selector: 'kpn-network-changes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-change-filter
        [filterOptions]="filterOptions$ | async"
        (optionSelected)="onOptionSelected($event)"
      />
    </kpn-sidebar>
  `,
  standalone: true,
  imports: [SidebarComponent, ChangeFilterComponent, AsyncPipe],
})
export class NetworkChangesSidebarComponent {
  filterOptions$ = this.store
    .select(selectNetworkChangesFilterOptions)
    .pipe(filter((filterOptions) => !!filterOptions));

  constructor(private store: Store) {}

  onOptionSelected(option: ChangeOption): void {
    this.store.dispatch(actionNetworkChangesFilterOption({ option }));
  }
}
