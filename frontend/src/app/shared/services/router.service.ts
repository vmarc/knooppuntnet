import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NetworkType } from '@api/custom';
import { Countries } from '@app/kpn/common';
import { NetworkTypes } from '@app/kpn/common';

@Injectable()
export class RouterService {
  private activatedRoute = inject(ActivatedRoute);

  param(name: string): string {
    return this.activatedRoute.snapshot.paramMap.get(name);
  }

  paramNetworkType(): NetworkType {
    return NetworkTypes.withName(this.param('networkType'));
  }

  paramCountry() {
    return Countries.withDomain(this.param('country'));
  }
}
