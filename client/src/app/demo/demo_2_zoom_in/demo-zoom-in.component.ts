import {Component, OnInit} from "@angular/core";
import {ViewChild} from "@angular/core";
import {PageService} from "../../components/shared/page.service";
import {VideoComponent} from "../components/video.component";

@Component({
  selector: "kpn-demo-zoom-in",
  template: `
    <kpn-video
      videoSource="https://knooppuntnet.nl/public/demo2.mp4"
      (canplay)="canplay()">
    </kpn-video>
  `
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
    this.video.play();
  }

}
