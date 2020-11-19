import {Component} from '@angular/core';

@Component({
  selector: 'app-tabs',
  template: `
    <nz-tabset nzLinkRouter nzSize="small" nzType="card">
      <nz-tab>
        <a *nzTabLink nz-tab-link [routerLink]="['/analysis/nodes']">
          <span>Nodes</span>
          <app-badge [count]="1024"></app-badge>
        </a>
      </nz-tab>
      <nz-tab>
        <a *nzTabLink nz-tab-link [routerLink]="['/analysis/routes']">
          <span>Routes</span>
          <app-badge [count]="66"></app-badge>
        </a>
      </nz-tab>
      <nz-tab>
        <a *nzTabLink nz-tab-link [routerLink]="['/analysis/facts']">
          <span>Facts</span>
          <app-badge [count]="99"></app-badge>
        </a>
      </nz-tab>
    </nz-tabset>
  `,
  styles: [`
    .count {
      color: gray;
    }
  `]
})
export class TabsComponent {

}
