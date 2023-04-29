import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatRadioModule } from '@angular/material/radio';
import { SharedModule } from '@app/components/shared';
import { AnalysisStrategyComponent } from './analysis-strategy.component';
import { AnalysisStrategyService } from './analysis-strategy.service';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    MatRadioModule,
    MatIconModule,
    AnalysisStrategyComponent,
  ],
  exports: [AnalysisStrategyComponent],
  providers: [AnalysisStrategyService],
})
export class AnalysisStrategyModule {}