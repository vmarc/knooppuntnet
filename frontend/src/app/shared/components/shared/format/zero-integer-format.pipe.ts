import { inject } from '@angular/core';
import { LOCALE_ID } from '@angular/core';
import { PipeTransform } from '@angular/core';
import { Pipe } from '@angular/core';

@Pipe({
  name: 'zeroInteger',
  standalone: true,
})
export class ZeroIntegerFormatPipe implements PipeTransform {
  public locale: string = inject(LOCALE_ID);

  transform(value: number): string {
    if (value) {
      let thousandsSeparator = '.';
      if (this.locale === 'fr') {
        thousandsSeparator = ' ';
      }
      return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, thousandsSeparator);
    }
    return '0';
  }
}
