import {Component} from "@angular/core";
import {PageService} from "../../components/shared/page.service";

@Component({
  selector: "kpn-via-route",
  template: `
    <kpn-video videoSource="https://knooppuntnet.nl/public/demo3.mp4">
    </kpn-video>
  `
})
export class ViaLoopRouteComponent {

  constructor(private pageService: PageService) {
    this.pageService.showFooter = false;
  }
}
