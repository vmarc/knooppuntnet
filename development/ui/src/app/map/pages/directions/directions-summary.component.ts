import {Component, Input} from "@angular/core";
import {Directions} from "../../../kpn/shared/directions/directions";

@Component({
  selector: "kpn-directions-summary",
  template: `
    <table class="kpn-table">
      <tr>
        <td>distance</td>
        <td>{{directions.distance}}m</td>
      </tr>
      <tr>
        <td>ascend</td>
        <td>{{directions.ascend}}m</td>
      </tr>
      <tr>
        <td>descend</td>
        <td>{{directions.descend}}m</td>
      </tr>
    </table>
  `
})
export class DirectionsSummaryComponent {
  @Input() directions: Directions;
}
