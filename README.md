# PathTracker
Tracks &amp; displays you journeys 

Lets user to start / stop tracking. Uses android service to keep tracking user position in the background.
Whenever activity starts, it bounds to service. When user starts tracking, service is explicitly started by startService().
Because of this service stays alive only if user is currently tracking his path. 

App contains two fragments, list and map fragment. List fragment displays all saved journeys, map fragment 
displays selected (red) & current(blue) journey on the map.

On tablets, two fragments are displayed at the same time in master detail flow, selecting element from the list 
refreshes journey on the map.

On phones each fragment covers whole screen, selecting element from the list displays new map fragment. 

App is designed using MVP, dagger2 & room
