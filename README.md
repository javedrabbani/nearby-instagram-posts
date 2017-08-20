# nearby-instagram-posts

A simple app that attempts to show Instagram posts near a user's current location.

Due to Sandbox nature of Instagram apps, the app will only show posts from users that have been added as Sandbox users.

The application starts with logging into a user's Instagram account. Once the login has been successful, application tries to fetch the access token
associated for current Instagram session.
If access token is retreived successfully, control gets transferred to another activity that takes care of displaying nearby Instagrams posts.

List of nearby locations is shown inside a drop down list which is updated periodically whenver user location changes a little.
Once user selects a location from the drop down list, corrsponding Instagram posts are searched for that selected location.

If there are any posts found, the list view loads the post images along side user name, full name and profile picture.

Currently there is no option to change the default radius to search around user's current location.

The application at present does not have any Settings to let user log-out from the current account.
