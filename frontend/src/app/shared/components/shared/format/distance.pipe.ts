import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'distance',
  standalone: true,
})
export class DistancePipe implements PipeTransform {
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
    return `${+(meters / 1000).toFixed()} km`;
  }
}
