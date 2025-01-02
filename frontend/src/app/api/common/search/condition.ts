// this file is generated, please do not modify

import { ConditionGroup } from './condition-group';
import { ConditionLocation } from './condition-location';
import { ConditionName } from './condition-name';
import { ConditionTag } from './condition-tag';

export interface Condition {
  readonly tag?: ConditionTag;
  readonly location?: ConditionLocation;
  readonly name?: ConditionName;
  readonly group?: ConditionGroup;
}
