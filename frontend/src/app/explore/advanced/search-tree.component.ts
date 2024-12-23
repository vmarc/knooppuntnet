import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { ConditionGroup } from '@api/common/search/condition-group';
import { SearchConditionComponent } from './search-condition.component';
import { SearchGroupHeaderComponent } from './search-group-header.component';

@Component({
  selector: 'kpn-search-tree',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="condition-tree">
      <kpn-search-group-header [group]="group()" [indexes]="[]" />
      <ul>
        @for (conditionLevel1 of group().conditions; track indexLevel1; let indexLevel1 = $index) {
          <li>
            @if (conditionLevel1.subject !== 'group') {
              <kpn-search-condition [condition]="conditionLevel1" [indexes]="[indexLevel1]" />
            } @else {
              <kpn-search-group-header [group]="conditionLevel1.group" [indexes]="[indexLevel1]" />
              <ul>
                @for (
                  conditionLevel2 of conditionLevel1.group.conditions;
                  track indexLevel2;
                  let indexLevel2 = $index
                ) {
                  <li>
                    @if (conditionLevel2.subject !== 'group') {
                      <kpn-search-condition
                        [condition]="conditionLevel2"
                        [indexes]="[indexLevel1, indexLevel2]"
                      />
                    } @else {
                      <kpn-search-group-header
                        [group]="conditionLevel2.group"
                        [indexes]="[indexLevel1, indexLevel2]"
                      />
                      <ul>
                        @for (
                          conditionLevel3 of conditionLevel2.group.conditions;
                          track indexLevel3;
                          let indexLevel3 = $index
                        ) {
                          <li>
                            @if (conditionLevel3.subject !== 'group') {
                              <kpn-search-condition
                                [condition]="conditionLevel3"
                                [indexes]="[indexLevel1, indexLevel2, indexLevel3]"
                              />
                            } @else {
                              <kpn-search-group-header
                                [group]="conditionLevel3.group"
                                [indexes]="[indexLevel1, indexLevel2, indexLevel3]"
                              />
                              <ul>
                                @for (
                                  conditionLevel4 of conditionLevel3.group.conditions;
                                  track indexLevel4;
                                  let indexLevel4 = $index
                                ) {
                                  <li>
                                    <kpn-search-condition
                                      [condition]="conditionLevel4"
                                      [indexes]="[
                                        indexLevel1,
                                        indexLevel2,
                                        indexLevel3,
                                        indexLevel4,
                                      ]"
                                    />
                                  </li>
                                }
                              </ul>
                            }
                          </li>
                        }
                      </ul>
                    }
                  </li>
                }
              </ul>
            }
          </li>
        }
      </ul>
    </div>
  `,
  styles: `
    /*  set up list and list items so that they don't interfere with the tree drawing */
    .condition-tree,
    ul,
    li {
      position: relative;
    }

    ul {
      list-style: none;
      padding-left: 32px;
    }

    /* make sure the pseudo-elements are empty and moved to the left of the list elements */
    li::before,
    li::after {
      content: '';
      position: absolute;
      left: -1em;
    }

    /*  horizontal lines */
    li::before {
      border-top: 1px solid #000;
      top: 25px;
      width: 1em;
      height: 0;
    }

    /* vertical lines */
    li::after {
      border-left: 1px solid #000;
      height: 100%;
      width: 0;
      top: 2px;
    }

    /* the last item on every level has just a very short line leading to it, and nothing further */
    ul > li:last-child::after {
      height: 25px;
    }
  `,
  imports: [FormsModule, ReactiveFormsModule, SearchConditionComponent, SearchGroupHeaderComponent],
})
export class SearchTreeComponent {
  group = input.required<ConditionGroup>();
}
