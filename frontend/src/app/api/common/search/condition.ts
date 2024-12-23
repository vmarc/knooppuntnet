// this file is generated, please do not modify

import { ConditionGroup } from './condition-group';
import { ConditionLocation } from './condition-location';
import { ConditionRouteName } from './condition-route-name';
import { ConditionSubject } from './condition-subject';
import { ConditionTag } from './condition-tag';

export interface Condition {
  readonly subject: ConditionSubject;
  readonly tag?: ConditionTag;
  readonly location?: ConditionLocation;
  readonly name?: ConditionRouteName;
  readonly group?: ConditionGroup;
}
