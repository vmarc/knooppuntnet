import { Statistic } from '../common/statistics';

export interface Statistics {
  readonly map: Map<string, Statistic>;
}
