import {Component} from '@angular/core';

@Component({
  selector: 'app-sider',
  template: `
    <div class="sidebar-logo">
      <h1>Knooppuntnet</h1>
    </div>
    <ul nz-menu nzTheme="light" nzMode="inline" [nzInlineCollapsed]="isCollapsed">
      <li nz-submenu nzOpen nzTitle="Dashboard" nzIcon="dashboard">
        <ul>
          <li nz-menu-item nzMatchRouter>
            <a routerLink="/welcome">Welcome</a>
          </li>
          <li nz-menu-item nzMatchRouter>
            <a>Monitor</a>
          </li>
          <li nz-menu-item nzMatchRouter>
            <a>Workplace</a>
          </li>
        </ul>
      </li>
      <li nz-submenu nzOpen nzTitle="Form" nzIcon="form">
        <ul>
          <li nz-menu-item nzMatchRouter>
            <a>Basic Form</a>
          </li>
        </ul>
      </li>
    </ul>
  `,
  styles: [`

    .sidebar-logo {
      position: relative;
      height: 64px;
      padding-left: 24px;
      overflow: hidden;
      line-height: 64px;
      transition: all .3s;
    }

    .sidebar-logo img {
      display: inline-block;
      height: 32px;
      width: 32px;
      vertical-align: middle;
    }

    .sidebar-logo h1 {
      display: inline-block;
      margin: 0 0 0 20px;
      font-weight: 600;
      font-size: 14px;
      font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
      vertical-align: middle;
    }

  `]
})
export class SiderComponent {

  isCollapsed = false;
}
