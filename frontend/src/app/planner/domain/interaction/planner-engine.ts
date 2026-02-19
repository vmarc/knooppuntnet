import { Coordinate } from '@api/custom/coordinate';
import { MapFeature } from '../features/map-feature';

export interface PlannerEngine {
  handleDownEvent(features: ReadonlyArray<MapFeature>, coordinate: Coordinate): boolean;

  handleSingleClickEvent(
    features: ReadonlyArray<MapFeature>,
    coordinate: Coordinate,
    modifierKeyOnly: boolean
  ): boolean;

  handleMoveEvent(
    features: ReadonlyArray<MapFeature>,
    coordinate: Coordinate,
    modifierKeyOnly: boolean
  ): boolean;

  handleDragEvent(features: ReadonlyArray<MapFeature>, coordinate: Coordinate): boolean;

  handleUpEvent(features: ReadonlyArray<MapFeature>, coordinate: Coordinate): boolean;

  handleMouseLeave(): void;
}
