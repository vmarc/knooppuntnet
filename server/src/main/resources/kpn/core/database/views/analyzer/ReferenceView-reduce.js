var sum = 0;
if (rereduce) {
  for (i = 0; i < values.length; i++) {
    sum += values[i];
  }
}
else {
  for (i = 0; i < values.length; i++) {
    sum += values[i][0];
  }
}
var result = sum;
