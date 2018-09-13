var newValues = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
for (var i = 0; i < values.length; i++) {
  for (var j = 0; j < newValues.length; j++) {
    newValues[j] += values[i][j];
  }
}
var result = newValues;
