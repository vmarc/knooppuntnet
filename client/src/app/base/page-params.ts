import { Params } from '@angular/router';
import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { Subset } from '@api/custom/subset';
import { SubsetFact } from '../analysis/subset/store/subset.state';
import { AnalysisStrategy } from '../core/preferences/preferences.state';
import { Countries } from '../kpn/common/countries';
import { NetworkTypes } from '../kpn/common/network-types';

export class PageParams {
  constructor(
    private routeParams: Params = [],
    private queryParams: Params = []
  ) {}

  networkId(): number {
    return +this.routeParams['networkId'];
  }

  routeId(): string {
    return this.routeParams['routeId'];
  }

  strategy(defaultStrategy: AnalysisStrategy): AnalysisStrategy {
    let strategy = defaultStrategy;
    if (this.queryParams['strategy']) {
      strategy = this.queryParams['strategy'];
    }
    return strategy;
  }

  changesParameters(
    preferencesImpact: boolean,
    preferencesPageSize: number
  ): ChangesParameters {
    let year: number;
    let month: number;
    let day: number;
    let pageSize: number;
    let pageIndex = 0;
    let impact: boolean;

    if (this.queryParams['year']) {
      year = +this.queryParams['year'];
    }
    if (this.queryParams['month']) {
      month = +this.queryParams['month'];
    }
    if (this.queryParams['day']) {
      day = +this.queryParams['day'];
    }
    if (this.queryParams['pageSize']) {
      pageSize = +this.queryParams['pageSize'];
    } else {
      pageSize = preferencesPageSize;
    }
    if (this.queryParams['pageIndex']) {
      pageIndex = +this.queryParams['pageIndex'];
    }
    if (this.queryParams['impact']) {
      impact = this.queryParams['impact'] === 'true';
    } else {
      impact = preferencesImpact;
    }

    return {
      year,
      month,
      day,
      pageSize,
      pageIndex,
      impact,
    };
  }

  subset(): Subset {
    const country = Countries.withDomain(this.routeParams['country']);
    const networkType = NetworkTypes.withName(this.routeParams['networkType']);
    return { country, networkType };
  }

  subsetFact(): SubsetFact {
    const subset = this.subset();
    const factName = this.routeParams['fact'];
    return new SubsetFact(subset, factName);
  }
}
