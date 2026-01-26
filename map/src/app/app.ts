import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  template: `
    <div class="menu">
      <ol>
        <li><a routerLink="osm-raster-tiles">OSM raster tiles</a></li>
        <li><a routerLink="osm-vector-tiles">OSM vector tiles</a></li>
        <li><a routerLink="globe">Maplibre globe demo map</a></li>
      </ol>
    </div>
    <router-outlet />
  `,
  styles: `
    .menu {
      border-top: 1px solid #ccc;
      border-bottom: 1px solid #ccc;
    }
  `,
  imports: [RouterLink, RouterOutlet],
})
export class App {}
