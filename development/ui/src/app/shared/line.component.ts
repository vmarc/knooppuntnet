import {Component} from "@angular/core";

@Component({
  selector: 'kpn-line',
  template: `
    <div class="line">
      <ng-content></ng-content>
    </div>
  `,
  styleUrls: [
    "./line.component.scss"
  ]
})
export class LineComponent {
}
