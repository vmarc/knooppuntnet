import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Params } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { Subset } from '@api/custom/subset';
import { RouteType } from '@api/common/route-type';
import { Countries } from '@app/shared/kpn/common/countries';
import { RouteTypes } from '@app/shared/kpn/common/route-types';

@Injectable()
export class RouterService {
  private readonly router = inject(Router);
  private readonly activatedRoute = inject(ActivatedRoute);

  fragment(): string {
    return this.activatedRoute.snapshot.fragment;
  }

  params(): Params {
    return this.activatedRoute.snapshot.params;
  }

  queryParams(): Params {
    return this.activatedRoute.snapshot.queryParams;
  }

  param(name: string): string {
    return this.activatedRoute.snapshot.paramMap.get(name);
  }

  queryParam(name: string): string {
    return this.activatedRoute.snapshot.queryParamMap.get(name);
  }

  paramRouteType(): RouteType {
    return RouteTypes.withName(this.param('routeType'));
  }

  paramCountry() {
    return Countries.withDomain(this.param('country'));
  }

  paramSubset(): Subset {
    return { country: this.paramCountry(), routeType: this.paramRouteType() };
  }

  urlLayerIds(): string[] {
    const layersParam = this.queryParam('layers');
    if (layersParam) {
      return layersParam.split(',');
    }
    return [];
  }

  updateQueryParams(queryParams: Params): Promise<boolean> {
    return this.router.navigate([], {
      relativeTo: this.activatedRoute,
      queryParamsHandling: 'merge',
      queryParams,
    });
  }
}
