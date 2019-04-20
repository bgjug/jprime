importScripts('https://storage.googleapis.com/workbox-cdn/releases/4.1.1/workbox-sw.js');

if (workbox) {
  console.log(`Yay! Workbox is loaded 🎉`);
} else {
  console.log(`Boo! Workbox didn't load 😬`);
}

workbox.routing.registerRoute(
  /\.(?:js|css|html)$/,
  workbox.strategies.staleWhileRevalidate()
)
workbox.routing.registerRoute(
  '/pwa',
  workbox.strategies.staleWhileRevalidate()
);

