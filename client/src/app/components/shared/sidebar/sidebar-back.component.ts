import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {PageService} from '../page.service';

@Component({
  selector: 'kpn-sidebar-back',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-nav-list>
      <mat-list-item (click)="back()">
        <mat-icon svgIcon="back" mat-list-icon></mat-icon>
        <span i18n="@@sidebar.back">back</span>
      </mat-list-item>
    </mat-nav-list>
  `,
  styles: [`

    mat-nav-list {
      padding-top: 0;
      height: 48px;
      border-bottom: 1px solid rgb(240, 240, 240);
    }

    .mat-list-base .mat-list-item.mat-list-item-with-avatar {
      height: 48px;
    }

    span {
      padding-left: 10px;
    }

  `]
})
export class SidebarBackComponent {

  constructor(private pageService: PageService) {
  }

  back(): void {
    this.pageService.toggleSidebarOpen();
  }

}
