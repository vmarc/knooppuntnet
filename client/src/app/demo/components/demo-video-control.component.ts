import {Component, Input} from "@angular/core";
import {Store, select} from "@ngrx/store";
import {actionDemoPlay} from "../../core/demo/demo.actions";
import {selectDemoCurrentVideo} from "../../core/demo/demo.selectors";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";

@Component({
  selector: "kpn-demo-video-control",
  template: `
    <div class="video-control" [ngClass]="{ selected: selected$ | async}">
      <span class="title">
        <ng-content></ng-content>
      </span>
      <a (click)="play()" class="play-pause-button" [ngClass]="{ buttonSelected: selected$ | async}">
        <mat-icon svgIcon="play"></mat-icon>
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

    .play-pause-button mat-icon {
      width: 3em;
      height: 3em;
    }

  `]
})
export class DemoVideoControlComponent {

  @Input() name;

  selected$: Observable<boolean>;

  constructor(private store: Store) {
    this.selected$ = this.store.pipe(
      select(selectDemoCurrentVideo),
      map(current => current == this.name)
    );
  }

  play(): void {
    this.store.dispatch(
      actionDemoPlay({
        video: this.name
      })
    );
  }

}
