import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { Country } from '@api/common/country';
import { RouteType } from '@api/common/route-type';
import { SubsetInfo } from '@api/common/subset/subset-info';
import { Subset } from '@api/custom/subset';
import { SubsetPageName } from '@app/analysis/subset/internal/components/subset-page-name';

@Injectable({
  providedIn: 'root',
})
export class SubsetService {
  private readonly _subset = signal<Subset>(null);
  private readonly _subsetInfo = signal<SubsetInfo>(null);
  private readonly _pageName = signal<SubsetPageName>(null);

  readonly subset = this._subset.asReadonly();
  readonly subsetInfo = this._subsetInfo.asReadonly();
  readonly pageName = this._pageName.asReadonly();

  onInit(country: Country, routeType: RouteType): void {
    const oldSubset = this.subset();
    if (!oldSubset || oldSubset.country !== country || oldSubset.routeType !== routeType) {
      this._subset.set({ country, routeType });
      this._subsetInfo.set(null);
    }
  }

  setPageName(pageName: SubsetPageName): void {
    this._pageName.set(pageName);
  }

  setSubsetInfo(subsetInfo: SubsetInfo): void {
    this._subsetInfo.set(subsetInfo);
  }
}
