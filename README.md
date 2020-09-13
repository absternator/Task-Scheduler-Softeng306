# Team 17 Peaches Group Project 🍑

## Team
| GitHub Username | Full Name | UPI |
| --------------- | --------- | --- |
| annithinggoes | Annie Chau | acha818 |
| dongmeilim | Dong-Mei Lim | dlim654 |
| jayzed99 | Jacinta Zhang | jzha700 |
| xinyi98 | Xinyi Guo | xguo679 |
| absternator | Anmol Thapar | atha969 |


## Building and Running
The project is built using gradle.

### To build the project

In Intellij:
 - Open up the gradle tab on the right hand side
 - Go to build folder and double click the build command

On command line: run `.\gradlew build`

## To run the project
 
*Make sure to have initially built the project first*
  
 On command line: run `.\gradlew run --args "<input.dot> <num of processors> <options>"`
 
 ## Running the jar file
 
 `java -jar scheduler.jar <input.dot> <num of processors> <options>`
 
## Options

|  Options |Argument | Description  |
|---|---|---|
|  `-p` |&lt;number of cores> |  use N cores for execution in parallel (default is sequential) |
|  `-v`| | visualise the search  |
|  `-o` |&lt;OUTPUT> |  output file is named OUTPUT (default is INPUT-output.dot) |

![](/Cover-img.png)
