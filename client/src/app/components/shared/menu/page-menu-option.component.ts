import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-page-menu-option",
  template: `
    <a  [routerLink]="link" [state]="state" routerLinkActive="active" [routerLinkActiveOptions]="{exact: true}">
      {{pageTitle}}
      <span *ngIf="elementCount != null" class="element-count"> ({{elementCount}})</span>
    </a>
  `,
  styles: [`

    .active {
      color: rgba(0, 0, 0, 0.87);
      font-weight: bold;
    }

    .element-count {
      color: gray;
      font-weight: normal;
    }

  `]
})
export class PageMenuOptionComponent {
  @Input() link: string;
  @Input() pageTitle: string;
  @Input() state: {[k: string]: any;};
  @Input() elementCount: number;
}
