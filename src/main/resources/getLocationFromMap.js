function getLocationFromMap() {
    // Example: extract latitude and longitude
    var latitude = map.getLatitude();
    var longitude = map.getLongitude();

    // Example: call Java method to pass data
    mapDataExtractor.onDataReceived(latitude + ", " + longitude);
}
