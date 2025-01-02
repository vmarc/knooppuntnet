import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { FormControl } from '@angular/forms';
import { FormBuilder } from '@angular/forms';
import { Condition } from '@api/common/search/condition';
import { ConditionGroup } from '@api/common/search/condition-group';
import { ConditionGroupOperator } from '@api/common/search/condition-group-operator';
import { ConditionLocation } from '@api/common/search/condition-location';
import { ConditionOperator } from '@api/common/search/condition-operator';
import { ConditionName } from '@api/common/search/condition-name';
import { ConditionSubject } from '@api/common/search/condition-subject';
import { ConditionTag } from '@api/common/search/condition-tag';
import { ApiService } from '@app/services';
import { ExploreState } from '@app/state';
import { State } from '@app/state';
import { ConditionRouteNameForm } from './condition-controls';
import { ConditionTagForm } from './condition-controls';
import { ConditionLocationForm } from './condition-controls';
import { ConditionGroupForm } from './condition-controls';
import { ConditionForm } from './condition-controls';

@Injectable({
  providedIn: 'root',
})
export class ConditionService {
  private readonly state = inject(State);
  private readonly apiService = inject(ApiService);
  private readonly fb = inject(FormBuilder);
  readonly group = this.state.explore.group;

  readonly form: ConditionGroupForm = this.toConditionGroupForm(ExploreState.example());

  submit() {
    const group = this.toConditionGroup(this.form);
    this.apiService.explore(group).subscribe((response) => {
      if (response.result) {
        this.state.explore.updateRouteSearchResults(response.result);
      }
    });
  }

  private toConditionGroup(groupForm: ConditionGroupForm): ConditionGroup {
    const operator = groupForm.controls.operator.value;
    const conditions = groupForm.controls.conditions.controls.map((c) => this.toCondition(c));
    return {
      operator,
      conditions,
    };
  }

  private toCondition(conditionForm: ConditionForm): Condition {
    const subject = conditionForm.controls.subject.value;
    let tag: ConditionTag = undefined;
    let location: ConditionLocation = undefined;
    let name: ConditionName = undefined;
    let group: ConditionGroup = undefined;

    if (subject === 'tag') {
      tag = conditionForm.controls.tag.value as ConditionTag;
    } else if (subject === 'location') {
      location = conditionForm.controls.location.value as ConditionLocation;
    } else if (subject === 'name') {
      name = conditionForm.controls.name.value as ConditionName;
    } else if (!subject || subject === 'group') {
      group = this.toConditionGroup(conditionForm.controls.group);
    }

    return {
      tag,
      location,
      name,
      group,
    };
  }

  private rootConditionGroup(): ConditionGroupForm {
    return this.fb.group({
      operator: this.fb.control<ConditionGroupOperator>('and'),
      conditions: this.fb.array<ConditionForm>([
        this.toConditionForm(ExploreState.defaultCondition()),
      ]),
    });
  }

  private toConditionGroupForm(group: ConditionGroup): ConditionGroupForm {
    const operator = this.fb.control<ConditionGroupOperator>(group.operator);
    const conditions = this.fb.array<ConditionForm>(
      group.conditions.map((condition) => this.toConditionForm(condition))
    );

    return this.fb.group({
      operator,
      conditions,
    });
  }

  toConditionForm(condition: Condition): ConditionForm {
    let subject: FormControl<ConditionSubject>;
    let tag: ConditionTagForm;
    let location: ConditionLocationForm;
    let name: ConditionRouteNameForm;
    let group: ConditionGroupForm;

    if (condition.tag) {
      subject = this.fb.control<ConditionSubject>('tag');
      const operator = this.fb.control<ConditionOperator>(condition.tag.operator);
      const key = this.fb.control<string>(condition.tag.key);
      const value = this.fb.control<string>(condition.tag.value);
      tag = this.fb.group({
        operator,
        key,
        value,
      });
    } else {
      tag = this.defaultTagForm();
    }

    if (condition.location) {
      subject = this.fb.control<ConditionSubject>('location');
      const nameControl = this.fb.control<string>(condition.location.name);
      location = this.fb.group({
        name: nameControl,
      });
    } else {
      location = this.defaultLocationForm();
    }

    if (condition.name) {
      subject = this.fb.control<ConditionSubject>('name');
      const operator = this.fb.control<ConditionOperator>(condition.name.operator);
      const nameControl = this.fb.control<string>(condition.name.name);
      name = this.fb.group({
        operator,
        name: nameControl,
      });
    } else {
      name = this.defaultNameForm();
    }

    if (condition.group) {
      subject = this.fb.control<ConditionSubject>('group');
      const operator = this.fb.control<ConditionGroupOperator>(condition.group.operator);
      const conditions = this.fb.array<ConditionForm>(
        condition.group.conditions.map((c) => this.toConditionForm(c))
      );
      group = this.fb.group({
        operator,
        conditions,
      });
    } else {
      group = this.defaultGroupForm();
    }

    return this.fb.group({
      subject,
      tag,
      location,
      name,
      group,
    });
  }

  private defaultTagForm(): ConditionTagForm {
    return this.fb.group({
      operator: this.fb.control<ConditionOperator>('contains'),
      key: this.fb.control<string>(''),
      value: this.fb.control<string>(''),
    });
  }

  private defaultLocationForm(): ConditionLocationForm {
    return this.fb.group({
      name: this.fb.control<string>(''),
    });
  }

  private defaultNameForm(): ConditionRouteNameForm {
    return this.fb.group({
      operator: this.fb.control<ConditionOperator>('contains'),
      name: this.fb.control<string>(''),
    });
  }

  private defaultGroupForm(): ConditionGroupForm {
    return this.fb.group({
      operator: this.fb.control<ConditionGroupOperator>('and'),
      conditions: this.fb.array<ConditionForm>([]),
    });
  }
}
