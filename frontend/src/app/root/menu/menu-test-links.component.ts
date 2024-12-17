import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'kpn-menu-test-links',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p class="links-title">Temporary test links:</p>

    <ul>
      <li>
        <a routerLink="analysis/route/6376622">Route 01-02</a>
      </li>
      <li>
        <a routerLink="analysis/route/7973533">LAW9 Pieterpad</a>
      </li>
      <li>
        <a routerLink="analysis/route/16068584">Wandelpad Calmeyn</a>
      </li>
      <li>
        <a routerLink="analysis/hiking/be/België:Oost-Vlaanderen:Aalst/details">Location Aalst</a>
      </li>
      <li>
        <a routerLink="analysis/hiking/be/networks">Subset Belgium</a>
      </li>
    </ul>
  `,
  styles: [
    `
      .links-title {
        margin-left: 1.5em;
      }

      li {
        margin-left: 1.5em;
        margin-top: 1em;
        margin-bottom: 1em;
      }
    `,
  ],
  imports: [RouterLink],
})
export class MenuTestLinksComponent {}
