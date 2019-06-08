export class MapStylingColors {

  public static readonly green /*ol.Color*/ = [0, 200, 0]; // regular nodes and routes
  public static readonly darkGreen /*ol.Color*/ = [0, 96, 0]; // orphan nodes and routes
  public static readonly red /*ol.Color*/ = [255, 0, 0]; // orphan
  public static readonly darkRed /*ol.Color*/ = [187, 0, 0]; // orphan error
  public static readonly blue /*ol.Color*/ = [0, 0, 255]; // orphan error
  public static readonly darkBlue /*ol.Color*/ = [0, 0, 187]; // orphan error
  public static readonly gray /*ol.Color*/ = [105, 105, 105]; // nodes and routes that do not belong to the current network
  public static readonly black = [0, 0, 0];

  public static readonly yellow = [255, 255, 0];
  public static readonly white = [255, 255, 255];
}
