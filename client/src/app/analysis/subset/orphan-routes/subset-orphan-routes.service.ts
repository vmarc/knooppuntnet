import { Injectable } from '@angular/core';
import { ReplaySubject } from 'rxjs';
import { FilterOptions } from '../../../kpn/filter/filter-options';

@Injectable()
export class SubsetOrphanRoutesService {
  readonly filterOptions$: ReplaySubject<FilterOptions>;

  constructor() {
    this.filterOptions$ = new ReplaySubject(1);
    this.filterOptions$.next(FilterOptions.empty());
  }
}
