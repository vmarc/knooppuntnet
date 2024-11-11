export default [
  {
    context: ["/api/**", "/admin-api/**"],
    target: "http://127.0.0.1:9005",
    secure: false,
  },
  {
    context: ["/websocket/*"],
    target: "ws://127.0.0.1:9005",
    secure: false,
    ws: true,
  },
  {
    context: ["/tiles/**", "/images/**", "/assets/**"],
    target: "https://experimental.knooppuntnet.nl",
    changeOrigin: true,
    secure: false,
  },
];
