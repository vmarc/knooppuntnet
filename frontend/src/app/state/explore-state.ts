import { signal } from '@angular/core';
import { Condition } from '@api/common/search/condition';
import { ConditionGroup } from '@api/common/search/condition-group';
import { ConditionLocation } from '@api/common/search/condition-location';
import { ConditionName } from '@api/common/search/condition-name';
import { ConditionSubject } from '@api/common/search/condition-subject';
import { ConditionTag } from '@api/common/search/condition-tag';
import { RouteSearchResult } from '@api/common/search/route-search-result';
import { ExploreRoute } from './explore-route';

export class ExploreState {
  private readonly _group = signal<ConditionGroup>(/*this.defaultGroup()*/ ExploreState.example());
  private readonly _routes = signal<Array<ExploreRoute>>([]);
  private readonly _routeSearchResults = signal<Array<RouteSearchResult>>([]);
  private readonly _selectedRouteSearchResult = signal<RouteSearchResult | undefined>(undefined);

  readonly group = this._group.asReadonly();
  readonly routes = this._routes.asReadonly();
  readonly routeSearchResults = this._routeSearchResults.asReadonly();
  readonly selectedRouteSearchResult = this._selectedRouteSearchResult.asReadonly();

  updateGroup(value: ConditionGroup): void {
    this._group.set(value);
  }

  updateRoutes(routes: Array<ExploreRoute>) {
    this._routes.set(routes);
  }

  updateRouteSearchResults(routeSearchResults: Array<RouteSearchResult>) {
    this._routeSearchResults.set(routeSearchResults);
  }

  updateRouteSearchResult(routeSearchResult: RouteSearchResult | undefined) {
    this._selectedRouteSearchResult.set(routeSearchResult);
  }

  static defaultGroup(): ConditionGroup {
    return {
      operator: 'and',
      conditions: [this.defaultCondition()],
    };
  }

  static defaultCondition(): Condition {
    return {
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
    const location: ConditionLocation = subject === 'location' ? { name: '' } : undefined;
    const name: ConditionName = subject === 'name' ? { operator: 'equals', name: '' } : undefined;
    const group: ConditionGroup = subject === 'group' ? this.defaultGroup() : undefined;

    return {
      tag,
      location,
      name,
      group,
    };
  }

  static defaultGroupCondition(): Condition {
    return {
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
          name: {
            operator: 'contains',
            name: 'LAW 9',
          },
        },
      ],
    };
  }

  static example2(): ConditionGroup {
    return {
      operator: 'and',
      conditions: [
        {
          tag: {
            operator: 'contains',
            key: 'operator',
            value: 'US:US',
          },
        },
        {
          tag: {
            operator: 'equals',
            key: 'symbol',
            value: 'gray',
          },
        },
        {
          group: {
            operator: 'or',
            conditions: [
              {
                name: {
                  operator: 'contains',
                  name: 'LAW 9',
                },
              },
              {
                location: {
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
