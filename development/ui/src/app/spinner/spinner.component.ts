import {Component, OnDestroy, OnInit} from "@angular/core";
import {Subscription} from "rxjs";
import {debounceTime} from "rxjs/operators";
import {SpinnerService} from "./spinner.service";

@Component({
  selector: "kpn-spinner",
  template: `
    <mat-spinner *ngIf="showSpinner" diameter="40"></mat-spinner>
  `
})
export class SpinnerComponent implements OnDestroy, OnInit {

  showSpinner = false;
  private subscription: Subscription = null;

  constructor(private spinnerService: SpinnerService) {
  }

  ngOnInit(): void {
    this.subscription = this.spinnerService.spinnerState().pipe(debounceTime(300)).subscribe(show => {
      this.showSpinner = show;
    });
  }

  ngOnDestroy() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

}
