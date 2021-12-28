import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { filter } from 'rxjs/operators';
import { AppState } from '../../../core/core.state';
import { ChangeOption } from '../../changes/store/changes.actions';
import { actionNetworkChangesFilterOption } from '../store/network.actions';
import { selectNetworkChangesFilterOptions } from '../store/network.selectors';

@Component({
  selector: 'kpn-network-changes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-change-filter
        [filterOptions]="filterOptions$ | async"
        (changeOption)="changed($event)"
      ></kpn-change-filter>
    </kpn-sidebar>
  `,
})
export class NetworkChangesSidebarComponent {
  filterOptions$ = this.store
    .select(selectNetworkChangesFilterOptions)
    .pipe(filter((filterOptions) => !!filterOptions));

  constructor(private store: Store<AppState>) {}

  changed(option: ChangeOption): void {
    this.store.dispatch(actionNetworkChangesFilterOption({ option }));
  }
}
