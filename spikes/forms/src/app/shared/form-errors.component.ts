import {Component, Input} from "@angular/core";
import {FormGroup} from "@angular/forms";
import {Util} from "./util";

@Component({
  selector: "app-form-errors",
  template: `
    <div *ngIf="show()" class="errors">
      <div *ngFor="let error of util.toMessages(form.errors)">
        {{error}}
      </div>
    </div>
  `,
  styles: [`
    .errors {
      margin-top: 2em;
      color: red;
    }
  `]
})

export class FormErrorsComponent {

  util = Util;

  @Input() form: FormGroup;

  show(): boolean {
    return this.form && this.form.invalid && this.form["submitted"];
  }

}
