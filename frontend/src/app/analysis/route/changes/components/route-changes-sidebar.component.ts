import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ChangeFilterComponent } from '@app/analysis/components/changes/filter';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { ChangeOption } from '@app/kpn/common';
import { Store } from '@ngrx/store';
import { filter } from 'rxjs/operators';
import { actionRouteChangesFilterOption } from '../../store/route.actions';
import { selectRouteChangesFilterOptions } from '../../store/route.selectors';

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
  standalone: true,
  imports: [SidebarComponent, ChangeFilterComponent, AsyncPipe],
})
export class RouteChangesSidebarComponent {
  private readonly store = inject(Store);
  protected readonly filterOptions$ = this.store
    .select(selectRouteChangesFilterOptions)
    .pipe(filter((filterOptions) => !!filterOptions));

  onOptionSelected(option: ChangeOption): void {
    this.store.dispatch(actionRouteChangesFilterOption({ option }));
  }
}
