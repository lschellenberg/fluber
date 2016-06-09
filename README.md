# Fluber

Fluber loads pictures from Flickr using Flickr's search API.

The app supports landscape and portrait. It uses an action bar for search and shows a search history at the bottom of the search
field. It might be that on smaller devices the search history is invisible in landscape. Starting using a Presenter pattern
it soon turned out that paging is better done with a `ViewModel`. Search uses MVP and Gallery doesn't.

Here is the progress of current development:

- [x]  The app implements the desired features and meets the requirement.
- [ ] Dagger2 is used to resolve dependencies.
- [ ] The build leverages flavors and build types.
- [ ] Tests are written with Mockito, Espresso and Roboelectrics.
- [ ] Material Design is integrated and styling and a clean resource.
- [ ] A staggered way to display the images in the list is used and a transformation of the image size is supported.

The list above is considered also as a priority list.

# Dependencies

- RXJava
- Retrofit
- Picasso
- GSON

# Build

Import project into Android Studio. In `app/build.gradle` provide your Flickr API key:

```groovy
android {
    ...
    defaultConfig {
        ...
        manifestPlaceholders = [flickrApiKey: "YOUR_FLICKR_API_KEY"]
    }
}
```
