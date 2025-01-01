import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { Condition } from '@api/common/search/condition';
import { ConditionGroup } from '@api/common/search/condition-group';
import { SearchConditionComponent } from './search-condition.component';
import { SearchGroupHeaderComponent } from './search-group-header.component';
import { SearchService } from './search.service';

@Component({
  selector: 'kpn-search-tree',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="condition-tree">
      <kpn-search-group-header
        [group]="group()"
        [removeEnabled]="false"
        (add)="add([], $event)"
        (update)="updateGroup([], $event)"
      />
      <ul>
        @for (conditionLevel1 of group().conditions; track indexLevel1; let indexLevel1 = $index) {
          <li>
            @let indexes1 = [indexLevel1];
            @if (conditionLevel1.subject !== 'group') {
              <kpn-search-condition
                [condition]="conditionLevel1"
                (update)="update(indexes1, $event)"
                (remove)="remove(indexes1)"
              />
            } @else {
              <kpn-search-group-header
                [group]="conditionLevel1.group"
                (add)="add(indexes1, $event)"
                (update)="updateGroup(indexes1, $event)"
                (remove)="remove(indexes1)"
              />
              <ul>
                @for (
                  conditionLevel2 of conditionLevel1.group.conditions;
                  track indexLevel2;
                  let indexLevel2 = $index
                ) {
                  <li>
                    @let indexes2 = indexes1.concat([indexLevel2]);
                    @if (conditionLevel2.subject !== 'group') {
                      <kpn-search-condition
                        [condition]="conditionLevel2"
                        (update)="update(indexes2, $event)"
                        (remove)="remove(indexes2)"
                      />
                    } @else {
                      <kpn-search-group-header
                        [group]="conditionLevel2.group"
                        (add)="add(indexes2, $event)"
                        (update)="updateGroup(indexes2, $event)"
                        (remove)="remove(indexes2)"
                      />
                      <ul>
                        @for (
                          conditionLevel3 of conditionLevel2.group.conditions;
                          track indexLevel3;
                          let indexLevel3 = $index
                        ) {
                          <li>
                            @let indexes3 = indexes2.concat([indexLevel3]);
                            @if (conditionLevel3.subject !== 'group') {
                              <kpn-search-condition
                                [condition]="conditionLevel3"
                                (update)="update(indexes3, $event)"
                                (remove)="remove(indexes3)"
                              />
                            } @else {
                              <kpn-search-group-header
                                [group]="conditionLevel3.group"
                                (add)="add(indexes3, $event)"
                                (update)="updateGroup(indexes3, $event)"
                                (remove)="remove(indexes3)"
                              />
                              <ul>
                                @for (
                                  conditionLevel4 of conditionLevel3.group.conditions;
                                  track indexLevel4;
                                  let indexLevel4 = $index
                                ) {
                                  <li>
                                    @let indexes4 = indexes3.concat([indexLevel4]);
                                    <kpn-search-condition
                                      [condition]="conditionLevel4"
                                      (update)="update(indexes4, $event)"
                                      (remove)="remove(indexes4)"
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
      margin: 0;
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
  private readonly service = inject(SearchService);

  readonly group = this.service.group;

  add(indexes: number[], condition: Condition): void {
    this.service.add(indexes, condition);
  }

  update(indexes: number[], condition: Condition): void {
    this.service.update(indexes, condition);
  }

  updateGroup(indexes: number[], group: ConditionGroup): void {
    this.service.updateGroup(indexes, group);
  }

  remove(indexes: number[]): void {
    this.service.remove(indexes);
  }
}
