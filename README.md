# Twitter Clone 

###Twitter Clone for Android

![app screenshots](https://github.com/teymourk/Android-Twitter-Client-SQL-/blob/master/AppScreens.png)

### Following Api URL

```
    REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    REST_CONSUMER_KEY = "Z6BdukyrHfAsxbfzxU5NtV7RQ";       // Change this
    REST_CONSUMER_SECRET = "pL2dyJ4V2GasYIwMCAoJoOGBnLuANRa3ABzbGckKUE2AsoA9kK"; // Change this
    REST_CALLBACK_URL = "oauth://TwitterClient"; // Change this (here and in manifest)
```
### Follwoing Api Calls

## Verify user Credentials on Auth

```
/account/verify_credentials.json

```
## Fetch Timeline (GET Request)
```
statuses/home_timeline.json?count=10
```

## Tweet (Post Request)
```
statuses/update.json?status=" + text
```
## Update User Bio (POST Request)
```
account/update_profile.json?description=" + text
```

