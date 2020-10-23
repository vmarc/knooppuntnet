import {Component} from "@angular/core";
import {ViewChild} from "@angular/core";
import {PageService} from "../../components/shared/page.service";
import {VideoComponent} from "../components/video.component";

@Component({
  selector: "kpn-plan-a-route",
  template: `
    <kpn-video
      videoSource="https://knooppuntnet.nl/public/demo1.mp4"
      (canPlay)="canplay()"
      (currentTime)="currentTimeChanged($event)">
    </kpn-video>
  `
})
export class DemoPlanRouteComponent {

  canPlay = false;
  currentTime = 0;

  @ViewChild(VideoComponent, {static: false}) video: VideoComponent;

  constructor(private pageService: PageService) {
    this.pageService.showFooter = false;
  }

  start() {
    this.video.play();
  }

  canplay(): void {
    this.canPlay = true;
    this.video.play();
  }

  currentTimeChanged(currentTime: number): void {
    this.currentTime = currentTime;
  }

  isText(textId: number): boolean {
    if (this.video) {
      const now = this.currentTime;
      return now < textId * 10 && now > (textId - 1) * 10;
    }
    return false;
  }
}
