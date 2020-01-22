import {Component} from "@angular/core";
import {PageService} from "../page.service";

@Component({
  selector: "kpn-toolbar",
  template: `
    <mat-toolbar>
      <button mat-icon-button (click)="toggleSidebarOpen()">
        <mat-icon svgIcon="menu"></mat-icon>
      </button>
      <button mat-button routerLink="/" class="toolbar-app-name"><h1>knooppuntnet</h1></button>
      <kpn-spinner></kpn-spinner>
      <span class="toolbar-spacer"></span>
    </mat-toolbar>
  `,
  styles: [`

    :host {
      display: block;
      border-bottom: solid 1px lightgray;
    }

    .toolbar-spacer {
      flex: 1 1 auto;
    }

    .toolbar-app-name {
      margin-left: 8px;
    }

    .mat-toolbar-row, .mat-toolbar-single-row {
      height: 47px;
    }
  `]
})
export class ToolbarComponent {

  constructor(private pageService: PageService) {
  }

  toggleSidebarOpen() {
    this.pageService.toggleSidebarOpen();
  }

}
