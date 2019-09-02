import {Component, Inject} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {NetworkType} from "../../../../kpn/shared/network-type";
import {RouteAccessibleData} from "./route-accessible-data";

@Component({
  selector: "kpn-route-acccessible-indicator-dialog",
  template: `
    <!--@@ letter T -->
    <kpn-indicator-dialog
      letter="A"
      i18n-letter="@@route-accessible-indicator.letter"
      [color]="color"
      (closeDialog)="onCloseDialog()">

      <!--@@ OK - Toegankelijkheid onbekend -->
      <span dialog-title *ngIf="isGray()" i18n="@@route-accessible-indicator.gray.title">
        OK - Accessibility unknown
      </span>
      <!--@@ Route toegankelijkheid gegevens zijn onbekend voor dit type route. -->
      <div dialog-body *ngIf="isGray()" i18n="@@route-accessible-indicator.gray.text">
        Accessibility information is unknown for this type of route.
      </div>


      <!--@@ OK - Toegankelijk -->
      <span dialog-title *ngIf="isGreen()" i18n="@@route-accessible-indicator.green.title">
        OK - Accessible        
      </span>
      <!--@@ Deze route is volledig toegankelijk voor fietsers. -->
      <div dialog-body *ngIf="isGreen() && isCycling() " i18n="@@route-accessible-indicator.green.text.cycling">
        This route is completely accessible for bicycle.
      </div>
      <!--@@ Deze route is volledig toegankelijk voor wandelaars. -->
      <div dialog-body *ngIf="isGreen() && isHiking() " i18n="@@route-accessible-indicator.green.text.hiking">
        This route is completely accessible for hiking.
      </div>
      <!--@@ Deze route is volledig toegankelijk voor motorboot. -->
      <div dialog-body *ngIf="isGreen() && isMotorboat() " i18n="@@route-accessible-indicator.green.text.motorboat">
        This route is completely accessible for motorboat.
      </div>
      <!--@@ Deze route is volledig toegankelijk voor kano. -->
      <div dialog-body *ngIf="isGreen() && isCanoe() " i18n="@@route-accessible-indicator.green.text.canoe">
        This route is completely accessible for canoe.
      </div>


      <!--@@ NOK - Niet toegankelijk -->
      <span dialog-title *ngIf="isRed()" i18n="@@route-accessible-indicator.red.title">
        NOK - Not Accessible        
      </span>
      <!--@@ Deze route is niet volledig toegankelijk voor fietsers. -->
      <div dialog-body *ngIf="isRed() && isCycling() " i18n="@@route-accessible-indicator.red.text.cycling">
        This route is not completely accessible for bicycle.
      </div>
      <!--@@ Deze route is niet volledig toegankelijk voor wandelaars. -->
      <div dialog-body *ngIf="isRed() && isHiking() " i18n="@@route-accessible-indicator.red.text.hiking">
        This route is not completely accessible for hiking.
      </div>
      <!--@@ Deze route is niet volledig toegankelijk voor motorboot. -->
      <div dialog-body *ngIf="isRed() && isMotorboat() " i18n="@@route-accessible-indicator.red.text.motorboat">
        This route is not completely accessible for motorboat.
      </div>
      <!--@@ Deze route is niet volledig toegankelijk voor kano. -->
      <div dialog-body *ngIf="isRed() && isCanoe() " i18n="@@route-accessible-indicator.red.text.canoe">
        This route is not completely accessible for canoe.
      </div>

    </kpn-indicator-dialog>
  `
})
export class RouteAccessibleIndicatorDialogComponent {

  constructor(private dialogRef: MatDialogRef<RouteAccessibleIndicatorDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: RouteAccessibleData) {
  }

  onCloseDialog(): void {
    this.dialogRef.close();
  }

  get color() {
    return this.data.color;
  }

  isGray() {
    return this.data.color === "gray";
  }

  isGreen() {
    return this.data.color === "green";
  }

  isRed() {
    return this.data.color === "red";
  }

  isCycling(): boolean {
    return this.isNetworkType(NetworkType.cycling);
  }

  isHiking(): boolean {
    return this.isNetworkType(NetworkType.hiking);
  }

  isMotorboat(): boolean {
    return this.isNetworkType(NetworkType.motorboat);
  }

  isCanoe(): boolean {
    return this.isNetworkType(NetworkType.canoe);
  }

  private isNetworkType(networkType: NetworkType): boolean {
    return networkType.id === this.data.networkType.id;
  }

}
