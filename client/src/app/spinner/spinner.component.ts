import {ChangeDetectionStrategy} from "@angular/core";
import {Component, OnDestroy, OnInit} from "@angular/core";
import {debounceTime} from "rxjs/operators";
import {Subscriptions} from "../util/Subscriptions";
import {SpinnerService} from "./spinner.service";

@Component({
  selector: "kpn-spinner",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-spinner *ngIf="showSpinner" diameter="40"></mat-spinner>
  `
})
export class SpinnerComponent implements OnDestroy, OnInit {

  private readonly subscriptions = new Subscriptions();

  showSpinner = false;

  constructor(private spinnerService: SpinnerService) {
  }

  ngOnInit(): void {
    this.subscriptions.add(this.spinnerService.spinnerState().pipe(debounceTime(300)).subscribe(show => {
      this.showSpinner = show;
    }));
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

}
