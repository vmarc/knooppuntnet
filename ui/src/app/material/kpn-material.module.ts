import {
  MatButtonModule,
  MatCheckboxModule,
  MatDividerModule,
  MatMenuModule,
  MatPaginatorModule,
  MatRadioModule,
  MatSidenavModule,
  MatSlideToggleModule,
  MatSortModule,
  MatTableModule,
  MatToolbarModule,
  MatTooltipModule,
  MatIconModule,
  MatListModule
} from '@angular/material';
import {NgModule} from "@angular/core";

@NgModule({
  imports: [
    MatButtonModule,
    MatCheckboxModule,
    MatSidenavModule,
    MatToolbarModule,
    MatMenuModule,
    MatRadioModule,
    MatSlideToggleModule,
    MatTooltipModule,
    MatPaginatorModule,
    MatSortModule,
    MatTableModule,
    MatIconModule,
    MatListModule
  ],
  exports: [
    MatButtonModule,
    MatCheckboxModule,
    MatSidenavModule,
    MatToolbarModule,
    MatMenuModule,
    MatRadioModule,
    MatSlideToggleModule,
    MatTooltipModule,
    MatPaginatorModule,
    MatSortModule,
    MatTableModule,
    MatIconModule,
    MatListModule
  ],
})
export class KpnMaterialModule {
}
