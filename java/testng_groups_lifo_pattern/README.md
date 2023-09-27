### Testng groups in LIFO (Last In First Out) pattern with parallel execution

This type of LIFO pattern in testng groups can be used for creating/deleting resources in correct order.


- Example show opening and closing Russian matryoshka dolls in correct order
- Groups used in this case are `open` and `close`

    ```sh
    mvn clean test -DsuiteXmlFile=testng.xml

    [INFO] Running TestSuite
    Thread 26 - Russian matryoshka big open
    Thread 27 - Russian matryoshka medium open
    Thread 28 - Russian matryoshka small open
    Thread 29 - Russian matryoshka small close
    Thread 30 - Russian matryoshka medium close
    Thread 36 - Russian matryoshka big close
    ```
