PROXY_CONFIG = {
  "/api/**": {
    "target": "http://experimental.knooppuntnet.nl",
    "secure": false
  },
  "/json-api/**": {
    "target": "http://localhost:9005",
    "secure": false
  },
  "/tiles/**": {
    "target": "https://experimental.knooppuntnet.nl",
    "secure": false
  }
};

module.exports = PROXY_CONFIG;
