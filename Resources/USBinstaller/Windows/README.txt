Steps to create a new installer:
- run maven install in OpenRobertaParent to get the jar file and lib folder
- copy a 32-bit jre (1.7.0_51) and rename the jre to "java".
- run launch4j (installed your windows machine) and open the launch4j.xml
- create the .exe with launch4j
- run the build.bat
-> a .msi file should appear - test it!