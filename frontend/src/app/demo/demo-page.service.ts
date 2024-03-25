import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { PageWidthService } from '@app/components/shared';
import { RouterService } from '../shared/services/router.service';

@Injectable({
  providedIn: 'root',
})
export class DemoPageService {
  private readonly pageWidthService = inject(PageWidthService);
  private readonly routerService = inject(RouterService);
  private readonly router = inject(Router);

  private readonly _video = signal<string>('');
  private readonly _playing = signal<boolean>(false);
  private readonly _time = signal<number>(0);
  private readonly _duration = signal<number>(0);

  readonly video = this._video.asReadonly();
  readonly playing = this._playing.asReadonly();
  readonly time = this._time.asReadonly();
  readonly duration = this._duration.asReadonly();
  readonly enabled = this.pageWidthService.isVeryLarge;

  private videoElement: HTMLVideoElement;
  private sourceElement: HTMLSourceElement;

  onInit(videoElement: HTMLVideoElement, sourceElement: HTMLSourceElement) {
    this.videoElement = videoElement;
    this.sourceElement = sourceElement;
    const video = this.routerService.param('video');
    if (video) {
      this._video.set(video);
      this.setSource(`/videos/en/${video}.mp4`);
      this.play();
    }
  }

  setPlaying(playing: boolean): void {
    this._playing.set(playing);
  }

  updateDuration(): void {
    this._duration.set(this.videoElement.duration);
  }

  setProgress(progress: number): void {
    this.videoElement.currentTime = progress * this.videoElement.duration;
  }

  onDestroy(): void {
    this.videoElement.pause();
    this.sourceElement.src = '';
    this.videoElement.load();
    this.sourceElement = null;
    this.videoElement = null;
  }

  play(): void {
    const promise = this.videoElement.play();
    if (promise !== undefined) {
      promise
        .then((_) => {
          // Autoplay started
        })
        .catch((error) => {
          // Autoplay was prevented
        });
    }
  }

  pause(): void {
    this.videoElement.pause();
  }

  timeChanged(): void {
    this._time.set(this.videoElement.currentTime);
  }

  playVideo(video: string) {
    if (this.video() === video) {
      if (this.playing()) {
        this.pause();
      } else {
        this.play();
      }
    } else {
      this.router.navigate(['/demo', video]);
      this._video.set(video);
      this.setSource(`/videos/en/${this.video()}.mp4`);
      this.play();
    }
  }

  private setSource(videoSource: string): void {
    this.sourceElement.src = videoSource;
    this.videoElement.load();
  }
}
