import {Component, Input} from "@angular/core";
import {Timestamp} from "../../../kpn/shared/timestamp";

@Component({
  selector: "kpn-timestamp",
  template: `
    {{formattedTimestamp()}}
  `
})
export class TimestampComponent {

  @Input() timestamp: Timestamp;

  formattedTimestamp() {
    if (!this.timestamp) {
      return "";
    }
    return this.timestamp.year.toString() + "-" +
      this.digits(this.timestamp.month) + "-" +
      this.digits(this.timestamp.day) + " " +
      this.digits(this.timestamp.hour) + ":" +
      this.digits(this.timestamp.second);
  }

  private digits(n: number): string {
    return (100 + n).toString().substr(1);
  }
}
