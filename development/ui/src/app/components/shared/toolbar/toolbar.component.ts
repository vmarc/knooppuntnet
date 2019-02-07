import {Component, OnInit} from '@angular/core';
import {PageService} from "../page.service";

@Component({
  selector: 'kpn-toolbar',
  template: `
    <mat-toolbar>
      <button mat-icon-button (click)="toggleSidebarOpen()">
        <mat-icon svgIcon="menu"></mat-icon>
      </button>
      <button mat-button routerLink="/" class="toolbar-app-name"><h1>Knooppuntnet</h1></button>
      <span class="toolbar-spacer"></span>
    </mat-toolbar>
  `,
  styles: [`
    .toolbar-spacer {
      flex: 1 1 auto;
    }

    .toolbar-app-name {
      margin-left: 8px;
    }

    .mat-toolbar-row, .mat-toolbar-single-row {
      height: 48px;
    }
  `]
})
export class ToolbarComponent implements OnInit {

  constructor(private pageService: PageService) {
  }

  ngOnInit() {
  }

  toggleSidebarOpen() {
    this.pageService.toggleSidebarOpen();
  }

}
