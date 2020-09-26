import {Component} from "@angular/core";
import {Observable} from "rxjs";
import {Store} from "@ngrx/store";
import {decrement, increment, reset} from "./counter.actions";

@Component({
  selector: "app-counter",
  template: `
    <div class="buttons">
      <button id="increment" (click)="increment()">Increment</button>
      <button id="decrement" (click)="decrement()">Decrement</button>
      <button id="reset" (click)="reset()">Reset Counter</button>
    </div>

    <div class="count">
      {{ count$ | async }}
    </div>
  `,
  styles: [`

    .buttons :not(:last-child) {
      margin-right: 10px;
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

}
