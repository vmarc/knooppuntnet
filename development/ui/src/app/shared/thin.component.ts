import {Component} from "@angular/core";

@Component({
  selector: 'kpn-thin',
  template: `
    <ng-content></ng-content>
  `,
  styles: [`
    :host {
      display: inline;
      color: gray;

    /deep/ a {
      color: rgba(0, 0, 255, 0.7);
    }

    }
  `]
})
export class ThinComponent {
}
