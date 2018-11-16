import {Component, Input} from '@angular/core';

@Component({
  selector: 'kpn-sidenav-menu',
  template: `
    <mat-nav-list>
      <mat-list-item (click)="toggleOpen()">
        <mat-icon *ngIf="open" mat-list-icon>arrow_drop_down</mat-icon>
        <mat-icon *ngIf="!open" mat-list-icon>arrow_right</mat-icon>
        <span>{{title}}</span>
      </mat-list-item>
      <mat-nav-list *ngIf="open">
        <ng-content></ng-content>
      </mat-nav-list>
    </mat-nav-list>
  `,
  styles: [`
    /deep/ .mat-nav-list {
      padding-top: 0 !important;
    }

    /deep/ .mat-list-item {
      height: 32px !important;
    }

    /deep/ .mat-list-item-with-avatar {
      height: 32px !important;
    }
    
  `]
})
export class SidenavMenuComponent {

  @Input() open: boolean = true;
  @Input() title: string;

  toggleOpen(): void {
    this.open = !this.open;
  }

}
