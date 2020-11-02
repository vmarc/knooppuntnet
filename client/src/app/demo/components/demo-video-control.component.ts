import {Component, Input} from "@angular/core";
import {ChangeDetectionStrategy} from "@angular/core";
import {select, Store} from "@ngrx/store";
import {actionDemoControlPlay} from "../../core/demo/demo.actions";
import {selectDemoVideo} from "../../core/demo/demo.selectors";
import {selectDemoPlaying} from "../../core/demo/demo.selectors";
import {selectDemoEnabled} from "../../core/demo/demo.selectors";
import {Observable} from "rxjs";
import {combineLatest} from "rxjs";
import {map} from "rxjs/operators";

@Component({
  selector: "kpn-demo-video-control",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="video-control" [ngClass]="{ selected: selected$ | async}">
      <div class="control-text">
        <div class="title">
          <ng-content></ng-content>
        </div>
        <div class="duration">
          {{duration}} <span i18n="demo.duration.seconds">seconds</span>
        </div>
      </div>

      <div *ngIf="enabled$ | async; then enabled else disabled"></div>
      <ng-template #enabled>
        <a (click)="play()" class="play-pause-button" [ngClass]="{ buttonSelected: selected$ | async}">
          <mat-icon [svgIcon]="icon$ | async"></mat-icon>
        </a>
      </ng-template>
      <ng-template #disabled>
        <span class="play-pause-button-disabled">
          <mat-icon [svgIcon]="icon$ | async"></mat-icon>
        </span>
      </ng-template>

    </div>
  `,
  styles: [`

    .video-control {
      display: flex;
      border-bottom: 1px solid lightgrey;
      height: 4em;
      padding: 1em;
    }

    .control-text {
      flex-grow: 1;
      display: flex;
      flex-direction: column;
    }

    .selected {
      background-color: #fafafa;
    }

    .title {
      flex-grow: 1;
      padding-right: 10px;
    }

    .duration {
      color: lightgray;
    }

    .play-pause-button {
      color: lightgray;
      cursor: pointer;
    }

    .play-pause-button:hover {
      color: gray;
    }

    .play-pause-button-disabled {
      color: lightgray;
    }

    .buttonSelected {
      color: red;
    }

    .buttonSelected.play-pause-button:hover {
      color: red;
    }

    .play-pause-button mat-icon {
      width: 3em;
      height: 3em;
    }

    .play-pause-button-disabled mat-icon {
      width: 3em;
      height: 3em;
    }

  `]
})
export class DemoVideoControlComponent {

  @Input() name: string;
  @Input() duration: string;

  readonly selected$: Observable<boolean> = this.store.pipe(
    select(selectDemoVideo),
    map(current => current == this.name)
  );

  readonly playing$: Observable<boolean> = combineLatest([this.selected$, this.store.pipe(select(selectDemoPlaying))]).pipe(
    map(([selected, playing]) => selected && playing)
  );

  readonly icon$: Observable<string> = this.playing$.pipe(map(playing => playing ? "pause" : "play"));

  readonly enabled$: Observable<boolean> = this.store.pipe(select(selectDemoEnabled));

  constructor(private store: Store) {
  }

  play(): void {
    this.store.dispatch(actionDemoControlPlay({video: this.name}));
  }

}
