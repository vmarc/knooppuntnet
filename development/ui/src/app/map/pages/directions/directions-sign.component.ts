import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-directions-sign",
  template: `
    <mat-icon svgIcon={{sign}} class="sign-icon"></mat-icon>
  `,
  styles: [`

    mat-icon {
      width: 40px;
      height: 40px;
    }

    /deep/ .sign-icon > svg {
      fill: #666666;
      stroke: #666666;
    }

  `]
})
export class DirectionsSignComponent {
  @Input() sign: string;
}
