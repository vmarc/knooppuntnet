import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Observable } from 'rxjs';
import { FilterOptions } from '@app/kpn/filter/filter-options';

@Injectable({
  providedIn: 'root',
})
export class NetworkNodesService {
  readonly filterOptions$: Observable<FilterOptions>;
  private readonly _filterOptions$ = new BehaviorSubject<FilterOptions>(
    FilterOptions.empty()
  );

  constructor() {
    this.filterOptions$ = this._filterOptions$.asObservable();
  }

  setFilterOptions(options: FilterOptions): void {
    this._filterOptions$.next(options);
  }
}
