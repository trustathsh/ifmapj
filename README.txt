ifmapj
======
This package contains an *experimental* IF-MAP library for Java. It eases
the development of IF-MAP clients. ifmapj supports IF-MAP 2.0 [1].

Development was done by Fachhochschule Hannover (Hannover University of
Applied Sciences and Arts) [2] within the ESUKOM research project [3].

Documentation
=============
Most parts of the code are well commented. A UML class diagram is provided
within the src/main/doc folder. In order to start your own IF-MAP client,
refer to the examples in the ifmapj-examples project (available separately).
The default password for the keystore included is 'ifmapj'.

Libraries
=========
ifmapj uses the Apache httpcore library. However, if this library is not
available, ifmapj supports a fallback mechanism that works without any
additional library.
If you do not want to use the Apache httpcore library, but cannot remove
it from the CLASSPATH, e.g. on Android, it is possible to set the system
property "ifmapj.communication.handler" to "java".
This property may also be set to "apache", in which case no fallback is
used if the Apache httpcore library is not available, but initialization
simply fails.

IF-MAP Server Verification
==========================
In order to disable verification of the IF-MAP Server certificate,
the system property "ifmapj.communication.verifypeercert" can be set
to false. If this property is either unset or set to true, verification
of the IF-MAP Server certificate is enabled. Setting the property to any
value other than true or false, will lead to an InitializationException.

Additonally, the resolved IPs from the common name of the server
certificate can be compared with the resolved IPs of the hostname
which was used to connect to. The system property is called
"ifmapj.communication.verifypeerhost". If it is set to true, this
check is done, otherwise it is not, regardless of the value of the
system property. Note, the realization of this functionality is rather
basic. Therefore, if you are concerned about this at all, you should
check ifmapj's implementation and decide whether it meets
your requirements.

IF-MAP Server Hostname / Common Name Verification 
=================================================

Build
=====
Just execute

	mvn package

in order to create a binary jar file, a source jar file and this project
archive.

Feedback
========
If you have any questions, problems or comments, please contact

	trust@f4-i.fh-hannover.de

LICENSE
=======
ifmapj is licensed under the Apache License, Version 2.0 [4].

Changelog
=========

0.1.4
-----

* Refactoring of Identifier and Request handling:
  - More modular to allow registration of CustomIdentifier
    and CustomRequest handlers.
  - see: Identifiers.registerIdentifierHandler() and
    Requests.registerRequestHandler().

* Make setter methods of Identifiers deprecated to
  mark the transition to immutable Identifier objects.

* Deprecate IdentifierFactory and RequestFactory with
  factory classes Identifiers and Requests.

0.1.3
-----
* Fixed typo 'disoverer-id' in standard metadata factory.

* Updated testing keystore

* Fix Device identifier parsing

* Introduce ifmapj.communication.verifypeercert and
  ifmapj.communication.verifypeerhost system properties

0.1.2
-----
* Add Chunked Transfer Encoding functionality to
  JavaCommunicationHandler.

* Avoid a cloneNode() bug in Android < API Level 11,
  which leads to DOMExceptions when DOM Level 1
  methods are used to create DOM attributes and
  elements.

* Unified CommunicationHandler implementations

URLs
====
[1] http://www.trustedcomputinggroup.org/files/static_page_files/1528BAC2-1A4B-B294-D02E5F053A3CF6C9/TNC_IFMAP_v2_0r36.pdf

[2] http://trust.inform.fh-hannover.de

[3] http://www.esukom.de

[4] http://www.apache.org/licenses/LICENSE-2.0.html
