import {Component} from "@angular/core";

@Component({
  selector: 'kpn-thick',
  template: `
    <ng-content></ng-content>
  `,
  styles: [`
    :host {
      display: inline;
      font-weight: bold;
    }
  `]
})
export class ThickComponent {
}
