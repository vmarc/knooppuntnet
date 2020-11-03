import {ValidationErrors} from "@angular/forms";
import {FormGroup} from "@angular/forms";

export class Util {

  public static json(object: any): string {
    return JSON.stringify(object);
  }

  public static toMessages(errors: ValidationErrors | null): string[] {
    const messages: Array<string> = [];
    for (const key in errors) {
      messages.push(key);
    }
    return messages;
  }

  public static submitForm(form: FormGroup): void {
    this.setFormSubmitted(form, true);
  }

  public static resetForm(form: FormGroup): void {
    this.setFormSubmitted(form, false);
  }

  private static setFormSubmitted(form: FormGroup, submitted: boolean): void {
    if (form) {
      form["submitted"] = submitted;
      for (let controlKey in form.controls) {0
        if (form.controls.hasOwnProperty(controlKey)) {
          const control = form.controls[controlKey];
          if (control["submitted"]) {
            control["submitted"].next(submitted);
          }
        }
      }
    }
  }

}
