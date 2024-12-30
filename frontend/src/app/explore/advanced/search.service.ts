import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { Condition } from '@api/common/search/condition';
import { ConditionGroup } from '@api/common/search/condition-group';
import { State } from '@app/state';
import { WritableDraft } from 'immer';
import { produce } from 'immer';

@Injectable({
  providedIn: 'root',
})
export class SearchService {
  private readonly state = inject(State);
  readonly group = this.state.explore.group;

  add(indexes: number[], condition: Condition): void {
    console.log(
      `ADD indexes=${JSON.stringify(indexes)}, condition=${JSON.stringify(condition, null, 2)}`
    );
    this.updateRoot(
      produce(this.group(), (draft) => {
        const conditions = this.groupConditionsAtIndexes(draft, indexes);
        conditions.push(condition);
      })
    );
  }

  update(indexes: number[], condition: Condition): void {
    this.updateRoot(
      produce(this.group(), (draft) => {
        const conditions = this.groupConditionsAtIndexes(draft, indexes);
        conditions.splice(indexes[indexes.length - 1], 0, condition);
      })
    );
  }

  updateGroup(indexes: number[], group: ConditionGroup): void {
    this.updateRoot(
      produce(this.group(), (draft) => {
        const conditions = this.groupConditionsAtIndexes(draft, indexes);
        const groupCondition = conditions.at(indexes[indexes.length - 1]);
        const newGroupCondition = {
          ...groupCondition,
          group: group,
        };
        conditions.splice(indexes[indexes.length - 1], 0, newGroupCondition);
      })
    );
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
    let conditions: Condition[] = [];
    if (indexes.length >= 0) {
      conditions = draft.conditions;
      console.log(`conditions0=${JSON.stringify(conditions, null, 2)}`);
    }
    if (indexes.length >= 1) {
      conditions = conditions[indexes[0]].group.conditions;
      console.log(`conditions1=${JSON.stringify(conditions, null, 2)}`);
    }
    if (indexes.length >= 2) {
      conditions = conditions[indexes[1]].group.conditions;
      console.log(`conditions2=${JSON.stringify(conditions, null, 2)}`);
    }
    if (indexes.length >= 3) {
      conditions = conditions[indexes[2]].group.conditions;
    }
    return conditions;
  }

  private updateRoot(value: ConditionGroup): void {
    this.state.explore.updateGroup(value);
  }
}
