import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-page-menu-option",
  template: `
    <a [routerLink]="link" [state]="state" routerLinkActive="active" [routerLinkActiveOptions]="{exact: true}" class="link">
      <ng-content></ng-content>
      <span *ngIf="elementCount != null" class="element-count"> ({{elementCount}})</span>
    </a>
  `,
  styles: [`

    .link {
      white-space: nowrap;      
    }
    
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
