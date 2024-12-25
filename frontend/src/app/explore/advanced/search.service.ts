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

  removeCondition(indexes: number[]): void {
    this.updateGroup(
      produce(this.group(), (draft) => {
        const conditions = this.conditionsAtIndexes(draft, indexes);
        conditions.splice(indexes[indexes.length - 1], 1);
      })
    );
  }

  private update(conditions: Condition[], indexes: number[]): Condition[] {
    return conditions;
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

  private updateGroup(value: ConditionGroup): void {
    this.state.explore.updateGroup(value);
  }
}
