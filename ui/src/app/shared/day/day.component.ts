import {Component, Input} from "@angular/core";
import {Timestamp} from "../../kpn/shared/timestamp";

@Component({
  selector: 'day',
  templateUrl: './day.component.html',
  styleUrls: ['./day.component.scss']
})
export class DayComponent {

  @Input() timestamp: Timestamp;

  formattedTimestamp() {
    if (!this.timestamp) {
      return "";
    }
    return this.timestamp.year.toString() + "-" +
      this.digits(this.timestamp.month) + "-" +
      this.digits(this.timestamp.day);
  }

  private digits(n: number): string {
    return (100 + n).toString().substr(1);
  }
}
