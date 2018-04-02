# Scapes Engine Android
Android code repository for the
[ScapesEngine](https://github.com/Tobi29/ScapesEngine).
For more info on the non-Android components head there.

This repository contains the modules required for Android support that can only
be built using the Android Build Tools.

Uses Android provided OpenGL ES 3.0 support (for graphics) and a custom built
OpenAL-soft binary` (for audio, most architectures included by default).

The backend provides an abstract activity which is easiest for displaying
just the game without any Android specific ui, however a few things are to note:
  * The engine is bound to the activity so that restarting the activity also
    restarts the engine. Consider setting up manual configuration change
    handling in the manifest. The engine should pick everything up out of the
    box, which is why supporting activity restarts does not actually help with
    anything
  * The only lifecycle events exposed directly can be listened for using a
    `ComponentLifecycle` attached to the engine. Any game data must be saved
    whenever `ComponentLifecycle::halt` is called, unless further precautions
    are taking using e.g. services.

## Roadmap
  * ~~Engine runs~~
  * ~~Fonts~~
  * ~~Scapes works more or less fully~~ (It tests most of the engine)
  * ~~Performance profiling integrations~~ (Systrace and CPU sampler do the job)
  * ~~Keyboard and mouse support~~ (Mouse capture is a noop before api level 26)
  * ~~Gamepad support~~
  * Use platforms APIs for ~~Audio~~ and Image decoding (MediaCodec is a piece
    of ****)
  * Power management (The engine currently only knows one mode: drain battery)
  * ChromeOS support (No idea if it might already run)
  * VR Support, Google says VR is ze future!

## Build
The project uses Gradle to build all modules.

Requires an Android SDK and NDK installed with build tools (version can be
configured in `gradle.properties`) and CMake support.

You can run `:publishToMavenLocal` to install all its module as maven artifacts.

To use anything you can add any module through jitpack.io or set up a composite
build.

# Modules

## Android Backend
Android backend for the engine using an activity and foreground service.

### Artifacts
  * AndroidBackend

## Android Parcel Tag
Glue layer between Android Parcels and Bundles and tag structures.

### Artifacts
  * AndroidParcelTag

# Android SQLite
Glue layer between Android provided SQLite support the the SQLFramework.

### Artifacts
  * AndroidSQLite

# Android SysTrace
Glue layer between the profiler used in the engine and systrace.

Note: Untested due to not-working related problems on phone.

### Artifacts
  * AndroidSysTrace

# OpenAL
Contains a build of [OpenAL-soft](https://github.com/kcat/openal-soft) and ALAN
to provide easy access to 3D audio on Android.

### Artifacts
  * OpenAL
