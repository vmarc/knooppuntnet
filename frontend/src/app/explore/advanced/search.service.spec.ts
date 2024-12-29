import { TestBed } from '@angular/core/testing';
import { ConditionGroup } from '@api/common/search/condition-group';
import { State } from '@app/state';
import { SearchService } from './search.service';

describe('searchService', () => {
  let state: State;
  let searchService: SearchService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [State, SearchService],
    });
    state = TestBed.inject(State);
    searchService = TestBed.inject(SearchService);
  });

  const group: ConditionGroup = {
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

  it('delete at level 1', () => {
    state.explore.updateGroup(group);
    searchService.remove([2]);

    expect(searchService.group()).toEqual({
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
      ],
    });
  });

  it('delete at level 2', () => {
    state.explore.updateGroup(group);
    searchService.remove([2, 0]);

    expect(searchService.group()).toEqual({
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
    });
  });
});
