PROXY_CONFIG = {
  "/api/**": {
    "target": "https://experimental.knooppuntnet.nl",
    "secure": false
  },
  "/json-api/**": {
    "target": "https://experimental.knooppuntnet.nl",
    "secure": false
  },
  "/tiles/**": {
    "target": "https://experimental.knooppuntnet.nl",
    "secure": false
  },
  "**": {
    "target": "http://localhost:9000",
    "secure": false,
    "bypass": function (req) {
      console.log("URL " + req.url);
      if (req.headers.accept.indexOf("html") !== -1) {
        return "/index.html";
      }
    }
  }
};

module.exports = PROXY_CONFIG;
