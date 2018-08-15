var factCount = 0;

for (i = 0; i < values.length; i++) {
  var value = values[i];
  if (value.length) {
    factCount += value.length;
  }
  else {
    factCount += 1;
  }
}
var result = {networkCount: values.length, factCount: factCount};
