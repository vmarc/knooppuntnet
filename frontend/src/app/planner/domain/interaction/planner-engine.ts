import { Coordinate } from '@api/custom/coordinate';
import { MapFeature } from '../features/map-feature';

export interface PlannerEngine {
  handleDownEvent(features: MapFeature[], coordinate: Coordinate): boolean;

  handleSingleClickEvent(
    features: MapFeature[],
    coordinate: Coordinate,
    modifierKeyOnly: boolean
  ): boolean;

  handleMoveEvent(
    features: MapFeature[],
    coordinate: Coordinate,
    modifierKeyOnly: boolean
  ): boolean;

  handleDragEvent(features: MapFeature[], coordinate: Coordinate): boolean;

  handleUpEvent(features: MapFeature[], coordinate: Coordinate): boolean;

  handleMouseLeave(): void;
}
