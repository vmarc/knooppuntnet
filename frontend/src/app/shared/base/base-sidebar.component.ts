import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { SidebarComponent } from '@app/components/shared/sidebar';

@Component({
  selector: 'kpn-base-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <kpn-sidebar>
      <div id="extraFunctions" (click)="toggleExtraFunctions()" class="extra-functions"></div>

      <ul *ngIf="extraFunctionsEnabled">
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
        <li>
          <p>
            <a routerLink="/symbols">Symbols</a>
          </p>
        </li>
      </ul>
    </kpn-sidebar>
  `,
  styles: `
    .extra-functions {
      float: right;
      width: 20px;
      height: 20px;
    }
  `,
  standalone: true,
  imports: [SidebarComponent, NgIf, RouterLink],
})
export class BaseSidebarComponent {
  extraFunctionsEnabled = false;

  toggleExtraFunctions(): void {
    this.extraFunctionsEnabled = !this.extraFunctionsEnabled;
  }
}
