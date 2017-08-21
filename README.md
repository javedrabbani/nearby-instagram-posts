# nearby-instagram-posts

A simple app that attempts to show Instagram posts near a user's current location.

Due to Sandbox nature of Instagram apps, the app will only show posts from users that have been added as Sandbox users for instagram app in question.

The application starts by logging into a user's Instagram account. Once the login has been successful, application tries to fetch  access token
for that Instagram session.
If access token is retreived successfully, control gets transferred to another activity that takes care of displaying nearby Instagrams posts.

List of nearby locations is shown inside a drop down list which is updated periodically whenever user location changes a bit.
Once user selects a location from the drop down list, Instagram posts are searched for that selected location.

If there are any posts found, list view loads the post images along side user name, full name and profile picture.

Currently there is no option to change the default search radius around user's current location.

To use the app with your Instagram app, simply replace "client_id" and "content_secret" string resources with your Instagram app "CLIENT_ID" and "CLIENT_SECRET". Also update "REDIRECT_URI" with your redirect uri as defined for your app on Instagram developer website.