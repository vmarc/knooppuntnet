import {Component, Input} from "@angular/core";
import {Reference} from "../../../kpn/shared/common/reference";

@Component({
  selector: 'icon-link',
  template: `
    <kpn-network-type-icon [networkType]="reference.networkType"></kpn-network-type-icon>
    <a class="text" [routerLink]="url">{{reference.name}}</a>
    <mat-icon *ngIf="reference.connection">link</mat-icon>
  `,
  styles: [`
    a {
      vertical-align: top;
      padding-left: 10px;
      padding-right: 10px;
      line-height: 24px;
    }
  `]
})
export class IconLinkComponent {
  @Input() reference: Reference;
  @Input() url: string;
}
