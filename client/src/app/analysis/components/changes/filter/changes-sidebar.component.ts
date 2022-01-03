import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { filter } from 'rxjs/operators';
import { AppState } from '../../../../core/core.state';
import { actionChangesFilterOption } from '../../../changes/store/changes.actions';
import { ChangeOption } from '../../../changes/store/changes.actions';
import { selectChangesFilterOptions } from '../../../changes/store/changes.selectors';

@Component({
  selector: 'kpn-changes-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-analysis-strategy></kpn-analysis-strategy>
      <kpn-change-filter
        [filterOptions]="filterOptions$ | async"
        (changeOption)="changed($event)"
      ></kpn-change-filter>
    </kpn-sidebar>
  `,
})
export class ChangesSidebarComponent {
  filterOptions$ = this.store
    .select(selectChangesFilterOptions)
    .pipe(filter((filterOptions) => !!filterOptions));

  constructor(private store: Store<AppState>) {}

  changed(option: ChangeOption): void {
    this.store.dispatch(actionChangesFilterOption({ option }));
  }
}
