import {Component, Input} from "@angular/core";

@Component({
  selector: "app-classes",
  template: `
    <span
      *ngFor="let className of classNames()"
      [ngClass]="{'special': isSpecial(className), 'extra-special': isExtraSpecial(className)}">

      {{className}}
    </span>
  `,
  styles: [`
    .special {
      color: red;
    }

    .extra-special {
      color: red;
      font-weight: bold;
    }
  `]
})
export class ClassesComponent {

  @Input() classes: string;

  classNames(): string[] {
    return this.classes.split(" ");
  }

  isSpecial(className: string): boolean {
    return className.indexOf("ng-") >= 0;
  }

  isExtraSpecial(className: string): boolean {
    return className.indexOf("mat-form-field-invalid") >= 0 ||
      className.indexOf("our-own-submitted") >= 0;
  }

}
