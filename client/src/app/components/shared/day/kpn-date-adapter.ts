import { NativeDateAdapter } from '@angular/material/core';
import { DayUtil } from '../day-util';

export class KpnDateAdapter extends NativeDateAdapter {
  private readonly enRegex = /^(\d{4})-(\d\d?)-(\d\d?)$/;
  private readonly nlRegex = /^(\d\d?)-(\d\d?)-(\d{4})$/;
  private readonly deRegex = /^(\d\d?)\.(\d\d?)\.(\d{4})$/;
  private readonly frRegex = /^(\d\d?)\/(\d\d?)\/(\d{4})$/;

  constructor(public matDateLocale: string) {
    super(matDateLocale);
  }

  public parse(value: any, parseFormat?: any): Date | null {
    let result = null;
    if (value) {
      let regex = this.enRegex;
      if (this.matDateLocale === 'nl') {
        regex = this.nlRegex;
      } else if (this.matDateLocale === 'de') {
        regex = this.deRegex;
      } else if (this.matDateLocale === 'fr') {
        regex = this.frRegex;
      }
      if (value.match(regex)) {
        const arr = regex.exec(value);
        if (this.matDateLocale === 'en') {
          const year = arr[1];
          const month = arr[2].padStart(2, '0');
          const day = +arr[3].padStart(2, '0');
          return DayUtil.toDate(`${year}-${month}-${day}`);
        }
        const year = arr[3];
        const month = arr[2].padStart(2, '0');
        const day = +arr[1].padStart(2, '0');
        return DayUtil.toDate(`${year}-${month}-${day}`);
      }
    }
    return result;
  }

  format(date: Date, displayFormat: Object): string {
    return DayUtil.toString(this.matDateLocale, DayUtil.toDay(date));
  }
}
