import { Injectable } from '@angular/core';
import { signal } from '@angular/core';
import { ConditionGroup } from '@api/common/search/condition-group';

@Injectable({
  providedIn: 'root',
})
export class SearchService {
  private readonly _group = signal<ConditionGroup>(this.example());

  readonly group = this._group.asReadonly();

  removeCondition(indexes: number[]): void {
    const updatedGroup = this.group();

    this._group.set(updatedGroup);
  }

  private defaultGroup(): ConditionGroup {
    return {
      operator: 'and',
      conditions: [
        {
          subject: 'tag',
          tag: {
            operator: 'equals',
            key: '',
            value: '',
          },
        },
      ],
    };
  }

  example(): ConditionGroup {
    return {
      operator: 'and',
      conditions: [
        {
          subject: 'tag',
          tag: {
            operator: 'contains',
            key: 'operator',
            value: 'US:US',
          },
        },
        {
          subject: 'tag',
          tag: {
            operator: 'equals',
            key: 'symbol',
            value: 'gray',
          },
        },
        {
          subject: 'group',
          group: {
            operator: 'or',
            conditions: [
              {
                subject: 'name',
                name: {
                  operator: 'contains',
                  name: 'LAW 9',
                },
              },
              {
                subject: 'location',
                location: {
                  operator: 'contains',
                  name: 'Essen',
                },
              },
            ],
          },
        },
      ],
    };
  }
}
