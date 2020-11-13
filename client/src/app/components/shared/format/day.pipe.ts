import {Pipe, PipeTransform} from "@angular/core";
import {Day} from "../../../kpn/api/custom/day";

@Pipe({
  name: "day"
})
export class DayPipe implements PipeTransform {

  transform(day: Day): string {
    if (day) {
      let result = day.year.toString();
      result += "-";
      result += this.twoDigits(day.month);
      if (day.day) {
        result += "-";
        result += this.twoDigits(day.day);
      }
      return result;
    }
    return "-";
  }

  private twoDigits(value: number): string {
    return (value < 10 ? "0" : "") + value;
  }

}
