import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButton } from '@angular/material/button';
import { ConditionGroup } from '@api/common/search/condition-group';
import { DividerComponent } from '@app/components/shared';
import { SearchConditionComponent } from './search-condition.component';
import { SearchGroupHeaderComponent } from './search-group-header.component';

@Component({
  selector: 'kpn-search',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <form>
      <kpn-search-group-header />
      <kpn-search-condition />
    </form>

    <kpn-divider />

    <div>
      <button mat-stroked-button>Search</button>
    </div>

    <kpn-divider />

    <kpn-divider />
    <div class="condition-tree">
      <kpn-search-group-header />
      <ul>
        <li>
          <kpn-search-condition />
        </li>
        <li>
          <kpn-search-condition />
        </li>
        <li>
          <kpn-search-group-header />
          <ul>
            <li>
              <kpn-search-condition />
            </li>
          </ul>
        </li>
        <li>
          <kpn-search-group-header />
          <ul>
            <li>
              <kpn-search-condition />
            </li>
            <li>
              <kpn-search-condition />
            </li>
          </ul>
        </li>
      </ul>
    </div>
  `,
  styles: `
    .condition-line {
      display: flex;
      align-items: center;
    }

    /*  set up list and list items so that they don't interfere with the tree drawing */
    .condition-tree,
    .condition-tree ul,
    .condition-tree li {
      position: relative;
    }

    .condition-tree ul {
      list-style: none;
      padding-left: 32px;
    }

    /* make sure the pseudo-elements are empty and moved to the left of the list elements */
    .condition-tree li::before,
    .condition-tree li::after {
      content: '';
      position: absolute;
      left: -1em;
    }

    /*  horizontal lines */
    .condition-tree li::before {
      border-top: 1px solid #000;
      top: 25px;
      width: 1em;
      height: 0;
    }

    /* vertical lines */
    .condition-tree li::after {
      border-left: 1px solid #000;
      height: 100%;
      width: 0;
      top: 2px;
    }

    /* the last item on every level has just a very short line leading to it, and nothing further */
    .condition-tree ul > li:last-child::after {
      height: 25px;
    }
  `,
  imports: [
    MatButton,
    FormsModule,
    ReactiveFormsModule,
    DividerComponent,
    SearchGroupHeaderComponent,
    SearchConditionComponent,
  ],
})
export class SearchComponent {
  myControl = new FormControl('');

  example(): ConditionGroup {
    return {
      operator: 'and',
      conditions: [
        {
          conditionType: 'tag',
          tag: {
            operator: 'contains',
            key: 'operator',
            value: 'US:US',
          },
        },
        {
          conditionType: 'tag',
          tag: {
            operator: 'equals',
            key: 'symbol',
            value: 'gray',
          },
        },
        {
          conditionType: 'group',
          group: {
            operator: 'or',
            conditions: [
              {
                conditionType: 'name',
                name: {
                  operator: 'contains',
                  name: 'LAW 9',
                },
              },
              {
                conditionType: 'location',
                name: {
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
