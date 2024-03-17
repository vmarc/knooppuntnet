import { Injectable } from '@angular/core';
import { FilterOptions } from '@app/kpn/filter';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class NetworkRoutesService {
  private readonly _filterOptions$ = new BehaviorSubject(FilterOptions.empty());
  public readonly filterOptions$ = this._filterOptions$.asObservable();

  setFilterOptions(options: FilterOptions): void {
    this._filterOptions$.next(options);
  }
}
