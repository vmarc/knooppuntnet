import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {ViewChild} from '@angular/core';
import {AfterViewInit} from '@angular/core';
import {ElementRef} from '@angular/core';
import {OnDestroy} from '@angular/core';
import {Store} from '@ngrx/store';
import {select} from '@ngrx/store';
import {PageService} from '../../components/shared/page.service';
import {DemoService} from '../../core/demo/demo.service';
import * as DemoActions from '../../core/demo/demo.actions';
import {Observable} from 'rxjs';
import {selectDemoEnabled} from '../../core/demo/demo.selectors';
import {map} from 'rxjs/operators';

@Component({
  selector: 'kpn-video',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <div class="video-page">

      <div *ngIf="enabled$ | async; then enabled else disabled"></div>

      <ng-template #disabled>
        <kpn-demo-disabled></kpn-demo-disabled>
      </ng-template>

      <ng-template #enabled>
        <kpn-video-cover *ngIf="!canPlayReceived"></kpn-video-cover>
        <kpn-demo-video-play-button></kpn-demo-video-play-button>
      </ng-template>

      <div [ngClass]="{hidden: (disabled$ | async) || !canPlayReceived}">
        <video
          #videoPlayer
          width="1280px"
          height="720px"
          autoplay
          (click)="playPause()"
          (playing)="playingChanged()"
          (pause)="pauseChanged()"
          (canplay)="canPlayChanged()"
          (timeupdate)="timeChanged()"
          i18n="demo.no-video-support">
          <source #videoPlayerSource src="" type='video/mp4'/>
          Sorry, cannot play videos in your browser.
        </video>

        <kpn-demo-video-progress></kpn-demo-video-progress>
      </div>
    </div>
  `,
  styles: [`
    .video-page {
      overflow-x: hidden;
    }

    video {
      z-index: 5;
      position: absolute;
      border: 2px solid lightgrey;
    }

    .hidden {
      display: none;
    }

  `]
})
export class DemoVideoComponent implements AfterViewInit, OnDestroy {

  @ViewChild('videoPlayer', {static: false}) videoElementRef: ElementRef;
  @ViewChild('videoPlayerSource', {static: false}) videoPlayerSourceRef: ElementRef;

  canPlayReceived = false;
  readonly enabled$: Observable<boolean> = this.store.pipe(select(selectDemoEnabled));
  readonly disabled$: Observable<boolean> = this.enabled$.pipe(map(enabled => !enabled));

  constructor(private store: Store,
              private demoService: DemoService,
              private pageService: PageService) {
    this.pageService.showFooter = false;
  }

  ngAfterViewInit(): void {
    this.demoService.setVideoElement(this.videoElementRef.nativeElement, this.videoPlayerSourceRef.nativeElement);
    this.store.dispatch(DemoActions.videoPlayerAvailable());
  }

  ngOnDestroy(): void {
    this.store.dispatch(DemoActions.end());
  }

  canPlayChanged(): void {
    this.store.dispatch(DemoActions.canPlay({duration: this.demoService.duration}));


    this.canPlayReceived = true;
  }

  timeChanged(): void {
    this.store.dispatch(DemoActions.timeUpdate({time: this.demoService.time}));
  }

  playPause(): void {
    if (this.videoElementRef.nativeElement.paused) {
      this.demoService.play();
    } else {
      this.demoService.pause();
    }
  }

  playingChanged(): void {
    this.store.dispatch(DemoActions.playingChanged({playing: true}));
  }

  pauseChanged(): void {
    this.store.dispatch(DemoActions.playingChanged({playing: false}));
  }
}
