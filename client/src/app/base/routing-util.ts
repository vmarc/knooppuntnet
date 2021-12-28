import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { SerializedRouterStateSnapshot } from '@ngrx/router-store';
import { RouterNavigationPayload } from '@ngrx/router-store';
import { Subsets } from '../kpn/common/subsets';

export class RoutingUtil {
  constructor(
    private action: {
      payload: RouterNavigationPayload<SerializedRouterStateSnapshot>;
    }
  ) {}

  isChangesPage(): boolean {
    return this.url().includes('/analysis/changes');
  }

  isNetworkChangesPage(): boolean {
    return (
      this.url().includes('/analysis/network/') &&
      this.url().includes('/changes')
    );
  }

  isSubsetChangesPage(): boolean {
    let subsetChangePage = false;
    Subsets.all.forEach((subset) => {
      const url = `/analysis/${subset.networkType}/${subset.country}/changes`;
      if (this.url().includes(url)) {
        subsetChangePage = true;
      }
    });
    return subsetChangePage;
  }

  changesParameters(): ChangesParameters {
    const queryParams = this.action.payload.routerState.root.queryParams;

    let year: number;
    let month: number;
    let day: number;
    let itemsPerPage = 25;
    let pageIndex = 0;
    let impact = true;

    if (queryParams['year']) {
      year = +queryParams['year'];
    }
    if (queryParams['month']) {
      month = +queryParams['month'];
    }
    if (queryParams['day']) {
      day = +queryParams['day'];
    }
    if (queryParams['itemsPerPage']) {
      itemsPerPage = +queryParams['itemsPerPage'];
    }
    if (queryParams['pageIndex']) {
      pageIndex = +queryParams['pageIndex'];
    }
    if (queryParams['impact']) {
      impact = queryParams['impact'];
    }

    return {
      year,
      month,
      day,
      itemsPerPage,
      pageIndex,
      impact,
    };
  }

  private url(): string {
    return this.action.payload.routerState.url;
  }
}
