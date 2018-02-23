# Scapes Engine Android
Android code repository for the
[ScapesEngine](https://github.com/Tobi29/ScapesEngine).
For more info on the non-Android components head there.

This repository contains the modules required for Android support that can only
be built using the Android Build Tools.

Uses Android provided OpenGL ES 3.0 support (for graphics) and a custom built
OpenAL-soft binary` (for audio, all architectures included by default).

There are two ready-to-use ways to run the engine:
  * Using just an activity: This is the simpler option that gives considerably
    better battery-life and integrates nicely into the pause and resume
    lifecycle events. It does however require handling a process kill after
    `Game.halt()` was called.
    To use just implement the `ScapesEngineActivity` class and start it.
    This activity should be registered in the manifest.
  * Using a bound service: This is the much safer option for complex and
    intensive games, that cannot quickly save all state during a pause and
    instead guarantees that the engine will always get shut down correctly
    unless Android or the user feels like flat out killing it without warning.
    To use you need to implement the `ScapesEngineServiceActivity` class by
    returning a class reference to an implementation of the
    `ScapesEngineService` class. Both should be registered in the manifest.

In both cases in the *will* get shut down and restarted when the activity is
destroyed, so enabling manual handling of configuration changes is highly
recommended. The backend tries its best to ensure that configuration changes
are either handled correctly or no special attention is required to begin with.

## Roadmap
  * ~~Engine runs~~
  * ~~Fonts~~
  * ~~Scapes works more or less fully~~ (It tests most of the engine)
  * ~~Performance profiling integrations~~ (Systrace and CPU sampler do the job)
  * ~~Keyboard and mouse support~~ (Mouse capture will probably only be
    available with Android O once implemented)
  * ~~Gamepad support~~
  * Use platforms APIs for Audio and Image decoding
  * Power management (The engine currently only knows one mode: drain battery)
  * Allow easy embedding without full engine running (Aka making engine lighter)
  * ChromeOS support (No idea if it might already run)
  * VR Support, Google says VR is ze future!

## Build
The project uses Gradle to build all modules.

Requires an Android SDK and NDK installed with build tools (version can be
configured in `gradle.properties`) and CMake support.

You can run :install to install all its module as maven artifacts.

To use anything you can add any module through jitpack.io or set up a composite
build. (Once Google fixes support for it)

## NOTICE
DO NOT UNDER ANY CIRCUMSTANCES DO ANYTHING UNEXPECTED FROM GOOGLE, IT **WILL**
BLOW UP!!!

NO WARRANTIES IF THIS MAKES YOUR COMPUTER EXPLODE BY TRYING TO INCLUDE
THE ENGINE REPO USING A COMPOSITE BUILD!!!

Basically: It took multiple days of work spanning over the course of a year to
find a way to set up these modules without some ridiculous errors from the
gradle plugin or IntelliJ breaking various nice features on normal Kotlin
modules when a single Android one is present.

# Modules

## AndroidBackend
Android backend for the engine using an activity and foreground service.

### Dependencies
  * OpenAL
  * [Engine](https://github.com/Tobi29/ScapesEngine/tree/master/Engine)
  * [ShaderCompiler](
    https://github.com/Tobi29/ScapesEngine/tree/master/ShaderCompiler)
  * [OpenALSounds](
    https://github.com/Tobi29/ScapesEngine/tree/master/Backends/OpenALSounds)

## AndroidParcelTag
Glue layer between Android Parcels and Bundles and tag structures

### Dependencies
  * [TagStructureBinary](
    https://github.com/Tobi29/ScapesEngine/tree/master/Utils/TagStructureBinary)

# AndroidSQLite
Glue layer between Android provided SQLite support the the SQLFramework.

### Dependencies
  * [FileSystems](
    https://github.com/Tobi29/ScapesEngine/tree/master/FileSystems)
  * [SQLFramework](
    https://github.com/Tobi29/ScapesEngine/tree/master/SQLFramework)

# AndroidSysTrace
Glue layer between the profiler used in the engine and systrace.

Note: Untested due to not-working related problems on phone.

### Dependencies
  * [Utils](https://github.com/Tobi29/ScapesEngine/tree/master/Utils)

# OpenAL
Contains a build of [OpenAL-soft](https://github.com/kcat/openal-soft) and ALAN
to provide easy access to 3D audio on Android.

### Dependencies
  * [Kotlin](https://kotlinlang.org)
  * OpenSL ES Android
  * CMake support in NDK (build only)
