const bangkok = [13.7563, 100.5018];

const map = L.map('map', {doubleClickZoom: false}).setView(bangkok, 12);
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
  attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);

L.Control.geocoder().addTo(map);

var newLocation;
map.on('dblclick', e => {
  newLocation = e.latlng;
  document.getElementById('modal-btn').click();
//   ctmx_electron.service.process.new_game('my-game');
});

var steve;
var added = false;
const setSteve = (lat, lng) => {
  if (added) {
    steve.remove();
    steve.setLatLng([lat, lng]);
  } {
    added = true;
    const steveIcon = L.divIcon({
      html: '<img src="steve2.jpeg" style="border: 1px solid black">'
    });
    steve = L.marker([lat, lng], {icon: steveIcon}).addTo(map);
  }
};


