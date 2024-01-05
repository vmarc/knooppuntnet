import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { ChangeFilterOptions } from './change-filter-options';

@Injectable({
  providedIn: 'root',
})
export class ChangesService {
  private readonly _filterOptions$ = new BehaviorSubject<ChangeFilterOptions>(
    ChangeFilterOptions.empty()
  );
  protected readonly filterOptions$ = this._filterOptions$.asObservable();

  setFilterOptions(options: ChangeFilterOptions): void {
    this._filterOptions$.next(options);
  }

  resetFilterOptions(): void {
    this.setFilterOptions(ChangeFilterOptions.empty());
  }
}
