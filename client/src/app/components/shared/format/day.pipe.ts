import {Pipe, PipeTransform} from "@angular/core";
import {Day} from "../../../kpn/api/custom/day";
import {Util} from "../util";

@Pipe({
  name: "day"
})
export class DayPipe implements PipeTransform {

  transform(day: Day): string {
    if (day) {
      let result = day.year.toString();
      result += "-";
      result += Util.twoDigits(day.month);
      if (day.day) {
        result += "-";
        result += Util.twoDigits(day.day);
      }
      return result;
    }
    return "-";
  }

}
