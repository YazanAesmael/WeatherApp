# WeatherApp Using Kotlin and XML.

![Screenshot_20220726_231153_google-pixel4xl-clearlywhite-portrait](https://user-images.githubusercontent.com/97782768/221375193-5f812ce1-b9fe-450b-b489-2538372eb47d.png)

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
