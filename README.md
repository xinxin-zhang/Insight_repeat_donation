The structure of this repo is like this

    ├── README.md 
    ├── run.sh
    ├── src
    │   └──com
    |       └── company
    |           └── Main.java
    |           └── donation_analytics.java
    ├── input
    │   └── percentile.txt
    │   └── itcont.txt
    ├── output
    |   └── repeat_donors.txt
    ├── insight_testsuite
        └── run_tests.sh
        └── tests
        └── test_1
        |   ├── input
        |   │   └── percentile.txt
        |   │   └── itcont.txt
        |   |__ output
        |   │   └── repeat_donors.txt                 
        ├── my_test1
            ├── input
        |   │   └── percentile.txt
        |   │   └── itcont.txt
            |── output
                └── repeat_donors.txt
               
 The codes are writen by Java. In insight_testsuite, two tests are performed. The first one is the sample test shown in coding challenge instructions.
 The second one is my own inputs and outputs. 
 The input files(percentile.txt and itcont.txt) in input folder are subject to be replaced. This is the location where user can uplaod their own input files for testing or production.
 
 The code is located at scr/com/company/  The key java code is donation_analytics.java  The comments and annotations are inside the java file. The tests are all passed. 
 
