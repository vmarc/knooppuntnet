import { ValidatorFn } from '@angular/forms';
import { AbstractControl } from '@angular/forms';

export const urlFragmentValidator: ValidatorFn = (c: AbstractControl) => {
  const value = c.value;
  const regex = /^[a-zA-Z0-9-]+$/i;
  if (!value || value.length === 0 || regex.test(value)) {
    return null;
  }
  return { urlFragmentInvalid: { value } };
};
