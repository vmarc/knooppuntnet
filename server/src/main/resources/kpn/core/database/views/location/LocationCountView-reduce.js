var nodeCount = 0;
var routeCount = 0;
var factCount = 0;

for (i = 0; i < values.length; i++) {
  var value = values[i];
  nodeCount += value[0];
  routeCount += value[1];
  factCount += value[2];
}

var result = [nodeCount, routeCount, factCount];
