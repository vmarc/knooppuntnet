import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { SubsetInfo } from '@api/common/subset/subset-info';
import { Subset } from '@api/custom/subset';
import { RouterService } from '@app/shared/services/router.service';

@Injectable({
  providedIn: 'root',
})
export class SubsetService {
  private readonly _subset = signal<Subset>(null);
  private readonly _subsetInfo = signal<SubsetInfo>(null);

  readonly subset = this._subset.asReadonly();
  readonly subsetInfo = this._subsetInfo.asReadonly();

  initPage(routerService: RouterService): void {
    const subset = routerService.paramSubset();
    const oldSubset = this.subset();
    if (
      !oldSubset ||
      oldSubset.country !== subset.country ||
      oldSubset.routeType !== subset.routeType
    ) {
      this._subset.set(subset);
      this._subsetInfo.set(null);
    }
  }

  setSubsetInfo(subsetInfo: SubsetInfo): void {
    this._subsetInfo.set(subsetInfo);
  }
}
