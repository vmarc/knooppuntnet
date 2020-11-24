import {Component, EventEmitter, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Issue} from '../store/issue/issue.state';

@Component({
  selector: 'app-new-issue',
  template: `
    <form [formGroup]="form" (ngSubmit)="onSubmit()">

      <div class="fields">
        <label for="text">Title</label>
        <input formControlName="title" type="text" id="title"/>

        <label for="description">Description</label>
        <input formControlName="description" type="text" id="description"/>

        <label for="priority">Priority</label>
        <select formControlName="priority" id="priority">
          <option value="low">Low</option>
          <option value="medium">Medium</option>
          <option value="high">High</option>
        </select>
      </div>

      <button [disabled]="!form.valid" type="submit">Submit</button>

    </form>
  `,
  styles: [`
    button[type="submit"] {
      margin: 16px 0;
    }
  `]
})
export class NewIssueComponent {
  form: FormGroup;

  @Output()
  submitNew = new EventEmitter<Issue>();

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      priority: ['low', Validators.required],
    });
  }

  onSubmit(): void {
    const issue = this.form.value;
    this.submitNew.emit(issue);
  }
}
