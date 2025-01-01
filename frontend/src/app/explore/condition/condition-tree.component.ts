import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButton } from '@angular/material/button';
import { Condition } from '@api/common/search/condition';
import { ConditionGroup } from '@api/common/search/condition-group';
import { ConditionTreeItemComponent } from './condition-tree-item.component';
import { ConditionService } from './condition.service';

@Component({
  selector: 'kpn-condition-tree',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <form [formGroup]="form" (ngSubmit)="onSubmit()">
      <div class="condition-tree">
        <kpn-condition-tree-item [form]="form" />
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
  imports: [FormsModule, ReactiveFormsModule, MatButton, ConditionTreeItemComponent],
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
