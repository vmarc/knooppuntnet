import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { LocationRoutesPage } from '@api/common/location/location-routes-page';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { AppState } from '../../../core/core.state';
import { actionLocationRoutesPageIndex } from '../store/location.actions';
import { selectLocationKey } from '../store/location.selectors';
import { selectLocationRoutesPageIndex } from '../store/location.selectors';

@Component({
  selector: 'kpn-location-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div
      *ngIf="page.routes.length === 0"
      class="kpn-spacer-above"
      i18n="@@location-routes.no-routes"
    >
      No routes
    </div>
    <kpn-location-route-table
      *ngIf="page.routes.length > 0"
      [pageIndex]="pageIndex$ | async"
      (page)="pageChanged($event)"
      [networkType]="networkType$ | async"
      [timeInfo]="page.timeInfo"
      [routes]="page.routes"
      [routeCount]="page.routeCount"
    >
    </kpn-location-route-table>
  `,
})
export class LocationRoutesComponent {
  @Input() page: LocationRoutesPage;

  readonly networkType$ = this.store
    .select(selectLocationKey)
    .pipe(map((key) => key.networkType));
  readonly pageIndex$ = this.store.select(selectLocationRoutesPageIndex);

  constructor(private store: Store<AppState>) {}

  pageChanged(pageIndex: number): void {
    window.scroll(0, 0);
    this.store.dispatch(actionLocationRoutesPageIndex({ pageIndex }));
  }
}
