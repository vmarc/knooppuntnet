import {Component} from "@angular/core";
import {Observable} from "rxjs";
import {Store} from "@ngrx/store";
import {decrement, increment, reset, set} from "./counter.actions";

@Component({
  selector: "app-counter",
  template: `
    <div class="buttons">
      <button (click)="increment()">Increment</button>
      <button (click)="decrement()">Decrement</button>
      <button (click)="reset()">Reset Counter</button>
    </div>
    <div class="buttons">
      <button (click)="value(2)">2</button>
      <button (click)="value(4)">4</button>
      <button (click)="value(8)">8</button>
    </div>

    <div class="count">
      {{ count$ | async }}
    </div>
  `,
  styles: [`

    .buttons :not(:last-child) {
      margin-right: 10px;
      margin-bottom: 10px;
    }

    .count {
      padding-top: 10px;
      padding-bottom: 10px;
      font-size: 25px;
    }
  `]
})
export class CounterComponent {

  count$: Observable<number>;

  constructor(private store: Store<{ count: number }>) {
    this.count$ = store.select("count");
  }

  increment() {
    this.store.dispatch(increment());
  }

  decrement() {
    this.store.dispatch(decrement());
  }

  reset() {
    this.store.dispatch(reset());
  }

  value(value: number) {
    this.store.dispatch(set({value: value}));
  }

}
