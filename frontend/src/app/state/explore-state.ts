import { signal } from '@angular/core';
import { ConditionGroup } from '@api/common/search/condition-group';
import { ExploreRoute } from './explore-route';

export class ExploreState {
  private readonly _group = signal<ConditionGroup>(/*this.defaultGroup()*/ this.example());
  private readonly _routes = signal<Array<ExploreRoute>>([]);

  readonly group = this._group.asReadonly();
  readonly routes = this._routes.asReadonly();

  updateGroup(value: ConditionGroup): void {
    this._group.set(value);
  }

  updateRoutes(routes: Array<ExploreRoute>) {
    this._routes.set(routes);
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

  private example(): ConditionGroup {
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
