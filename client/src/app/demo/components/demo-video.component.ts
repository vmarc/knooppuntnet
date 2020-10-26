import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";
import {ViewChild} from "@angular/core";
import {AfterViewInit} from "@angular/core";
import {ElementRef} from "@angular/core";
import {OnDestroy} from "@angular/core";
import {Store} from "@ngrx/store";
import {BehaviorSubject} from "rxjs";
import {PageService} from "../../components/shared/page.service";
import {DemoService} from "../../core/demo/demo.service";
import {actionDemoEnd} from "../../core/demo/demo.actions";
import {actionDemoVideoPlayerAvailable} from "../../core/demo/demo.actions";
import {actionDemoPlayingChanged} from "../../core/demo/demo.actions";

@Component({
  selector: "kpn-video",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <kpn-video-cover *ngIf="!canPlayReceived"></kpn-video-cover>

    <kpn-demo-video-play-button *ngIf="showPlayButton$ | async" (click)="play()"></kpn-demo-video-play-button>

    <video
      #videoPlayer
      width="1280px"
      height="720px"
      autoplay
      (click)="playPause()"
      (playing)="playingChanged()"
      (pause)="pauseChanged()"
      (canplay)="canplayChanged()"
      (timeupdate)="timeupdateChanged()"
      i18n="demo.no-video-support">
      <source #videoPlayerSource src="" type='video/mp4'/>
      Sorry, cannot play videos in your browser.
    </video>

    <div class="footer">
      <mat-slider
        class="progress"
        min="0"
        max="100"
        [value]="progress"
        (input)="sliderInputChanged($event)">
      </mat-slider>
    </div>
  `,
  styles: [`
    video {
      z-index: 5;
      position: absolute;
      border: 2px solid lightgrey;
    }

    .progress {
      width: 1280px;
    }

    .footer {
      z-index: 3;
      position: absolute;
      padding-top: 720px;
    }
  `]
})
export class DemoVideoComponent implements AfterViewInit, OnDestroy {

  showPlayButton$ = new BehaviorSubject<boolean>(false);

  @ViewChild("videoPlayer", {static: false}) videoElementRef: ElementRef;
  @ViewChild("videoPlayerSource", {static: false}) videoPlayerSourceRef: ElementRef;

  canPlayReceived = false;
  progress = 0;

  constructor(private store: Store,
              private demoService: DemoService,
              private pageService: PageService) {
    this.pageService.showFooter = false;
  }

  ngAfterViewInit(): void {
    this.demoService.setVideoElement(this.videoElementRef.nativeElement, this.videoPlayerSourceRef.nativeElement);
    this.store.dispatch(actionDemoVideoPlayerAvailable());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionDemoEnd());
  }

  canplayChanged(): void {
    this.showPlayButton$.next(true);
    this.canPlayReceived = true;
    this.play();
  }

  timeupdateChanged(): void {
    if (this.videoElementRef.nativeElement.duration > 0) {
      this.progress = 100 * this.videoElementRef.nativeElement.currentTime / this.videoElementRef.nativeElement.duration;
    }
  }

  playPause(): void {
    if (this.videoElementRef.nativeElement.paused) {
      this.play();
    } else {
      this.pause();
    }
  }

  play(): void {
    const promise = this.videoElementRef.nativeElement.play();
    if (promise !== undefined) {
      promise.then(_ => {
        // Autoplay started
        this.showPlayButton$.next(false);
      }).catch(error => {
        // Autoplay was prevented
      });
    }
  }

  pause(): void {
    this.videoElementRef.nativeElement.pause();
  }

  sliderInputChanged(event) {
    this.videoElementRef.nativeElement.currentTime = event.value / 100 * this.videoElementRef.nativeElement.duration;
  }

  playingChanged(): void {
    this.store.dispatch(actionDemoPlayingChanged({playing: true}));
  }

  pauseChanged(): void {
    this.store.dispatch(actionDemoPlayingChanged({playing: false}));
  }
}
