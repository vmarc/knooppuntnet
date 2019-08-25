import {List} from "immutable";
import {Util} from "../../../../components/shared/util";
import {ChangesFilter} from "../../../../kpn/shared/changes/filter/changes-filter";
import {ChangesFilterPeriod} from "../../../../kpn/shared/changes/filter/changes-filter-period";
import {ChangesParameters} from "../../../../kpn/shared/changes/filter/changes-parameters";
import {ChangeFilterOption} from "./change-filter-option";

export class ChangeFilterOptions {

  constructor(readonly options: List<ChangeFilterOption>) {
  }

  public static empty(): ChangeFilterOptions {
    return new ChangeFilterOptions(List());
  }

  public static from(parameters: ChangesParameters, filter: ChangesFilter, update: (ChangesParameters) => void): ChangeFilterOptions {

    const all = this.buildAll(parameters, filter, update);

    const options = filter.periods.map(year => {
      const months = year.periods.map(month => {
        const days = month.periods.map(day => {
          return new ChangeFilterOption(
            "day",
            day,
            List(),
            () => update(this.updatedParameters(parameters, true, year.name, month.name, day.name)),
            () => update(this.updatedParameters(parameters, false, year.name, month.name, day.name))
          );
        });
        return new ChangeFilterOption(
          "month",
          month,
          days,
          () => update(this.updatedParameters(parameters, true, year.name, month.name)),
          () => update(this.updatedParameters(parameters, false, year.name, month.name))
        );
      });

      return new ChangeFilterOption(
        "year",
        year,
        months,
        () => update(this.updatedParameters(parameters, true, year.name)),
        () => update(this.updatedParameters(parameters, false, year.name))
      );
    });

    const flatOptions: Array<ChangeFilterOption> = [];
    flatOptions.push(all);
    options.forEach(year => {
      flatOptions.push(year);
      year.options.forEach(month => {
        flatOptions.push(month);
        month.options.forEach(day => {
          flatOptions.push(day);
        });
      });
    });

    return new ChangeFilterOptions(List(flatOptions));
  }

  private static updatedParameters(parameters: ChangesParameters, impact: boolean, year: string = null, month: string = null, day: string = null): ChangesParameters {
    return new ChangesParameters(
      parameters.subset,
      parameters.networkId,
      parameters.routeId,
      parameters.nodeId,
      year,
      month,
      day,
      parameters.itemsPerPage,
      parameters.pageIndex,
      impact
    );
  }

  private static buildAll(parameters: ChangesParameters, filter: ChangesFilter, update: (ChangesParameters) => void): ChangeFilterOption {

    const totalCount = Util.sum(filter.periods.map(period => period.totalCount));
    const impactedCount = Util.sum(filter.periods.map(period => period.impactedCount));

    const all = new ChangesFilterPeriod(
      "All",
      totalCount,
      impactedCount,
      false,
      false,
      List()
    );

    return new ChangeFilterOption(
      "year",
      all,
      List(),
      () => update(this.updatedParameters(parameters, true)),
      () => update(this.updatedParameters(parameters, false))
    );
  }
}
