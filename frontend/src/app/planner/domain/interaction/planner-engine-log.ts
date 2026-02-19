import { Coordinate } from '@api/custom/coordinate';
import { MapFeature } from '@app/planner/domain/features/map-feature';
import { PlannerEngine } from './planner-engine';

export class PlannerEngineLog implements PlannerEngine {
  handleDownEvent(features: MapFeature[], coordinate: Coordinate): boolean {
    console.log('handleDownEvent', features, coordinate);
    return true;
  }

  handleDragEvent(features: MapFeature[], coordinate: Coordinate): boolean {
    console.log('handleDragEvent', features, coordinate);
    return true;
  }

  handleMouseLeave(): void {
    console.log('handleMouseLeave');
  }

  handleMoveEvent(
    features: MapFeature[],
    coordinate: Coordinate,
    modifierKeyOnly: boolean
  ): boolean {
    console.log('handleMoveEvent', features, coordinate, modifierKeyOnly);
    return true;
  }

  handleSingleClickEvent(
    features: MapFeature[],
    coordinate: Coordinate,
    modifierKeyOnly: boolean
  ): boolean {
    console.log('handleSingleClickEvent', features, coordinate, modifierKeyOnly);
    return true;
  }

  handleUpEvent(features: MapFeature[], coordinate: Coordinate): boolean {
    console.log('handleUpEvent', features, coordinate);
    return true;
  }
}
