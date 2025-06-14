import { output } from '@angular/core';
import { input } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { Condition } from '@api/common/search/condition';
import { ConditionGroupForm } from './condition-controls';
import { ConditionGroupComponent } from './condition-group.component';
import { ConditionComponent } from './condition.component';
import { ConditionService } from './condition.service';

@Component({
  selector: 'ui-condition-tree-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <form [formGroup]="form()">
      <ui-condition-group
        [form]="form()"
        [removeEnabled]="!root()"
        (add)="add($event)"
        (remove)="removeGroup()"
      />
      <ul formArrayName="conditions">
        @for (conditionForm of form().controls.conditions.controls; track $index) {
          <li [formGroupName]="$index">
            @if (conditionForm.controls.subject.value !== 'group') {
              <ui-condition [form]="conditionForm" (remove)="removeCondition($index)" />
            } @else {
              @let groupForm = conditionForm.controls.group;
              <form [formGroup]="groupForm">
                <ui-condition-tree-node [form]="groupForm" (remove)="removeCondition($index)" />
              </form>
            }
          </li>
        }
      </ul>
    </form>
  `,
  styles: `
    /*  set up list and list items so that they don't interfere with the tree drawing */
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
  imports: [FormsModule, ReactiveFormsModule, ConditionComponent, ConditionGroupComponent],
})
export class ConditionTreeNodeComponent {
  private readonly service = inject(ConditionService);
  readonly root = input<boolean>(false);
  readonly form = input.required<ConditionGroupForm>();
  readonly remove = output<void>();

  add(condition: Condition): void {
    const control = this.service.toConditionForm(condition);
    this.form().controls.conditions.push(control);
  }

  removeGroup(): void {
    this.remove.emit();
  }

  removeCondition(index: number): void {
    this.form().controls.conditions.removeAt(index);
  }
}
