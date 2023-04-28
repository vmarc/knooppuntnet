import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { SharedModule } from '@app/components/shared';
import { FactModule } from '../fact';
import { FactsPageComponent } from './facts-page.component';
import { FactsRoutingModule } from './facts-routing.module';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    FactModule,
    FactsRoutingModule,
    FactsPageComponent,
  ],
  exports: [FactsPageComponent],
})
export class FactsModule {}
