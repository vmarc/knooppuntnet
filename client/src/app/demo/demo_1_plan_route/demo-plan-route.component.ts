import {Component, OnInit} from "@angular/core";
import {ViewChild} from "@angular/core";
import {PageService} from "../../components/shared/page.service";
import {VideoComponent} from "../components/video.component";

@Component({
  selector: "kpn-plan-a-route",
  template: `
    <h1 class="header">
      Video demo - How to plan a route?
    </h1>

    <div class="instructions">
      <div>
        You can select the "Map" icon on the home page to start planning a route.
      </div>

      <div class="kpn-spacer-above kpn-spacer-below">
        <button mat-raised-button color="primary" (click)="start()">
          <mat-icon svgIcon="add"></mat-icon>
          Start demo
        </button>
      </div>
    </div>

    <kpn-video
      videoSource="https://knooppuntnet.nl/public/demo1.mp4"
      (canPlay)="canplay()"
      (currentTime)="currentTimeChanged($event)">
    </kpn-video>
  `,
  styles: [`
    .instructions {
      min-height: 150px;
    }
  `]
})
export class DemoPlanRouteComponent implements OnInit {

  canPlay = false;
  currentTime = 0;

  @ViewChild(VideoComponent, {static: false}) video: VideoComponent;

  constructor(private pageService: PageService) {
  }

  ngOnInit(): void {
    this.pageService.defaultMenu();
  }

  start() {
    this.video.play();
  }

  canplay(): void {
    this.canPlay = true;
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
