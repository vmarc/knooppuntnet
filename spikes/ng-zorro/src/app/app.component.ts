import {Component} from '@angular/core';

@Component({
  selector: 'app-root',
  template: `
    <nz-layout class="app-layout">
      <nz-sider class="menu-sidebar"
                nzCollapsible
                nzWidth="256px"
                nzTheme="light"
                nzBreakpoint="md"
                [(nzCollapsed)]="isCollapsed"
                [nzTrigger]="null">
        <app-sider></app-sider>
      </nz-sider>

      <nz-layout>
        <nz-header>
          <div class="app-header">
            <span class="header-trigger" (click)="isCollapsed = !isCollapsed">
                <i class="trigger"
                   nz-icon
                   [nzType]="isCollapsed ? 'menu-unfold' : 'menu-fold'"
                ></i>
            </span>
          </div>
        </nz-header>
        <nz-content>
          <div class="inner-content">
            <router-outlet></router-outlet>
          </div>
        </nz-content>
      </nz-layout>
    </nz-layout>

  `,
  styles: [`
    :host {
      display: flex;
      text-rendering: optimizeLegibility;
      -webkit-font-smoothing: antialiased;
      -moz-osx-font-smoothing: grayscale;
    }

    .app-layout {
      height: 100vh;
    }

    .menu-sidebar {
      position: relative;
      z-index: 10;
      min-height: 100vh;
      box-shadow: 2px 0 6px rgba(0, 21, 41, .35);
    }

    .header-trigger {
      height: 64px;
      padding: 20px 24px;
      font-size: 20px;
      cursor: pointer;
      transition: all .3s, padding 0s;
    }

    .trigger:hover {
      color: #1890ff;
    }

    nz-header {
      padding: 0;
      width: 100%;
      z-index: 2;
    }

    .app-header {
      position: relative;
      height: 64px;
      padding: 0;
      background: #fff;
      box-shadow: 0 1px 4px rgba(0, 21, 41, .08);
    }

    nz-content {
      /*margin: 24px;*/
    }

    .inner-content {
      padding: 24px;
      background: #fff;
      height: 100%;
    }

  `]
})
export class AppComponent {
  isCollapsed = false;
}
