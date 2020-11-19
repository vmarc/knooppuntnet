import {Component} from '@angular/core';

@Component({
  selector: 'app-analysis',
  template: `
    <nz-breadcrumb>
      <nz-breadcrumb-item>
        <i nz-icon nzType="home"></i>
      </nz-breadcrumb-item>
      <nz-breadcrumb-item>
        <a><i nz-icon nzType="user"></i><span>Analysis</span></a>
      </nz-breadcrumb-item>
      <nz-breadcrumb-item>
        Detail
      </nz-breadcrumb-item>
    </nz-breadcrumb>

    <h2>
      Analysis
    </h2>

    <ul>
      <li><a routerLink="/analysis/nodes">Nodes</a></li>
      <li><a routerLink="/analysis/routes">Routes</a></li>
      <li><a routerLink="/analysis/facts">Facts</a></li>
    </ul>
  `
})
export class AnalysisComponent {
}
