import {Component} from '@angular/core';

@Component({
  selector: 'app-header',
  template: `
    <nz-breadcrumb>
      <nz-breadcrumb-item>
        <a routerLink="/analysis"><i nz-icon nzType="home"></i></a>
      </nz-breadcrumb-item>
      <nz-breadcrumb-item>
        <a routerLink="/analysis"><i nz-icon nzType="user"></i><span>Analysis</span></a>
      </nz-breadcrumb-item>
      <nz-breadcrumb-item>
        Network
      </nz-breadcrumb-item>
    </nz-breadcrumb>

    <h2>Network</h2>
    <app-tabs></app-tabs>
  `
})
export class HeaderComponent {

}
