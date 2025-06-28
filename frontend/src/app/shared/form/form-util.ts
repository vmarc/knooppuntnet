import { FormGroupDirective } from '@angular/forms';
import { FormControl } from '@angular/forms';

export class FormUtil {
  static validateStatus(form: FormGroupDirective, formControl: FormControl): string {
    if (
      formControl.invalid &&
      formControl.errors &&
      (formControl.dirty || formControl.touched || form.submitted)
    ) {
      return 'error';
    }
    if (formControl.valid) {
      return 'success';
    }
    return undefined;
  }
}
