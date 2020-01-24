import {AfterViewInit, Component, ElementRef, ViewChild} from "@angular/core";

@Component({
  selector: "kpn-map-sidebar",
  template: `
    <kpn-map-sidebar-planner></kpn-map-sidebar-planner>
    <mat-divider></mat-divider>

    <canvas #profile width="200" height="100"></canvas>


    <mat-divider></mat-divider>

    <kpn-map-sidebar-appearance></kpn-map-sidebar-appearance>
    <kpn-map-sidebar-legend></kpn-map-sidebar-legend>
    <kpn-map-sidebar-poi-configuration></kpn-map-sidebar-poi-configuration>
  `
})
export class MapSidebarComponent implements AfterViewInit {

  @ViewChild("profile") canvas: ElementRef;

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
