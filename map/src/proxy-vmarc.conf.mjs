export default [
  {
    context: [
      "/api/**",
      "/admin-api/**",
      "/tiles/**",
      "/images/**",
      "/videos/**",
      "/assets/**",
    ],
    target: "http://127.0.0.1:8000",
    secure: false,
  },
  {
    context: [
      "/websocket/*",
    ],
    target: "ws://127.0.0.1:9005",
    secure: false,
    ws: true,
  },
];
