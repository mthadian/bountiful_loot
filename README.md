# bountiful_loot

Technology: Spring-boot/Java

Setup Steps:

Method 1:

•	Copy the file /bobthief-1.jar to local machine

•	Using java, run the file, i.e, java -jar bobthief-1.jar. By default, the application will run on port 8080,however to change port,
run as java -jar -Dserver.port={port} bobthief-1.jar ,eg, java -jar -Dserver.port=8081 bobthief-1.jar or on powershell java -jar “-Dserver.port=8081” bobthief-1.jar
Method 2:
Open the folder from an ide like Eclipse, Netbeans or IntelliJ as a maven project. The prerequisite in this case is maven and java .

The code logic can be found in logic.java

Testing

To simulate the optimal loot, make a HTTP GET request to server-url:port/api/loot, ie 127.0.0.1:8080/api/loot . 
In the request body, send a json with the same structure as in the question:
eg

Input

[ { "weight": 5, "value": 10 }, { "weight": 4, "value": 40 }, { "weight": 6, "value": 30 }, { "weight": 4, "value": 50 } ]








Output 
{
    "totalValue": 90,
    "totalWeight": 8,
    "bobsLoot": [
        {
            "weight": 4,
            "value": 40
        },
        {
            "weight": 4,
            "value": 50
        }
    ]
}
