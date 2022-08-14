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

  language(): string | null {
    const url = this.action.payload.routerState.url;
    if (url.length >= 4) {
      const language = url.substring(1, 3);
      if (
        language === 'en' ||
        language === 'de' ||
        language === 'fr' ||
        language === 'nl'
      ) {
        console.log(`DEBUG LANGUAGE=${language}, url=${url}`);
        return language;
      }
    }
    console.log(`DEBUG LANGUAGE=null, url=${url}`);
    return null;
  }

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
    let pageSize = 25;
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
    if (queryParams['pageSize']) {
      pageSize = +queryParams['pageSize'];
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
      pageSize,
      pageIndex,
      impact,
    };
  }

  private url(): string {
    return this.action.payload.routerState.url;
  }
}
