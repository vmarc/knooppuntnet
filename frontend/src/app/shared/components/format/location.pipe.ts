import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'location',
})
export class LocationPipe implements PipeTransform {
  private subStrings = [
    'Communauté de communes de la '.toLowerCase(),
    'Communauté de communes de '.toLowerCase(),
    'Communauté de communes des '.toLowerCase(),
    `Communauté de communes du canton de La `.toLowerCase(),
    'Communauté de communes du '.toLowerCase(),
    `Communauté d'agglomération du `.toLowerCase(),
    `Communauté d'agglomération de la `.toLowerCase(),
    `Communauté d’agglomération de `.toLowerCase(),
    `Communauté d'agglomération des `.toLowerCase(),
    `Communauté d'agglomération `.toLowerCase(),
    'Communauté de communes '.toLowerCase(),
    'Communauté urbaine de '.toLowerCase(),
    `Communauté urbaine `.toLowerCase(),
    'Communauté territoriale du '.toLowerCase(),
    ' agglomération'.toLowerCase(),
    ` Communauté d'agglomération`.toLowerCase(),
    ` Communauté`.toLowerCase(),
  ];

  transform(location: string): string {
    const name = location.toLowerCase();
    const matchingSubstring = this.subStrings.find((subString) => name.includes(subString));
    if (matchingSubstring) {
      const index = name.indexOf(matchingSubstring);
      if (index == 0) {
        return location.substring(matchingSubstring.length);
      }
      const before = location.substring(0, index);
      const after = location.substring(index + matchingSubstring.length);
      return before + after;
    }
    return location;
  }
}
