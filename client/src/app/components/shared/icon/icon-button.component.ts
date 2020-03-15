import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-icon-button",
  template: `
    <a [routerLink]="routerLink">
      <div class="wrapper">
        <mat-icon [svgIcon]="icon" class="icon"></mat-icon>
      </div>
      <div class="text">
        <ng-content></ng-content>
      </div>
    </a>
  `,
  styles: [`

    :host {
      display: inline-block;
      margin: 15px;
    }

    a:hover {
      cursor: pointer;
    }

    .wrapper {
      display: inline-block;
      border-color: gray;
      border-radius: 50%;
      border-style: solid;
      border-width: 3px;
      width: 100px;
      height: 100px;
      background: #f8f8f8;
    }

    .wrapper:hover {
      background-color: #e8ebee;
    }

    .text {
      width: 100px;
      margin-top: 5px;
      text-align: center;
    }

    .icon {
      position: relative;
      top: 25px;
      left: 25px;
      width: 50px;
      height: 50px;
      border: none;
      color: gray;
    }

  `]
})
export class IconButtonComponent {
  @Input() routerLink: string;
  @Input() icon: string;
}
