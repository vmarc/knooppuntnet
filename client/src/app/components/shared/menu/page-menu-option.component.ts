import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-page-menu-option",
  template: `
    <a [routerLink]="link" [state]="state" routerLinkActive="active" [routerLinkActiveOptions]="{exact: true}">
      <ng-content></ng-content>
      <span *ngIf="elementCount != null" class="element-count"> ({{elementCount}})</span>
    </a>
  `,
  styles: [`

    .active {
      font-weight: bold;
      color: black;
    }

    .element-count {
      color: gray;
      font-weight: normal;
    }

  `]
})
export class PageMenuOptionComponent {
  @Input() link: string;
  @Input() state: { [k: string]: any; };
  @Input() elementCount: number;
}
