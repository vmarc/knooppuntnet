import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { filter } from 'rxjs/operators';
import { AppState } from '../../../core/core.state';
import { ChangeOption } from '../../changes/store/changes.actions';
import { actionRouteChangesFilterOption } from '../store/route.actions';
import { selectRouteChangesFilterOptions } from '../store/route.selectors';

@Component({
  selector: 'kpn-route-changes-sidebar',
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
export class RouteChangesSidebarComponent {
  filterOptions$ = this.store
    .select(selectRouteChangesFilterOptions)
    .pipe(filter((filterOptions) => !!filterOptions));

  constructor(private store: Store<AppState>) {}

  onOptionSelected(option: ChangeOption): void {
    this.store.dispatch(actionRouteChangesFilterOption({ option }));
  }
}
