# WeatherApp Using Kotlin and XML.

<img width="885" alt="Screenshot 2023-02-26 003801" src="https://user-images.githubusercontent.com/97782768/221375249-3f1050e5-f4f3-468a-bbbe-87683923ac30.png">

_Locating the user automatically, Calling Accu weather Api to get the weather data.

_this app has the following features:

-auto-update
-auto location detection
-one activity only
-easy to customize
-Executor class instead of AsyncTask

Why Executor class? because AsyncTask is deprecated (leaks may occur if the AsyncTask is not static), 
Here, we are using the Executor-class as an alternative to the deprecated AsyncTask. it allows us to execute submitted (Runnable) tasks, 
I like using this class because it allows us to pass a task to be executed by a thread asynchronously (in the background), then handle the 
results (to update our UI in this case) using a Handler and catch any exceptions.

-I used Accu Weather API for this app, you go there and create an account, then you'll receive your own API key which you'll use in the app.
https://developer.accuweather.com/

-Read The Full Article About The Code Here: https://www.progressiveorigin.com/post/weather-app-android-kotlin#viewer-5ijov
