import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { State } from '@app/state/state';
import { NzDividerComponent } from 'ng-zorro-antd/divider';

@Component({
  selector: 'ui-menu-test-actions',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="action">
      <a (click)="selectRoute(8464197)">LAW11</a>
    </div>
    <div class="action">
      <a (click)="selectRoute(16068584)">Wandelpad Calmeyn</a>
    </div>
    <div class="action">
      <a (click)="selectRoute(16075296)">Het Stroperspad</a>
    </div>
    <div class="action">
      <a (click)="selectRoute(null)">None</a>
    </div>
    <nz-divider />

    <!--    @if (extraFunctionsEnabled) {-->
    <div class="action">
      <a routerLink="/poi/areas">Point of interest areas</a>
    </div>
    <div class="action">
      <a routerLink="/poi/location">Points of interest by location</a>
    </div>
    <div class="action">
      <a routerLink="/settings">Settings</a>
    </div>
    <div class="action">
      <a routerLink="/status">Status</a>
    </div>
    <div class="action">
      <a routerLink="/symbols">Symbols</a>
    </div>
    <div class="action">
      <a (click)="forceError()">Force Sentry error</a>
    </div>

    <nz-divider />

    <div class="action">zoom: {{ zoom() }}</div>
    <div class="action">center: {{ center() }}</div>
  `,
  styles: [
    `
      .action {
        margin: 1.5em;
      }
    `,
  ],
  imports: [NzDividerComponent, RouterLink],
})
export class MenuTestActionsComponent {
  private readonly state = inject(State);
  readonly zoom = this.state.map.zoom;
  readonly center = computed(() => {
    const coordinate = this.state.map.center();
    if (coordinate) {
      return `${coordinate[0]},${coordinate[1]}`;
    }
    return '';
  });

  selectRoute(routeId: number) {
    this.state.map.updateSelectedRoute(routeId);
  }

  forceError(): void {
    throw new Error('Forced error to verify Sentry reporting');
  }
}
