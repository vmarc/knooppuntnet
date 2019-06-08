import {Component} from "@angular/core";

@Component({
  selector: "app-container",
  template: `
    <div id="header-container">
      <app-header-container></app-header-container>
    </div>
    <div id="map-route-container">
      <app-map-route-container></app-map-route-container>
    </div>
  `,
  styles: [`

    #header-container {
      height: 10%;
      width: 100%;
      position: relative;
      left: 0;
    }

    #map-route-container {
      height: 90%;
      width: 100%;
      position: relative;
      left: 0;
    }
  `]
})
export class ContainerComponent {
}
