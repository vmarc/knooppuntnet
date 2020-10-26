import {Component, Input} from "@angular/core";
import {ChangeDetectionStrategy} from "@angular/core";
import {select, Store} from "@ngrx/store";
import {actionDemoControlPlay} from "../../core/demo/demo.actions";
import {selectDemoVideo} from "../../core/demo/demo.selectors";
import {selectDemoPlaying} from "../../core/demo/demo.selectors";
import {Observable} from "rxjs";
import {combineLatest} from "rxjs";
import {map} from "rxjs/operators";

@Component({
  selector: "kpn-demo-video-control",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="video-control" [ngClass]="{ selected: selected$ | async}">
      <span class="title">
        <ng-content></ng-content>
      </span>
      <a (click)="play()" class="play-pause-button" [ngClass]="{ buttonSelected: selected$ | async}">
        <mat-icon [svgIcon]="icon$ | async"></mat-icon>
      </a>
    </div>
  `,
  styles: [`

    .video-control {
      display: flex;
      border-bottom: 1px solid lightgrey;
      height: 3em;
      padding: 1em;
    }

    .selected {
      background-color: #fafafa;
    }

    .title {
      flex-grow: 1;
      padding-right: 10px;
    }

    .play-pause-button {
      color: lightgray;
      cursor: pointer;
    }

    .play-pause-button:hover {
      color: gray;
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

  `]
})
export class DemoVideoControlComponent {

  @Input() name;

  selected$: Observable<boolean>;
  playing$: Observable<boolean>;
  icon$: Observable<string>;

  constructor(private store: Store) {
    this.selected$ = this.store.pipe(
      select(selectDemoVideo),
      map(current => current == this.name)
    );
    this.playing$ = combineLatest([this.selected$, this.store.pipe(select(selectDemoPlaying))]).pipe(
      map(([selected, playing]) => selected && playing)
    );
    this.icon$ = this.playing$.pipe(map(playing => playing ? "pause" : "play"));
  }

  play(): void {
    this.store.dispatch(actionDemoControlPlay({video: this.name}));
  }

}
