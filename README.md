Android application that allows users to search an open source movie API. It takes a search term, queries the API and returns a paginated list of results. Users can click on a movie to open up a more detailed view about that movie.

This project features the following:
* Written in Kotlin
* MVVM Architecture
* LiveData & Coroutines
* Responsive UI w/ ConstraintLayout

### OMDb API Key
You will need to provide your own API key to successfully get this application running locally. You can visit the website [here](http://www.omdbapi.com/) and request your own API key. You can make 1000 requests per day. It's completely free and should only take a few minutes.

Once you have your API key you'll want to open the `java/com/liamgoodwin/movieslist/util/Constants.kt` file and set it as the value of the `OMDB_API_KEY` variable.

### Running the project
1. Open the project in Android Studio.
2. Open the 'Build Variants' tab and ensure you have the `:app` module set to `debug`.
3. Open the run tab and press `run app`, or press the green triangular play button near the top right of the window.
4. Enjoy reading all of the posts in the League app!

### Running tests
1. Open the project in Android Studio.
2. Find the directory `app/src/test/`.
3. Right click the directory to open the options dropdown, and click `Run 'Tests' in ...` with the green triangular play button next to it.