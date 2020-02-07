import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-sidebar-menu",
  template: `
    <mat-nav-list>
      <mat-list-item (click)="toggleOpen()">
        <mat-icon svgIcon="expand"*ngIf="open" mat-list-icon></mat-icon>
        <mat-icon svgIcon="collapse"*ngIf="!open" mat-list-icon></mat-icon>
        <span>{{title}}</span>
      </mat-list-item>
      <mat-nav-list *ngIf="open">
        <ng-content></ng-content>
      </mat-nav-list>
    </mat-nav-list>
  `,
  styles: [`

    ::ng-deep .mat-list-icon > svg {
      width: 12px;
      height: 12px;
      vertical-align: top;
      padding-top: 7px;
    }

    ::ng-deep .mat-nav-list {
      padding-top: 0 !important;
    }

    ::ng-deep .mat-list-item {
      height: 32px !important;
    }

    ::ng-deep .mat-list-item-with-avatar {
      height: 32px !important;
    }

  `]
})
export class SidebarMenuComponent {

  @Input() open = true;
  @Input() title: string;

  toggleOpen(): void {
    this.open = !this.open;
  }

}
