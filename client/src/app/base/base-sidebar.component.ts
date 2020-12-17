import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';

/* tslint:disable:template-i18n English only */
@Component({
  selector: 'kpn-base-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>

      <div id="extraFunctions" (click)="toggleExtraFunctions()" class="extra-functions"></div>

      <ul *ngIf="extraFunctionsEnabled">
        <li>
          <p>
            <a routerLink="/monitor/long-distance-routes">Long distance routes</a>
          </p>
        </li>
        <li>
          <p>
            <a routerLink="/monitor">Route monitor</a>
          </p>
        </li>
        <li>
          <p>
            <a routerLink="/poi/areas">Point of interest areas</a>
          </p>
        </li>
        <li>
          <p>
            <a routerLink="/status">Status</a>
          </p>
        </li>
      </ul>
    </kpn-sidebar>
  `,
  styles: [`
    .extra-functions {
      float: right;
      width: 20px;
      height: 20px;
    }
  `]
})
export class BaseSidebarComponent {

  extraFunctionsEnabled = false;

  toggleExtraFunctions(): void {
    this.extraFunctionsEnabled = !this.extraFunctionsEnabled;
  }

}
