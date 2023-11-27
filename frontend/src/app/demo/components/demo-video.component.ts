import { AsyncPipe } from '@angular/common';
import { NgClass } from '@angular/common';
import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ViewChild } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ElementRef } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { PageComponent } from '@app/components/shared/page';
import { Store } from '@ngrx/store';
import { select } from '@ngrx/store';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { DemoService } from '../demo.service';
import { actionDemoVideoPlayerAvailable } from '../store/demo.actions';
import { actionDemoEnd } from '../store/demo.actions';
import { actionDemoCanPlay } from '../store/demo.actions';
import { actionDemoTimeUpdate } from '../store/demo.actions';
import { actionDemoPlayingChanged } from '../store/demo.actions';
import { selectDemoEnabled } from '../store/demo.selectors';
import { DemoDisabledComponent } from './demo-disabled.component';
import { DemoSidebarComponent } from './demo-sidebar.component';
import { DemoVideoPlayButtonComponent } from './demo-video-play-button.component';
import { DemoVideoProgressComponent } from './demo-video-progress.component';
import { VideoCoverComponent } from './video-cover.component';

@Component({
  selector: 'kpn-video',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <div class="video-page">
        <div *ngIf="enabled$ | async; then enabled; else disabled"></div>

        <ng-template #disabled>
          <kpn-demo-disabled />
        </ng-template>

        <ng-template #enabled>
          <kpn-video-cover *ngIf="!canPlayReceived" />
          <kpn-demo-video-play-button />
        </ng-template>

        <div [ngClass]="{ hidden: (disabled$ | async) || !canPlayReceived }">
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
          >
            <source #videoPlayerSource src="" type="video/mp4" />
            <ng-container i18n="@@demo.no-video-support">
              Sorry, cannot play videos in your browser.
            </ng-container>
          </video>

          <kpn-demo-video-progress />
        </div>
      </div>
      <kpn-demo-sidebar sidebar />
    </kpn-page>
  `,
  styles: `
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
  `,
  standalone: true,
  imports: [
    AsyncPipe,
    DemoDisabledComponent,
    DemoSidebarComponent,
    DemoVideoPlayButtonComponent,
    DemoVideoProgressComponent,
    NgClass,
    NgIf,
    PageComponent,
    VideoCoverComponent,
  ],
})
export class DemoVideoComponent implements AfterViewInit, OnDestroy {
  @ViewChild('videoPlayer', { static: false }) videoElementRef: ElementRef;
  @ViewChild('videoPlayerSource', { static: false })
  videoPlayerSourceRef: ElementRef;

  canPlayReceived = false;
  readonly enabled$: Observable<boolean> = this.store.pipe(select(selectDemoEnabled));
  readonly disabled$: Observable<boolean> = this.enabled$.pipe(map((enabled) => !enabled));

  constructor(
    private store: Store,
    private demoService: DemoService
  ) {}

  ngAfterViewInit(): void {
    this.demoService.setVideoElement(
      this.videoElementRef.nativeElement,
      this.videoPlayerSourceRef.nativeElement
    );
    this.store.dispatch(actionDemoVideoPlayerAvailable());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionDemoEnd());
  }

  canPlayChanged(): void {
    this.store.dispatch(actionDemoCanPlay({ duration: this.demoService.duration }));

    this.canPlayReceived = true;
  }

  timeChanged(): void {
    this.store.dispatch(actionDemoTimeUpdate({ time: this.demoService.time }));
  }

  playPause(): void {
    if (this.videoElementRef.nativeElement.paused) {
      this.demoService.play();
    } else {
      this.demoService.pause();
    }
  }

  playingChanged(): void {
    this.store.dispatch(actionDemoPlayingChanged({ playing: true }));
  }

  pauseChanged(): void {
    this.store.dispatch(actionDemoPlayingChanged({ playing: false }));
  }
}
