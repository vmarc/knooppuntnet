import {AfterViewInit} from "@angular/core";
import {ElementRef} from "@angular/core";
import {ViewChild} from "@angular/core";
import {Component} from "@angular/core";

@Component({
  selector: "kpn-elevation-profile",
  template: `
    <canvas #profile width="200" height="100"></canvas>
    <mat-divider></mat-divider>
  `
})
export class ElevationProfileComponent implements AfterViewInit {

  @ViewChild("profile", {static: true}) canvas: ElementRef;

  ngAfterViewInit(): void {
    const ctx = this.canvas.nativeElement.getContext("2d");
    ctx.fillStyle = "#FF0000";
    ctx.fillRect(0, 0, 20, 20);
    ctx.moveTo(0, 0);
    ctx.lineTo(200, 100);
    ctx.moveTo(0, 100);
    ctx.lineTo(200, 0);
    ctx.stroke();
    ctx.fillStyle = "#0000FF";
    ctx.font = "16px serif";
    ctx.fillText("text", 50, 50, 150);
  }
}
