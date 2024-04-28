# SanlamPOC

This is a Sanlam POC repository which contains a vanilla scenario (Bank Account Withdrawal) solution clean up

# Approach 
- Looking at the code listed the TODO tasks as below :
- Normalise to SOLID as much as feasible
- Implement basic exception handling to cover bad request and business exceptions
- Generalise the static values into shared and segregated constants
- Implement utility to build queries
- Logger for reporting if applicable

# Probable Extensions 
- There should be segregation between the system(db) failures and business logic failures, all the failed events should be reported to SNS with probable root cause
- If any failures in reporting to SNS should be recorded into the logger as a business exception with RCA for future extensions to infrastructure clean up


# Dependencies 
- spring-boot : project set up
- slf4j : for logging
- awssdk : publishing results to SNS and setting up regions
