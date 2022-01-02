import { Params } from '@angular/router';
import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';

export class QueryParams {
  constructor(private queryParams: Params) {}

  changesParameters(
    preferencesImpact: boolean,
    preferencesItemsPerPage: number
  ): ChangesParameters {
    let year: number;
    let month: number;
    let day: number;
    let itemsPerPage = 25;
    let pageIndex = 0;
    let impact = true;

    if (this.queryParams['year']) {
      year = +this.queryParams['year'];
    }
    if (this.queryParams['month']) {
      month = +this.queryParams['month'];
    }
    if (this.queryParams['day']) {
      day = +this.queryParams['day'];
    }
    if (this.queryParams['itemsPerPage']) {
      itemsPerPage = +this.queryParams['itemsPerPage'];
    } else {
      itemsPerPage = preferencesItemsPerPage;
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
      itemsPerPage,
      pageIndex,
      impact,
    };
  }
}
