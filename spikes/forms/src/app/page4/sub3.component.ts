import { Component } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Validators } from '@angular/forms';
import { ControlContainer } from '@angular/forms';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-sub-3',
  template: `
    <div class="fields">
      <label>Field 31</label>
      <input appInput [formControl]="field31" />
      <app-field-errors [control]="field31"></app-field-errors>

      <label>Field 32</label>
      <input appInput #last [formControl]="field32" />
      <app-field-errors [control]="field32"></app-field-errors>
    </div>
  `,
})
export class Sub3Component implements OnDestroy {
  readonly field31 = new FormControl('', Validators.required);
  readonly field32 = new FormControl('', Validators.required);
  private readonly formGroup: FormGroup;

  constructor(private controlContainer: ControlContainer) {
    this.formGroup = controlContainer.control as FormGroup;
    this.formGroup.registerControl('field31', this.field31);
    this.formGroup.registerControl('field32', this.field32);
  }

  ngOnDestroy(): void {
    this.formGroup.removeControl('field31');
    this.formGroup.removeControl('field32');
  }
}
