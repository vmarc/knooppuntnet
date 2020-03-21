import {PipeTransform} from "@angular/core";
import {Pipe} from "@angular/core";

@Pipe({name: "integer"})
export class IntegerFormatPipe implements PipeTransform {
  transform(value: number): string {
    if (!!value) {
      return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
    }
    return "";
  }
}
