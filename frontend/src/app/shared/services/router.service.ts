import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { Params } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { Subset } from '@api/custom';
import { NetworkType } from '@api/custom';
import { Countries } from '@app/kpn/common';
import { NetworkTypes } from '@app/kpn/common';

@Injectable()
export class RouterService {
  private activatedRoute = inject(ActivatedRoute);

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

  paramNetworkType(): NetworkType {
    return NetworkTypes.withName(this.param('networkType'));
  }

  paramCountry() {
    return Countries.withDomain(this.param('country'));
  }

  paramSubset(): Subset {
    return { country: this.paramCountry(), networkType: this.paramNetworkType() };
  }
}
