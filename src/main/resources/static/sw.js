importScripts('https://storage.googleapis.com/workbox-cdn/releases/4.1.1/workbox-sw.js');

if (workbox) {
  console.log('Yay! Workbox is loaded');
} else {
  console.log("Boo! Workbox didn't load");
}


workbox.routing.registerRoute(
  '/pwa',
  new workbox.strategies.StaleWhileRevalidate()
);

workbox.routing.registerRoute(
  '/js/pwa.js',
  new workbox.strategies.StaleWhileRevalidate()
);

workbox.routing.registerRoute(
  '/halls',
  new workbox.strategies.StaleWhileRevalidate()
);

workbox.routing.registerRoute(
  new RegExp('/pwa/.*'),
  new workbox.strategies.StaleWhileRevalidate()
);

