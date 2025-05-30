import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButton } from '@angular/material/button';
import { ConditionTreeNodeComponent } from './condition-tree-node.component';
import { ConditionService } from './condition.service';

@Component({
  selector: 'ui-condition-tree',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="condition-tree">
      <ui-condition-tree-node [form]="form" [root]="true" />
    </div>
    <div class="kpn-spacer-above">
      <button mat-stroked-button (click)="onSubmit()">Search</button>
    </div>
  `,
  imports: [FormsModule, ReactiveFormsModule, MatButton, ConditionTreeNodeComponent],
})
export class ConditionTreeComponent {
  private readonly service = inject(ConditionService);
  readonly form = this.service.form;

  onSubmit(): void {
    this.service.submit();
  }
}
