# Android Map App

This is an Android Application which let users to save destinations and get distances between the current location and the destinations.
It is implemented with Android Studio.


## User Interface

### Main View

First view when the application starts.<br/><br/>


<p float="left">
<img src="imgs/1.png" width="250" hspace="10">
<img src="imgs/2.png" width="500" hspace="10">
</p>


<br/><br/>There are two textboxes for users to input the name and the address of the location.

The location will be added to the list once the user clicks ADD button.<br/><br/>


<p float="left">
<img src="imgs/3.png" width="250" hspace="10">
<img src="imgs/4.png" width="500" hspace="10">
</p>


<br/><br/>The user can select a location from the dropdown.
The user can remove the location from the list by clicking REMOVE button.

The view changes to map view when the user clicks GO button.
<br/><br/>
### Map View

It show a map. The map can be zoomed in and out and panned.

When the user clicks the GO button on the main view, it displays a line between the current location and the location that the user selected.<br/><br/>


<img src="imgs/5.png" width="250" hspace="10">


<br/><br/>The user can get the distance from the current location to the selected location by clicking the GET DISTANCE button.<br/><br/>


<img src="imgs/6.png" width="250" hspace="10">


<br/><br/>The map and distance are displayed next to each other with landscape layout.<br/><br/>


<img src="imgs/7.png" width="500" hspace="10">


<br/><br/>The destination point on the map can be dragged to different position.
The application will calculate the updated distination when the user click the GET DISTANCE button after the destination point changed.<br/><br/>


<p float="left">
<img src="imgs/8.png" width="250" hspace="10">
<img src="imgs/9.png" width="250" hspace="10">
<img src="imgs/10.png" width="500" hspace="10">
</p>


<br/><br/>The destination point can be saved by clicking SAVE PLACE button.
The saved destination point will be displayed when the user select the destination next time.


## Implementation

### Activities

There are three activities.

- MainActivity for adding and removing destinations.
- MapActivity for displaying a map.
- DistanceActivity for displaying the distance between current location and the selected destination.


### Layouts

This applciation supports portrait and landscape layouts.

There are two fragments. One is for a map and another one is for displaying a distance next to the map with landscape layout.


### Persistent state

The camera position and destination position are saved locally so it persists the state of the map across applications and activity instance transitions.


### Current location

There are two services for background tasks.
One is normal service which gets the last known location. Another one is intent service which updates the current location.

Two broadcast receivers are registered to receive results from the services.

This is for getting the current location if the application cannot get a recent location reading from the system.
Also, it allows the application to update the current location in background if the user moves.

The Criteria class was used to select the best provider which estimates current location more accurately.


### Database

Database is used for saving destinations in this application.


### Destinations

This application gets latitude and longitude from addresses using Geocoder.
AsyncTask is used to do this process in background.
