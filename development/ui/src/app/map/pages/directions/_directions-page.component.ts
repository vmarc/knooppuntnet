import {Component, OnDestroy, OnInit} from '@angular/core';
import {Directions} from "../../../kpn/shared/directions/directions";
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {AppService} from "../../../app.service";

@Component({
  selector: 'kpn-directions-page',
  template: `
    <h1>
      Turn-by-turn directions
    </h1>
    <h2 *ngIf="exampleName">
      {{exampleName}}
    </h2>

    <div *ngIf="directions != null">
      <kpn-directions-summary [directions]="directions"></kpn-directions-summary>
      <br/>
      <mat-divider></mat-divider>
      <div *ngFor="let instruction of directions.instructions">
        <kpn-directions-instruction [instruction]="instruction"></kpn-directions-instruction>
        <mat-divider></mat-divider>
      </div>
    </div>
  `
})
export class DirectionsPageComponent implements OnInit, OnDestroy {

  paramsSubscription: Subscription;
  exampleName: string;
  directions: Directions;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit() {
    this.paramsSubscription = this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      this.exampleName = params['exampleName'];
      this.appService.directions("nl", this.exampleName).subscribe(response => {
        this.directions = response.result;
      });
    });
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }
}
