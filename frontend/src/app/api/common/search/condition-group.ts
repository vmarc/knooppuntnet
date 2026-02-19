// this file is generated, please do not modify

import { Condition } from './condition';
import { ConditionGroupOperator } from './condition-group-operator';

export interface ConditionGroup {
  readonly operator: ConditionGroupOperator;
  readonly conditions: ReadonlyArray<Condition>;
}
