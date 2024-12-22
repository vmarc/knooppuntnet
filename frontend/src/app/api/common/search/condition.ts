// this file is generated, please do not modify

import { ConditionGroup } from './condition-group';
import { ConditionLocation } from './condition-location';
import { ConditionRouteName } from './condition-route-name';
import { ConditionTag } from './condition-tag';
import { ConditionType } from './condition-type';

export interface Condition {
  readonly conditionType: ConditionType;
  readonly tag?: ConditionTag;
  readonly location?: ConditionLocation;
  readonly name?: ConditionRouteName;
  readonly group?: ConditionGroup;
}
