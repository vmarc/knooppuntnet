import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButton } from '@angular/material/button';
import { Condition } from '@api/common/search/condition';
import { ConditionGroup } from '@api/common/search/condition-group';
import { ConditionGroupComponent } from './condition-group.component';
import { ConditionComponent } from './condition.component';
import { ConditionService } from './condition.service';

@Component({
  selector: 'kpn-condition-tree',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <form [formGroup]="form" (ngSubmit)="onSubmit()">
      <div class="condition-tree">
        <kpn-condition-group [form]="form" [removeEnabled]="false" />
        <ul formArrayName="conditions">
          @for (
            conditionLevel1 of form.controls.conditions.controls;
            track indexLevel1;
            let indexLevel1 = $index
          ) {
            <li [formGroupName]="indexLevel1">
              @let indexes1 = [indexLevel1];
              @if (conditionLevel1.controls.subject.value !== 'group') {
                <kpn-condition [form]="conditionLevel1" />
              } @else {
                @let formLevel1 = conditionLevel1.controls.group;
                <form [formGroup]="formLevel1">
                  <kpn-condition-group [form]="conditionLevel1.controls.group" />
                  <ul formArrayName="conditions">
                    @for (
                      conditionLevel2 of formLevel1.controls.conditions.controls;
                      track indexLevel2;
                      let indexLevel2 = $index
                    ) {
                      <li [formGroupName]="indexLevel2">
                        @let indexes2 = indexes1.concat([indexLevel2]);
                        @if (conditionLevel2.controls.subject.value !== 'group') {
                          <kpn-condition [form]="conditionLevel2" />
                        } @else {
                          @let formLevel2 = conditionLevel2.controls.group;
                          <form [formGroup]="formLevel2">
                            <kpn-condition-group [form]="conditionLevel2.controls.group" />
                            <ul formArrayName="conditions">
                              @for (
                                conditionLevel3 of formLevel2.controls.conditions.controls;
                                track indexLevel3;
                                let indexLevel3 = $index
                              ) {
                                <li>
                                  @let indexes3 = indexes2.concat([indexLevel3]);
                                  @if (conditionLevel3.controls.subject.value !== 'group') {
                                    <kpn-condition [form]="conditionLevel3" />
                                  } @else {
                                    @let formLevel3 = conditionLevel3.controls.group;
                                    <form [formGroup]="formLevel3">
                                      <kpn-condition-group
                                        [form]="conditionLevel3.controls.group"
                                      />
                                      <ul formArrayName="conditions">
                                        @for (
                                          conditionLevel4 of formLevel3.controls.conditions
                                            .controls;
                                          track indexLevel4;
                                          let indexLevel4 = $index
                                        ) {
                                          <li>
                                            @let indexes4 = indexes3.concat([indexLevel4]);
                                            <kpn-condition [form]="conditionLevel4" />
                                          </li>
                                        }
                                      </ul>
                                    </form>
                                  }
                                </li>
                              }
                            </ul>
                          </form>
                        }
                      </li>
                    }
                  </ul>
                </form>
              }
            </li>
          }
        </ul>
      </div>
      <div class="kpn-spacer-above">
        <button mat-stroked-button type="submit">Search</button>
      </div>
    </form>
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
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MatButton,
    ConditionComponent,
    ConditionGroupComponent,
  ],
})
export class ConditionTreeComponent {
  private readonly service = inject(ConditionService);

  form = this.service.form;

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

  onSubmit(): void {
    this.service.submit();
  }
}
