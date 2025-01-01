import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Condition } from '@api/common/search/condition';
import { ConditionGroup } from '@api/common/search/condition-group';
import { ConditionGroupOperator } from '@api/common/search/condition-group-operator';
import { ConditionLocation } from '@api/common/search/condition-location';
import { ConditionOperator } from '@api/common/search/condition-operator';
import { ConditionRouteName } from '@api/common/search/condition-route-name';
import { ConditionSubject } from '@api/common/search/condition-subject';
import { ConditionTag } from '@api/common/search/condition-tag';
import { ExploreState } from '@app/state';
import { State } from '@app/state';
import { WritableDraft } from 'immer';
import { produce } from 'immer';
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
  readonly group = this.state.explore.group;

  private readonly fb = inject(FormBuilder);

  form: ConditionGroupForm = this.example();

  submit() {
    const group = this.convertGroup(this.form.value as ConditionGroup);
    console.log(`SUBMIT ${JSON.stringify(group, null, 2)}`);
  }

  private convertGroup(group: ConditionGroup): ConditionGroup {
    const conditions = group.conditions.map((c) => this.convert(c));
    return {
      ...group,
      conditions,
    };
  }

  private convert(condition: Condition): Condition {
    const subject = condition.subject;
    let tag: ConditionTag = undefined;
    let location: ConditionLocation = undefined;
    let name: ConditionRouteName = undefined;
    let group: ConditionGroup = undefined;

    if (condition.subject === 'tag') {
      tag = condition.tag;
    } else if (condition.subject === 'location') {
      location = condition.location;
    } else if (condition.subject === 'name') {
      name = condition.name;
    } else if (!condition.subject || condition.subject === 'group') {
      const conditions = condition.group.conditions.map((c) => this.convert(c));
      group = {
        ...condition.group,
        conditions,
      };
    }

    return {
      subject,
      tag,
      location,
      name,
      group,
    };
  }

  private generateCondition(): ConditionForm {
    return this.fb.group({
      subject: this.fb.control<ConditionSubject>('name'),
      tag: this.fb.group({
        operator: this.fb.control<ConditionOperator>('contains'),
        key: this.fb.control<string>(''),
        value: this.fb.control<string>(''),
      }),
      location: this.fb.group({
        operator: this.fb.control<ConditionOperator>('contains'),
        name: this.fb.control<string>(''),
      }),
      name: this.fb.group({
        operator: this.fb.control<ConditionOperator>('contains'),
        name: this.fb.control<string>(''),
      }),
      group: this.fb.group({
        operator: this.fb.control<ConditionGroupOperator>('and'),
        conditions: this.fb.array<ConditionForm>([]),
      }),
    });
  }

  private rootConditionGroup(): ConditionGroupForm {
    return this.fb.group({
      operator: this.fb.control<ConditionGroupOperator>('and'),
      conditions: this.fb.array<ConditionForm>([this.generateCondition()]),
    });
  }

  private example(): ConditionGroupForm {
    return this.toConditionGroupForm(ExploreState.example());
  }

  private toConditionGroupForm(group: ConditionGroup): ConditionGroupForm {
    const operator = this.fb.control<ConditionGroupOperator>(group.operator);
    const conditions = this.fb.array<ConditionForm>(
      group.conditions.map((condition) => this.toCondition(condition))
    );

    return this.fb.group({
      operator,
      conditions,
    });
  }

  private toCondition(condition: Condition): ConditionForm {
    const subject = this.fb.control<ConditionSubject>(condition.subject);

    let tag: ConditionTagForm;
    let location: ConditionLocationForm;
    let name: ConditionRouteNameForm;
    let group: ConditionGroupForm;

    if (condition.subject === 'tag') {
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

    if (condition.subject === 'location') {
      const operator = this.fb.control<ConditionOperator>(condition.location.operator);
      const nameControl = this.fb.control<string>(condition.location.name);
      location = this.fb.group({
        operator,
        name: nameControl,
      });
    } else {
      location = this.defaultLocationForm();
    }

    if (condition.subject === 'name') {
      const operator = this.fb.control<ConditionOperator>(condition.name.operator);
      const nameControl = this.fb.control<string>(condition.name.name);
      name = this.fb.group({
        operator,
        name: nameControl,
      });
    } else {
      name = this.defaultNameForm();
    }

    if (condition.subject === 'group') {
      const operator = this.fb.control<ConditionGroupOperator>(condition.group.operator);
      const conditions = this.fb.array<ConditionForm>(
        condition.group.conditions.map((c) => this.toCondition(c))
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
      operator: this.fb.control<ConditionOperator>('contains'),
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

  add(indexes: number[], condition: Condition): void {
    this.updateRoot(
      produce(this.group(), (draft) => {
        const group = this.groupAtIndexes(draft, indexes);
        group.conditions.push(condition);
      })
    );
  }

  update(indexes: number[], condition: Condition): void {
    this.updateRoot(
      produce(this.group(), (draft) => {
        const conditions = this.groupConditionsAtIndexes(draft, indexes);
        conditions[indexes[indexes.length - 1]] = condition;
      })
    );
  }

  updateGroup(indexes: number[], group: ConditionGroup): void {
    if (indexes.length === 0) {
      this.updateRoot(group);
    } else {
      this.updateRoot(
        produce(this.group(), (draft) => {
          const conditions = this.conditionsAtIndexes(draft, indexes);
          conditions[indexes[indexes.length - 1]] = {
            subject: 'group',
            group,
          };
        })
      );
    }
  }

  remove(indexes: number[]): void {
    this.updateRoot(
      produce(this.group(), (draft) => {
        const conditions = this.conditionsAtIndexes(draft, indexes);
        conditions.splice(indexes[indexes.length - 1], 1);
      })
    );
  }

  private conditionsAtIndexes(
    draft: WritableDraft<ConditionGroup>,
    indexes: number[]
  ): Condition[] {
    let conditions: Condition[] = [];
    if (indexes.length >= 1) {
      conditions = draft.conditions;
    }
    if (indexes.length >= 2) {
      conditions = conditions[indexes[0]].group.conditions;
    }
    if (indexes.length >= 3) {
      conditions = conditions[indexes[1]].group.conditions;
    }
    if (indexes.length >= 4) {
      conditions = conditions[indexes[2]].group.conditions;
    }
    return conditions;
  }

  private groupConditionsAtIndexes(
    draft: WritableDraft<ConditionGroup>,
    indexes: number[]
  ): Condition[] {
    let conditions: Condition[] = draft.conditions;
    if (indexes.length - 1 > 0) {
      conditions = conditions[indexes[0]].group.conditions;
    }
    if (indexes.length - 1 > 1) {
      conditions = conditions[indexes[1]].group.conditions;
    }
    if (indexes.length - 1 > 2) {
      conditions = conditions[indexes[2]].group.conditions;
    }
    return conditions;
  }

  private groupAtIndexes(draft: WritableDraft<ConditionGroup>, indexes: number[]): ConditionGroup {
    let group: ConditionGroup = draft;
    if (indexes.length > 0) {
      group = group.conditions[indexes[0]].group;
    }
    if (indexes.length > 1) {
      group = group.conditions[indexes[1]].group;
    }
    if (indexes.length > 2) {
      group = group.conditions[indexes[2]].group;
    }
    return group;
  }

  private updateRoot(value: ConditionGroup): void {
    this.state.explore.updateGroup(value);
  }
}
