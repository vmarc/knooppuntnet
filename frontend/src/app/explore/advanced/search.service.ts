import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { Condition } from '@api/common/search/condition';
import { ConditionGroup } from '@api/common/search/condition-group';
import { State } from '@app/state';
import { produce } from 'immer';

@Injectable({
  providedIn: 'root',
})
export class SearchService {
  private readonly state = inject(State);
  readonly group = this.state.explore.group;

  removeCondition(indexes: number[]): void {
    if (indexes.length === 1) {
      const newGroup = produce(this.group(), (draft) => {
        draft.conditions.splice(indexes[0] /*the index */, 1);
      });
      this.updateGroup(newGroup);
    } else if (indexes.length === 2) {
      const newGroup = produce(this.group(), (draft) => {
        draft.conditions[indexes[0]].group.conditions.splice(indexes[1] /*the index */, 1);
      });
      this.updateGroup(newGroup);
    } else if (indexes.length === 3) {
      const newGroup = produce(this.group(), (draft) => {
        draft.conditions[indexes[0]].group.conditions[indexes[1]].group.conditions.splice(
          indexes[2] /*the index */,
          1
        );
      });
      this.updateGroup(newGroup);
    }
  }

  private update(conditions: Condition[], indexes: number[]): Condition[] {
    return conditions;
  }

  private updateGroup(value: ConditionGroup): void {
    this.state.explore.updateGroup(value);
  }
}
