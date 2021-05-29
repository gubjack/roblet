
# Changes

## 5
* *Bug fix*: Failing IO during re-connection must not end the retry loop
* revised logging
* `Slot.close()` now without `InterruptedException`
* Remove simple constructor for "old" `Client`
* Remove reference to server from "old" `Slot` type
* Deprecated "old" `Client`, `Server` and `Slot` from package
`genRob.genControl.client`

## 4
* Revised logging concept via `org.roblet.client.Logger`
* **roblet-protocol 1.0**
* **roblet-server-unit 3.0**
* new central class `org.roblet.client.Slot`
* Streams functionality removed
* **roblet-root 2**
* Adapted to generic roblet definition
* Exclude `genRob.genControl` packages from Javadoc

## 3.0
* Thread (group) names shortened
* Class loading
    * only one ClassLoader for conversions
    * determined using the loader of the clients classes (not from calling thread anymore)
    * dependency to RMI eliminated
    * logging keyword **load** removed
* Conversions unified for all object types except String
* Unused elements removed
    * Package <CODE>util.operation</CODE>
    * Helper class <CODE>ClassLoaders</CODE> removed

## 2.1
* Handling of conversions in the calling threads when
    * running roblets
    * writing to streams
    * invoking far instances
* more complete javadoc

## 2.0
* More specific ConversionException when roblet is not convertable
* Ensire closing streams on conversions
* Only protocol version 5 will be accepted
* communication threads log runtime exceptions
* Change of the class loading to use only calling threads loaders
* changes information file renamed to CHANGES.md

## 1.0
* Switch to Git
* Client can now take log criteria on creation thus avoiding use of Java
environment definitions
* Logger commented
* Usage of <CODE>NetUnit</CODE> for Client instances in Roblets
* **roblet-server-unit 1.0**
* Change to **Java 11**
* Change information moved to <CODE>changes.md</CODE>

## 0.2
* Calling far methods will result in a
<CODE>org.roblet.protocol.SlotNotActiveException</CODE> in case the server
meanwhile destroyed the slot
* **roblet-protocol 0.2.1**
* **roblet-root 0.2**
* <CODE>Client.getServer(...)</CODE> will now always return new server
representations - instead of spending effort to match
* New exception <CODE>org.roblet.client.InvocationException</CODE> for cases
when calling a far method runs into an error that is not the direct result of
that method
* Added <CODE>README.md</CODE>

## 0.1
* <CODE>genRob.genControl.client.Server#getSlot()</CODE> will now possibly also
throw <CODE>genRob.genControl.client.ServerException</CODE>
* A server change (e.g. due to restart) will now end all for the past server
waiting threads
* Early exceptions in case the user accesses functionality that on a closed
transport

## 0.0
* Sources from **Roblet-Development-Kit (RDK) 3.1**
* Author information
* MIT license
* Change to **org.roblet:roblet-root:0.1**
and **org.roblet:roblet-protocol:0.1**
