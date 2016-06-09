# Fluber
Fluber loads pictures from Flickr using the Flickr's search api.

The app supports landscape and portrait. It uses an action bar for search and shows a search history at the bottom of the search
field. It might be that on smaller devices in landscape the seach history is invisible. Starting using a Presenter pattern
it soon turned that paging is better done with a ViewModel. 

The development is considered done, when:

1. The app that implements the desired features and meets the requirement.
2. Dagger2 is used to resolve dependencies.
3. The build leverages flavors and build types.
4. Tests are written with Mockito, Espresso and Roboelectrics.
5. Material Design is integrated and styling and a clean resource .
6. A staggered way to display the images in the list is used and a transformation of the image size is supported.

# Dependencies

*RXJava *Retrofit *Picasso *GSON

# Build

The code should be easily build and run using Android Studio.
