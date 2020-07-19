|Nr|planner interaction|OK|expected result
|:---|:---|:---|:---
|1|hover over node|OK|cursor changes (+ yellow highlight) to indicate node is clickable
|2|hover over route in empty plan|OK|no cursor change, route is not clickable
|3|hover over route in plan with at least a start marker|OK|cursor changes (+ yellow highlight) to indicate that the route is clickable (note: highlight may be partial due to technical limitation)
|4|click node in empty plan|OK|adds blue start marker
|5|click route in empty plan|OK|not possible, route cannot be starting point
|6|click node in plan with start marker only|OK|adds leg and green end marker
|7|click node in plan with last leg node-to-node|OK|adds leg, and green end marker, previous green end marker becomes brown via marker
|8|click node in plan with last leg via-route|OK|adds leg and green end marker, previous green end marker becomes invisible (brown route via marker remains)
|9|click route in plan with start marker only|OK|adds leg, adds brown via marker on the coordinate that was clicked and adds green end marker on destination node
|10|click route in plan with last leg node-to-node|OK|adds leg, adds brown via marker on the coordinate that was clicked and adds green end marker on destination node, previous green end marker becomes brown via marker
|11|in plan with start marker only, drag blue start marker, drop on other node|OK|plan start is moved
|12|in plan with first leg node-to-node, drag blue start marker, drop on other node|OK|plan start is moved, first leg is recalculated
|13|in plan with first leg via-route, drag blue start marker, drop on other node|NOK|plan start is moved, first leg is recalculated (keeps going through route-via-point)
|14|drag blue start marker, drop on route|NOK|not supported, no cursor/highlight change when hovering over route - marker jumps back to original position
|15|in plan with last leg node-to-node, drag green end marker, drop on other node|OK|plan destination is moved, last leg is recalculated
|16|in plan with last leg via-route, drag green end marker, drop on other node|?|plan destination is moved, last leg is recalculated
|17|in plan with last leg node-to-node, drag green end marker, drop on route|?|TODO
|18|in plan with last leg via-route, drag green end marker, drop on route|?|TODO
|19|drag brown node via marker, drop on other node|?|via point is moved, legs are recalculated
|20|drag brown via-route via marker, drop on other node|?|via point is moved, legs are recalculated
|21|drag brown node via marker, drop on route|?|via point is moved, legs are recalculated
|22|drag brown via-route via marker, drop on route|?|via point is moved, legs are recalculated
|23|drag node-to-node-leg, drop on node|?|TODO
|24|drag node-to-node-leg, drop on route|?|TODO
|25|drag via-route-leg, drop on node|?|TODO
|26|drag via-route-leg, drop on route|?|TODO
