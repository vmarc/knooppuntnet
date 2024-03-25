import { NgClass } from '@angular/common';
import { signal } from '@angular/core';
import { OnInit } from '@angular/core';
import { viewChild } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ElementRef } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { PageComponent } from '@app/components/shared/page';
import { RouterService } from '../shared/services/router.service';
import { DemoDisabledComponent } from './components/demo-disabled.component';
import { DemoSidebarComponent } from './components/demo-sidebar.component';
import { DemoVideoProgressComponent } from './components/demo-video-progress.component';
import { VideoCoverComponent } from './components/video-cover.component';
import { DemoPageService } from './demo-page.service';

@Component({
  selector: 'kpn-page-video',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page [showFooter]="false">
      <div class="video-page">
        @if (enabled()) {
          @if (!canPlayReceived()) {
            <kpn-video-cover />
          }
        } @else {
          <kpn-demo-disabled />
        }

        <div [ngClass]="{ hidden: disabled() || !canPlayReceived() }">
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
  providers: [DemoPageService, RouterService],
  standalone: true,
  imports: [
    DemoDisabledComponent,
    DemoSidebarComponent,
    DemoVideoProgressComponent,
    NgClass,
    PageComponent,
    VideoCoverComponent,
  ],
})
export class DemoPageComponent implements OnInit, OnDestroy /*, AfterViewInit*/ {
  videoElement = viewChild.required<ElementRef>('videoPlayer');
  videoSource = viewChild.required<ElementRef>('videoPlayerSource');

  private readonly service = inject(DemoPageService);

  protected canPlayReceived = signal<boolean>(false);

  protected readonly enabled = this.service.enabled;
  protected readonly disabled = computed(() => !this.enabled());

  ngOnInit(): void {
    const videoElement = this.videoElement();
    const videoSource = this.videoSource();
    this.service.onInit(videoElement.nativeElement, videoSource.nativeElement);
  }

  ngOnDestroy(): void {
    this.service.onDestroy();
  }

  canPlayChanged(): void {
    this.service.updateDuration();
    this.service.play();
    this.canPlayReceived.set(true);
  }

  timeChanged(): void {
    this.service.timeChanged();
  }

  playPause(): void {
    if (this.videoElement().nativeElement.paused) {
      this.service.play();
    } else {
      this.service.pause();
    }
  }

  playingChanged(): void {
    this.service.setPlaying(true);
  }

  pauseChanged(): void {
    this.service.setPlaying(false);
  }
}
