# Building the Android Sample

You will need to put a few property values into a ```local.properties``` file.  Firstly go onto
the RapidAPI website and create a project - you'll need a project name and an API key.  If you want
to run the Nasa demo, you'll want to register for a Nasa API key also.

The app will build just fine, and all the code remains the same, it's just going to fail at runtime
if you dont have the various API keys in place.

## local.properties
```
nasa.api.key=...
rapidapi.api.key=...
rapidapi.project.name=...
```

