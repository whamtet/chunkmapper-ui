const bangkok = [13.7563, 100.5018];

var map = L.map('map', {doubleClickZoom: false}).setView(bangkok, 12);
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
  attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);

L.Control.geocoder().addTo(map);

var newLocation;
map.on('dblclick', e => {
  newLocation = e.latlng;
  document.getElementById('modal-btn').click();
});
