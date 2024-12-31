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
    console.log(
      `UPDATE indexes=${JSON.stringify(indexes)}, condition=${JSON.stringify(condition, null, 2)}`
    );
    this.updateRoot(
      produce(this.group(), (draft) => {
        const conditions = this.groupConditionsAtIndexes(draft, indexes);
        console.log(
          `UPDATE indexes=${JSON.stringify(indexes)}, condition=${JSON.stringify(condition, null, 2)}, conditions=${conditions}`
        );
        conditions[indexes[indexes.length - 1]] = condition;
      })
    );
  }

  updateGroup(indexes: number[], group: ConditionGroup): void {
    console.log(
      `UPDATE GROUP indexes=${JSON.stringify(indexes)}, group=${JSON.stringify(group, null, 2)}`
    );
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
    if (indexes.length >= 0) {
      group = group.conditions[indexes[0]].group;
    }
    if (indexes.length >= 1) {
      group = group.conditions[indexes[1]].group;
    }
    if (indexes.length >= 2) {
      group = group.conditions[indexes[2]].group;
    }
    return group;
  }

  private updateRoot(value: ConditionGroup): void {
    console.log(`UPDATE ROOT ${JSON.stringify(value, null, 2)}`);
    this.state.explore.updateGroup(value);
  }
}
