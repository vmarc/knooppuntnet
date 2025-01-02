import { FormControl } from '@angular/forms';
import { FormGroup } from '@angular/forms';
import { FormArray } from '@angular/forms';
import { ConditionGroupOperator } from '@api/common/search/condition-group-operator';
import { ConditionOperator } from '@api/common/search/condition-operator';
import { ConditionSubject } from '@api/common/search/condition-subject';

export type ConditionTagForm = FormGroup<{
  operator: FormControl<ConditionOperator>;
  key: FormControl<string>;
  value: FormControl<string>;
}>;

export type ConditionLocationForm = FormGroup<{
  name: FormControl<string>;
}>;

export type ConditionRouteNameForm = FormGroup<{
  operator: FormControl<ConditionOperator>;
  name: FormControl<string>;
}>;

export type ConditionForm = FormGroup<{
  subject: FormControl<ConditionSubject>;
  tag: ConditionTagForm;
  location: ConditionLocationForm;
  name: ConditionRouteNameForm;
  group: ConditionGroupForm;
}>;

export type ConditionGroupForm = FormGroup<{
  operator: FormControl<ConditionGroupOperator>;
  conditions: FormArray<ConditionForm>;
}>;
