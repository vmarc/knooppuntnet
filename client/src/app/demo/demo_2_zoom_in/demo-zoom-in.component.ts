import {Component, OnInit} from "@angular/core";
import {ViewChild} from "@angular/core";
import {PageService} from "../../components/shared/page.service";
import {VideoComponent} from "../components/video.component";

@Component({
  selector: "kpn-demo-zoom-in",
  template: `
    <div class="header">
      <h1>
        Video demo - How to zoom in to a selected area of the map?
      </h1>
    </div>

    <div>
      <div>
        To select an area on the map, hold down the shift key and draw a rectangle on the map.
      </div>

      <div *ngIf="!started" class="kpn-spacer-above kpn-spacer-below">
        <button mat-raised-button color="primary" (click)="start()">
          <mat-icon svgIcon="add"></mat-icon>
          Start demo
        </button>
      </div>

      <kpn-video
        videoSource="https://knooppuntnet.nl/public/demo2.mp4"
        (canplay)="canplay()"
        (timeupdate)="timeupdate()">
      </kpn-video>

    </div>
  `,
  styles: [`
  `]
})
export class DemoZoomInComponent implements OnInit {

  canPlay = false;
  started = false;

  @ViewChild(VideoComponent, {static: false}) video: VideoComponent;

  constructor(private pageService: PageService) {
  }

  ngOnInit(): void {
    this.pageService.defaultMenu();
  }

  start() {
    this.started = true;
    this.video.play();
  }

  canplay(): void {
    this.canPlay = true;
  }

  timeupdate(): void {
    // if (this.videoplayer.nativeElement.currentTime > 0) {
    //   // this.started = true;
    // }
    // console.log("timeupdate=" + this.videoplayer.nativeElement.currentTime);
  }

}
