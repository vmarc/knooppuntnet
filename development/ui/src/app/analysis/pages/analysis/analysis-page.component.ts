import {Component} from '@angular/core';
import {PageService} from "../../../shared/page.service";

@Component({
  selector: 'kpn-analysis-page',
  template: `
    <h1>
      Analysis
    </h1>

    <div class="grid">
      <div class="grid-column-1">
        <div>
          The Netherlands
        </div>
        <div>
          <a routerLink="/analysis/networks/nl/rcn">
            <mat-icon svgIcon="rcn"></mat-icon>
            <span>Cycling</span>
          </a>
        </div>
        <div>
          <a routerLink="/analysis/networks/nl/rwn">
            <mat-icon svgIcon="rwn"></mat-icon>
            <span>Hiking</span>
          </a>
        </div>
        <div>
          <a routerLink="/analysis/networks/nl/rhn">
            <mat-icon svgIcon="rhn"></mat-icon>
            <span>Horse</span>
          </a>
        </div>
        <div>
          <a routerLink="{{'/analysis/networks/nl/rmn'}}">
            <mat-icon svgIcon="rmn"></mat-icon>
            <span>Motorboat</span>
          </a>
        </div>
        <div>
          <a routerLink="/analysis/networks/nl/rpn">
            <mat-icon svgIcon="rcn"></mat-icon>
            <span>Canoe</span>
          </a>
        </div>
        <div>
          <a routerLink="/analysis/networks/nl/rin">
            <mat-icon svgIcon="rin"></mat-icon>
            <span>Inline skates</span>
          </a>
        </div>
      </div>
      <div class="grid-column-2">
        <div>
          Belgium
        </div>
        <div>
          <a routerLink="analysis/networks/be/rcn">
            <mat-icon svgIcon="rcn"></mat-icon>
            <span>Cycling</span>
          </a>
        </div>
        <div>
          <a routerLink="/analysis/networks/be/rwn">
            <mat-icon svgIcon="rwn"></mat-icon>
            <span>Hiking</span>
          </a>
        </div>
        <div>
          Germany
        </div>
        <div>
          <a routerLink="/analysis/networks/de/rcn">
            <mat-icon svgIcon="rcn"></mat-icon>
            <span>Cycling</span>
          </a>
        </div>
      </div>
    </div>

    <mat-divider></mat-divider>
    <kpn-link-overview></kpn-link-overview>
    <kpn-link-changes></kpn-link-changes>
  `,
  styles: [`
    mat-card-content {
      padding-left: 15px;
      padding-top: 15px;
      font-size: 16px;
    }

    .grid {
      display: flex;
      flex-direction: row;
    }

    .grid-column-2 {
      margin-left: 40px;
    }

    a {
      display: inline-flex;
      line-height: 20px;
      flex-direction: row;
      align-items: center;
      margin-top: 5px;
      margin-bottom: 5px;
    }

    mat-icon {
      margin-right: 10px;
      color: black;
    }

    kpn-link-overview, kpn-link-changes {
      display: block;
      line-height: 20px;
      margin-top: 5px;
      margin-bottom: 5px;
    }
  `]
})
export class AnalysisPageComponent {

  constructor(private pageService: PageService) {
  }

}
