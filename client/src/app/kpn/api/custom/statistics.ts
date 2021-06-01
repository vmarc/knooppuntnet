import { Statistic } from '../common/statistics/statistic';

export interface Statistics {
  readonly map: Map<string, Statistic>;
}
