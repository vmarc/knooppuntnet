import { signal } from '@angular/core';
import { Condition } from '@api/common/search/condition';
import { ConditionGroup } from '@api/common/search/condition-group';
import { ConditionLocation } from '@api/common/search/condition-location';
import { ConditionRouteName } from '@api/common/search/condition-route-name';
import { ConditionSubject } from '@api/common/search/condition-subject';
import { ConditionTag } from '@api/common/search/condition-tag';
import { ExploreRoute } from './explore-route';

export class ExploreState {
  private readonly _group = signal<ConditionGroup>(/*this.defaultGroup()*/ ExploreState.example());
  private readonly _routes = signal<Array<ExploreRoute>>([]);

  readonly group = this._group.asReadonly();
  readonly routes = this._routes.asReadonly();

  updateGroup(value: ConditionGroup): void {
    this._group.set(value);
  }

  updateRoutes(routes: Array<ExploreRoute>) {
    this._routes.set(routes);
  }

  static defaultGroup(): ConditionGroup {
    return {
      operator: 'and',
      conditions: [this.defaultCondition()],
    };
  }

  static defaultCondition(): Condition {
    return {
      subject: 'tag',
      tag: {
        operator: 'equals',
        key: '',
        value: '',
      },
    };
  }

  static defaultConditionSubject(subject: ConditionSubject): Condition {
    const tag: ConditionTag =
      subject === 'tag' ? { operator: 'contains', key: '', value: '' } : undefined;
    const location: ConditionLocation =
      subject === 'location' ? { operator: 'equals', name: '' } : undefined;
    const name: ConditionRouteName =
      subject === 'name' ? { operator: 'equals', name: '' } : undefined;
    const group: ConditionGroup = subject === 'group' ? this.defaultGroup() : undefined;

    return {
      subject,
      tag,
      location,
      name,
      group,
    };
  }

  static defaultGroupCondition(): Condition {
    return {
      subject: 'group',
      group: {
        operator: 'and',
        conditions: [this.defaultCondition()],
      },
    };
  }

  static example(): ConditionGroup {
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
        // {
        //   subject: 'tag',
        //   tag: {
        //     operator: 'equals',
        //     key: 'symbol',
        //     value: 'gray',
        //   },
        // },
        // {
        //   subject: 'group',
        //   group: {
        //     operator: 'or',
        //     conditions: [
        //       {
        //         subject: 'name',
        //         name: {
        //           operator: 'contains',
        //           name: 'LAW 9',
        //         },
        //       },
        //       {
        //         subject: 'location',
        //         location: {
        //           operator: 'contains',
        //           name: 'Essen',
        //         },
        //       },
        //     ],
        //   },
        // },
      ],
    };
  }
}
