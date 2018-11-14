import {Component, OnInit} from '@angular/core';
import {PageService} from "../page.service";

@Component({
  selector: 'kpn-toolbar',
  template: `
    <mat-toolbar>
      <button mat-icon-button (click)="toggleSideNavOpen()">
        <mat-icon>menu</mat-icon>
      </button>
      <button mat-button routerLink="/home" class="toolbar-app-name"><h1>Knooppuntnet</h1></button>
      <span class="toolbar-spacer"></span>
      <button mat-icon-button>
        <mat-icon>directions_bike</mat-icon>
      </button>
      <button mat-icon-button>
        <mat-icon>directions_walk</mat-icon>
      </button>
      <button mat-icon-button>
        <mat-icon>my_location</mat-icon>
      </button>
      <button mat-icon-button>
        <mat-icon>map</mat-icon>
      </button>
      <button mat-icon-button>
        <mat-icon>place</mat-icon>
      </button>
      <button mat-icon-button>
        <mat-icon>more_vert</mat-icon>
      </button>
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

  toggleSideNavOpen() {
    this.pageService.toggleSideNavOpen();
  }

}
