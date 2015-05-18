echo off
cls
del Main.class
del ImageItem.class
del ImageList.class
del CameraDisplay.class
del ImageProcessor.class
del VideoIndicator.class
echo on
javac CameraControlCommunicator.java -encoding UTF-8
javac Main.java -encoding UTF-8
java Main