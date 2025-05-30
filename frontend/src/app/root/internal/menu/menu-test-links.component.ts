import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'ui-menu-test-links',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>
      <a routerLink="analysis/route/1788296">Route with many members</a>
    </div>
    <div>
      <a routerLink="analysis/route/6376622">Route 01-02</a>
    </div>
    <div>
      <a routerLink="analysis/route/7973533">LAW9 Pieterpad</a>
    </div>
    <div>
      <a routerLink="analysis/route/16068584">Wandelpad Calmeyn</a>
    </div>
    <div>
      <a routerLink="analysis/hiking/be/België:Oost-Vlaanderen:Aalst/details">Location Aalst</a>
    </div>
    <div>
      <a routerLink="analysis/hiking/be/networks">Subset Belgium</a>
    </div>
  `,
  styles: [
    `
      div {
        margin-left: 1.5em;
        margin-top: 1em;
        margin-bottom: 1em;
      }
    `,
  ],
  imports: [RouterLink],
})
export class MenuTestLinksComponent {}
