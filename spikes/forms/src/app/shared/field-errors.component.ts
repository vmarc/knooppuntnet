import {Component, Input} from "@angular/core";
import {AbstractControl} from "@angular/forms";
import {Util} from "./util";
import {BehaviorSubject} from "rxjs";

@Component({
  selector: "app-field-errors",
  template: `
    <div *ngIf="show()" class="errors">
      <div *ngFor="let error of util.toMessages(control.errors)">
        {{error}}
      </div>
    </div>
  `,
  styles: [`
    .errors {
      color: red;
    }
  `]
})
export class FieldErrorsComponent {

  util = Util;

  @Input() control: AbstractControl;

  show(): boolean {
    const submitted$: BehaviorSubject<boolean> = this.control["submitted"];
    return this.control &&
      this.control.invalid && (
        this.control.dirty ||
        this.control.touched ||
        (submitted$ && submitted$.value)
      );
  }

}
