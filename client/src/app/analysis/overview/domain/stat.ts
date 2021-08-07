import { StatisticValues } from '@api/common/statistics/statistic-values';
import { Subset } from '@api/custom/subset';
import { IntegerFormatPipe } from '../../../components/shared/format/integer-format.pipe';
import { StatisticConfiguration } from './statistic-configuration';

export class Stat {
  constructor(
    readonly statisticValues: StatisticValues,
    readonly configuration: StatisticConfiguration
  ) {}

  total(): string {
    if (!this.statisticValues) {
      return '-';
    }
    const total = this.statisticValues.values.reduce((sum, statisticValue) => {
      return sum + statisticValue.value;
    }, 0);
    return this.format(total);
  }

  value(subset: Subset): string {
    if (!this.statisticValues) {
      return '-';
    }
    const subsetStatisticValue = this.statisticValues.values.find(
      (statisticValue) => {
        return (
          statisticValue.country === subset.country &&
          statisticValue.networkType === subset.networkType
        );
      }
    );

    if (subsetStatisticValue) {
      return this.format(subsetStatisticValue.value);
    }
    return '-';
  }

  private format(value: number): string {
    return new IntegerFormatPipe().transform(value);
  }
}
