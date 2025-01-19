# Andriawan ATM Apps

## How to run this application

To run this app, execute file `start.sh` it will run command `mvn package` under the hood. After the compilation process is done, 
the script will invoke java -jar command as follow

`java -jar [root]/target/andriawan-takehome-test-1.0-SNAPSHOT.jar`

the folder structure of maven project was generated from command 

`mvn archetype:generate -DgroupId=com.mycompany.app -DartifactId=my-app -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.5 -DinteractiveMode=false`

this command was taken from official maven website [https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html]


## Commands


* `help` - show list of command

* `login [name]` - Logs in as this customer and creates the customer if not exist

* `deposit [amount]` - Deposits this amount to the logged in customer

* `withdraw [amount]` - Withdraws this amount from the logged in customer

* `transfer [target] [amount]` - Transfers this amount from the logged in customer to the target customer

* `logout` - Logs out of the current customer

* `exit` - exit from application


## Example Session



Your console output should contain at least the following output depending on the scenario and commands. But feel free 

to add extra output as you see fit.



```bash

$ login Alice

Hello, Alice!

Your balance is $0



$ deposit 100

Your balance is $100



$ logout

Goodbye, Alice!



$ login Bob

Hello, Bob!

Your balance is $0



$ deposit 80

Your balance is $80



$ transfer Alice 50

Transferred $50 to Alice

your balance is $30



$ transfer Alice 100

Transferred $30 to Alice

Your balance is $0

Owed $70 to Alice



$ deposit 30

Transferred $30 to Alice

Your balance is $0

Owed $40 to Alice



$ logout

Goodbye, Bob!



$ login Alice

Hello, Alice!

Your balance is $210

Owed $40 from Bob



$ transfer Bob 30

Your balance is $210

Owed $10 from Bob



$ logout

Goodbye, Alice!



$ login Bob

Hello, Bob!

Your balance is $0

Owed $10 to Alice



$ deposit 100

Transferred $10 to Alice

Your balance is $90



$ logout

Goodbye, Bob!

```