import { Injectable } from '@angular/core';
import { FilterOptions } from '@app/kpn/filter';
import { ReplaySubject } from 'rxjs';

@Injectable()
export class SubsetOrphanRoutesService {
  readonly filterOptions$: ReplaySubject<FilterOptions>;

  constructor() {
    this.filterOptions$ = new ReplaySubject(1);
    this.filterOptions$.next(FilterOptions.empty());
  }
}
