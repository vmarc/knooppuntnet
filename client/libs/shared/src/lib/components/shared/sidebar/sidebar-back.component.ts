import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PageService } from '..';

@Component({
  selector: 'kpn-sidebar-back',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-nav-list>
      <mat-list-item (click)="back()">
        <mat-icon svgIcon="back" matListItemIcon />
        <span i18n="@@sidebar.back" matListItemTitle>back</span>
      </mat-list-item>
    </mat-nav-list>
  `,
  styles: [
    `
      mat-nav-list {
        padding-top: 0;
        height: 40px;
        border-bottom: 1px solid rgb(240, 240, 240);
      }

      span {
        padding-left: 10px;
      }
    `,
  ],
})
export class SidebarBackComponent {
  constructor(private pageService: PageService) {}

  back(): void {
    this.pageService.toggleSidebarOpen();
  }
}
