import { output } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { Condition } from '@api/common/search/condition';
import { ConditionSubject } from '@api/common/search/condition-subject';
import { ExploreState } from '@app/state/explore-state';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzCardComponent } from 'ng-zorro-antd/card';
import { NzDropDownDirective } from 'ng-zorro-antd/dropdown';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NzOptionComponent } from 'ng-zorro-antd/select';
import { NzSelectComponent } from 'ng-zorro-antd/select';
import { ConditionForm } from './condition-controls';
import { ConditionLocationComponent } from './condition-location.component';
import { ConditionNameComponent } from './condition-name.component';
import { ConditionTagComponent } from './condition-tag.component';

@Component({
  selector: 'ui-condition',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="card-wrapper">
      <nz-card>
        <div class="condition-line kpn-small-spacer-above">
          @let subject = form().controls.subject;

          <div>
            <div>subject</div>
            <div>
              <nz-select [formControl]="subject">
                <nz-option nzValue="location" nzLabel="location" />
                <nz-option nzValue="tag" nzLabel="tag" />
                <nz-option nzValue="name" nzLabel="name" />
              </nz-select>
            </div>
          </div>

          @if (subject.value === 'tag') {
            <ui-condition-tag [form]="form().controls.tag" />
          }
          @if (subject.value === 'location') {
            <ui-condition-location [form]="form().controls.location" />
          }
          @if (subject.value === 'name') {
            <ui-condition-name [form]="form().controls.name" />
          }

          <button nz-dropdown nz-button nzShape="circle" (click)="onRemove()">
            <nz-icon nzType="close" />
          </button>
        </div>
      </nz-card>
    </div>
  `,
  styles: `
    .card-wrapper {
      padding-top: 0.5em;
    }

    .condition-line {
      display: flex;
      padding-left: 0.5em;
      gap: 0.5em;
    }

    button {
      margin-top: 0.4em;
    }
  `,
  imports: [
    ConditionLocationComponent,
    ConditionNameComponent,
    ConditionTagComponent,
    FormsModule,
    NzButtonComponent,
    NzCardComponent,
    NzDropDownDirective,
    NzIconDirective,
    NzOptionComponent,
    NzSelectComponent,
    ReactiveFormsModule,
  ],
})
export class ConditionComponent {
  readonly form = input.required<ConditionForm>();
  readonly update = output<Condition>();
  readonly remove = output<void>();

  onSubjectChange(subject: ConditionSubject): void {
    const updated = ExploreState.defaultConditionSubject(subject);
    this.update.emit(updated);
  }

  onConditionChange(value: Condition): void {
    this.update.emit(value);
  }

  onRemove() {
    this.remove.emit();
  }
}
