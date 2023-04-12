import { Injectable } from '@angular/core';
import { FilterOptions } from '@app/kpn/filter';
import { BehaviorSubject } from 'rxjs';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class NetworkRoutesService {
  readonly filterOptions$: Observable<FilterOptions>;
  private readonly _filterOptions$ = new BehaviorSubject(FilterOptions.empty());

  constructor() {
    this.filterOptions$ = this._filterOptions$.asObservable();
  }

  setFilterOptions(options: FilterOptions): void {
    this._filterOptions$.next(options);
  }
}
