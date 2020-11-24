import {Component} from '@angular/core';
import {OnInit} from "@angular/core";
import {ControlContainer} from '@angular/forms';
import {FormGroup} from '@angular/forms';
import {FormControl} from "@angular/forms";

@Component({
  selector: 'app-sub-5',
  template: `

    <div class="fields">

      <label>Field 3</label>
      <input ourOwnInput [formControl]="field3">
      <app-field-errors [control]="field3"></app-field-errors>

      <label>Field 4</label>
      <input ourOwnInput #last [formControl]="field4">
      <app-field-errors [control]="field4"></app-field-errors>

    </div>
  `
})
export class Sub5Component implements OnInit {

  field3: FormControl;
  field4: FormControl;

  constructor(private controlContainer: ControlContainer) {
  }

  ngOnInit(): void {
    const formGroup = this.controlContainer.control as FormGroup;
    this.field3 = formGroup.get('field3') as FormControl;
    this.field4 = formGroup.get('field4') as FormControl;
  }

}
