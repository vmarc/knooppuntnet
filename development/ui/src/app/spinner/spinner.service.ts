import {Injectable} from "@angular/core";
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: "root"
})
export class SpinnerService {

  private readonly _spinnerState = new BehaviorSubject<boolean>(false);
  private counter = 0;

  spinnerState() {
    return this._spinnerState;
  }

  show() {
    if (this.counter === 0) {
      this._spinnerState.next(true);
    }

    this.counter++;

    console.log(`DEBUG SpinnerService show() - counter = ${this.counter}`);
  }

  hide() {
    if (this.counter === 1) {
      this._spinnerState.next(false);
    }
    if (this.counter > 0) {
      this.counter--;
    }

    console.log(`DEBUG SpinnerService hide() - counter = ${this.counter}`);
  }
}
