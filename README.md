# Scapes Engine Android
Android code repository for the
[ScapesEngine](https://github.com/Tobi29/ScapesEngine).
For more info on the non-Android components head there.

This repository contains the modules required for Android support that can only
be built using the Android Build Tools.

Uses Android provided OpenGL ES 3.0 support (for graphics) and a custom built
OpenAL-soft binary` (for audio, all architectures included by default).

To allow proper clean shutdowns this uses a foreground service for running the
engine itself with an activity bound to it to provide user interaction.

## Roadmap
  * ~~Engine runs~~
  * ~~Fonts~~
  * ~~Scapes works more or less fully~~ (It tests most of the engine)
  * ~~Dialogs~~ (Save dialog not implemented yet)
  * ~~Performance profiling integrations~~ (Systrace and CPU sampler do the job)
  * Keyboard and mouse support
  * Gamepad support
  * Use platforms APIs for Audio and Image decoding
  * Power management (The engine currently only knows one mode: drain battery)
  * Allow easy embedding without full engine running
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

# AndroidSQLite
Glue layer between Android provided SQLite support the the SQLFramework.

### Dependencies
  * [FileSystems](
    https://github.com/Tobi29/ScapesEngine/tree/master/FileSystems)
  * [SQLFramework](
    https://github.com/Tobi29/ScapesEngine/tree/master/SQLFramework)

# AndroidSSLProvider
Alternative backend for SSL certificate handling, required on Android due to
limited standard library.

### Dependencies
  * [ServerFramework](
    https://github.com/Tobi29/ScapesEngine/tree/master/ServerFramework)

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
