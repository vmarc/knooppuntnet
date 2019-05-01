import {Component, Input} from "@angular/core";
import {Timestamp} from "../../../kpn/shared/timestamp";

@Component({
  selector: "kpn-situation-on",
  template: `
    <ng-container i18n="@@situation-on">Situation on</ng-container>:
    <kpn-timestamp [timestamp]="timestamp"></kpn-timestamp>
  `
})
export class SituationOnComponent {

  @Input() timestamp: Timestamp;

}
