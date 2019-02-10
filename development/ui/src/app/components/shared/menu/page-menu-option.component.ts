import {Component, Input} from '@angular/core';

@Component({
  selector: 'kpn-page-menu-option',
  template: `
    <a [ngClass]="{'selected': selectedPageName === pageName}" [routerLink]="link">
      {{pageTitle}}
      <div *ngIf="elementCount != null" class="kpn-thin"> ({{elementCount}})</div>
    </a>
  `,
  styles: [`
    .selected {
      color: rgba(0, 0, 0, 0.87);
      font-weight: bold;
    }
  `]
})
export class PageMenuOptionComponent {

  @Input() pageName: string;
  @Input() selectedPageName: string;
  @Input() link: string;
  @Input() pageTitle: string;
  @Input() elementCount: number;

}
