import { LOCALE_ID } from '@angular/core';
import { inject } from '@angular/core';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'distance',
  standalone: true,
})
export class DistancePipe implements PipeTransform {
  public locale: string = inject(LOCALE_ID);

  transform(meters: number): string {
    if (!meters) {
      return `-`;
    }
    if (meters < 1000) {
      return `${meters} m`;
    }
    if (meters < 9950) {
      return `${+(meters / 1000).toFixed(1)} km`.replace('.', ',');
    }
    let thousandsSeparator = '.';
    if (this.locale === 'fr') {
      thousandsSeparator = '\u2009'; // thin space
    }
    const value = (meters / 1000)
      .toFixed()
      .toString()
      .replace(/\B(?=(\d{3})+(?!\d))/g, thousandsSeparator);
    return `${value} km`;
  }
}
