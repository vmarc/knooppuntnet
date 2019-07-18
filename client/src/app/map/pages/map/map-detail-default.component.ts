import {Component} from "@angular/core";

@Component({
  selector: "kpn-map-detail-default",
  template: `
    <div>
      Click network... <!-- node or route for more details, after zooming in sufficiently.-->
      <!--@@ Zoom in en klik op knooppunt of route voor verdere details. -->
    </div>
    <div class="note">
      The map contents... <!--is currently updated once every two hours.-->
      <!--@@ De kaart wordt momenteel om de twee uur automatisch aangepast aan de meest recente informatie in OpenStreetMap.-->
    </div>
  `,
  styles: [`
    .note {
      padding-top: 40px;
      font-style: italic;
    }
  `]
})
export class MapDetailDefaultComponent {

}
