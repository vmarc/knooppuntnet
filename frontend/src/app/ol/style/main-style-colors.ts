import { Color } from 'ol/color';

export const lightGreen: Color = [0, 255, 0]; // regular nodes and routes
export const green: Color = [0, 200, 0]; // regular nodes and routes
export const darkGreen: Color = [0, 150, 0]; // orphan nodes and routes
export const veryDarkGreen: Color = [0, 90, 0]; // orphan nodes and routes

export const red: Color = [255, 0, 0]; // orphan
export const darkRed: Color = [187, 0, 0]; // orphan error
export const blue: Color = [0, 0, 255]; // orphan error
export const darkBlue: Color = [0, 0, 187]; // orphan error
export const gray: Color = [200, 200, 200]; // nodes and routes that do not belong to the current network

export const yellow: Color = [255, 255, 0]; // selected color
export const white: Color = [255, 255, 255]; // node inner color
export const orange: Color = [255, 165, 0];

export const proposedWhite: Color = [240, 240, 240];
export const proposedColor: Color = [0, 150, 0, 0.4];
export const proposedUnpavedColor: Color = [255, 165, 0, 0.4];

export const surfaceUnknownColor: Color = [0, 0, 220];
export const proposedSurfaceUnknownColor: Color = [0, 0, 220, 0.4];

export const surveyUnknown: Color = [255, 255, 0];
export const surveyUnknownNode: Color = [225, 225, 0];
export const surveyLastMonth = lightGreen;
export const surveyLastHalfYearStart = green;
export const surveyLastYearStart = darkGreen;
export const surveyLastTwoYearsStart = veryDarkGreen;
export const surveyOlder = darkRed;
