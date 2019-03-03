import {Component, Input} from '@angular/core';
import {DirectionsPath} from "../../../kpn/shared/directions/directions-path";

@Component({
  selector: 'kpn-directions-summary',
  template: `
    <table class="kpn-table">
      <tr>
        <td>distance</td>
        <td>{{distance()}}</td>
      </tr>
      <tr>
        <td>ascend</td>
        <td>{{ascend()}}</td>
      </tr>
      <tr>
        <td>descend</td>
        <td>{{descend()}}</td>
      </tr>
    </table>
  `
})
export class DirectionsSummaryComponent {

  @Input() path: DirectionsPath;

  distance() {
    return this.toMeters(this.path.distance);
  }

  ascend() {
    return this.toMeters(this.path.ascend);
  }

  descend() {
    return this.toMeters(this.path.descend);
  }

  private toMeters(value: number): string {
    return "" + Math.round(value) + "m";
  }

  time() {
    const seconds = this.path.time / 1000;
    if (seconds < 60) {
      return "" + Math.round(seconds) + " seconds";
    }
    const minutes = seconds / 60;
    return "" + Math.round(minutes) + " minutes";
  }

}
