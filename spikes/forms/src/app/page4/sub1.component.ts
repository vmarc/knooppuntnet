import {Component} from '@angular/core';
import {OnDestroy} from "@angular/core";
import {FormControl} from '@angular/forms';
import {Validators} from '@angular/forms';
import {ControlContainer} from "@angular/forms";
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'app-sub-1',
  template: `
    <div class="fields">

      <label>Field 11</label>
      <input ourOwnInput [formControl]="field11">
      <app-field-errors [control]="field11"></app-field-errors>

      <label>Field 12</label>
      <input ourOwnInput #last [formControl]="field12">
      <app-field-errors [control]="field12"></app-field-errors>

    </div>
  `
})
export class Sub1Component implements OnDestroy {

  readonly field11 = new FormControl('', Validators.required);
  readonly field12 = new FormControl('', Validators.required);
  private readonly formGroup: FormGroup;

  constructor(private controlContainer: ControlContainer) {
    this.formGroup = controlContainer.control as FormGroup;
    this.formGroup.registerControl("field11", this.field11);
    this.formGroup.registerControl("field12", this.field12);
  }

  ngOnDestroy(): void {
    this.formGroup.removeControl("field11");
    this.formGroup.removeControl("field12");
  }
}
