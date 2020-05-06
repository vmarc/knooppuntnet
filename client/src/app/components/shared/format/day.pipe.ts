import {Pipe, PipeTransform} from "@angular/core";
import {Day} from "../../../kpn/api/common/common/day";

@Pipe({
  name: "day"
})
export class DayPipe implements PipeTransform {

  transform(day: Day): string {
    if (day) {
      let result = day.year.toString();
      result += "-";
      result += day.month.toString().padStart(2, "0");
      if (day.day) {
        result += "-";
        result += day.day.toString().padStart(2, "0");
      }
      return result;
    }
    return "-";
  }
}
