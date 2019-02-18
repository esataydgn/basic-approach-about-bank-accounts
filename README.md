##	Bank Saving and Checking Account Logics 

#	Description 

	Consider two different types of bank accounts – savings accounts and checking accounts. Each account has an owner and a balance.
	Accounts also support deposits and withdrawals of money amounts. A savings account additionally supports interest payments to its owner
	based on a certain interest rate tied to the account. There should be a possibility to calculate and pay the appropriate interest to
	a savings account. A checking account additionally supports balance overdrafts restricted by a limit tied to the account.
	There should be a possibility to do cash transfers between checking accounts.  

#	Project frameworks	:
		Java 1.8
		Spring Boot
		Rest-Jersey
		Maven
		Test RestTemplate Spring

#	How to run the project. 
	
	Import the project to your favorite IDE. Update the project dependencies by maven and find the AssignmentApplication.java 
	 then right click and run the project as Spring Boot app.It will start as a SpringBoot application. 
	
	Additionally, the unit tests can be fired from AccountControllerTest.java by run this class.
	
#	Implementation
	
	Project made by 5 different Rest services which are create user and services, withdrawal money,deposit money, calculate interested rate,
	send money to another account from checking accounts.You can call these services by Postman or other Rest service caller programs.
	
	1- Creation of the user and accounts:
		This service for initialize a new user and accounts and the return type is user and including his/hers accounts 
		
			Please find the service specifications.
		
			- End Point			:	http://<host>:<port>/users
			- HTTP Code			:	201 (Created)
			- Request Type		:	POST
			- Request Body		:  {"name":"Esat"}
			- Response Example	:	{   "id": 267545360,
										    "name": "Esat",
										    "accounts": [
										        {
										            "accountType": "SAVING",
										            "userId": 267545360,
										            "accountId": "8923eb04-0d23-4739-a0b4-e0b786e74f53",
										            "balance": 5000,
										            "withdrawalLimit": null,
										            "interestRate": 0.5
										        },
										        {
										            "accountType": "CHECKING",
										            "userId": 267545360,
										            "accountId": "ae78303a-7907-489e-898d-84b7de90fdbf",
										            "balance": 5000,
										            "withdrawalLimit": 1000,
										            "interestRate": null
										        }
										    ]
										} 
										
										*id is unique User number
										*account ids are alse uuid unique ids  
										
													
		2. Deposit money: 			
			Deposit is supported by all accounts. There is no limitation.
				
			Service Specifications.
		
			-End Point 			: 	http://<host>:<port>/accounts/{accountId}/deposit/{amount}
			-HTTP Code			:	200 (OK)
			-Request Type		:	POST
			-Response Example	:	{	 "accountType": "SAVING",
										    "userId": 267545360,
										    "accountId": "8923eb04-0d23-4739-a0b4-e0b786e74f53",
										    "balance": 5100,
										    "withdrawalLimit": null,
										    "interestRate": 0.5
										}
										
		3. Withdraw Money
			Withdrawal is supported by all accounts. But Checking account just allowed to withdrawal limited amount of money. This limitation
			set when accounts are created. Default Max_Withdraw_Limit is 1000 for checking accounts. if the amount of withdrawal is higher
			than Max_Withdraw_Limit then 422 -Unprocessable entity will return.
													 
			-End Point 			: 	http://<host>:<port>/accounts/{accountId}/withdrawal/{amount}
			-HTTP Code			:	200 (OK)
			-Request Type		:	POST
			-Response Example	:	{   "accountType": "SAVING",
										    "userId": 267545360,
										    "accountId": "8923eb04-0d23-4739-a0b4-e0b786e74f53",
										    "balance": 4600,
										    "interestRate": 0.5
										}
										
		4. Calculate Interested Rate for Saving Accounts
			Saving accounts have an additional feature that a interested rate is related with Saving account and when the service call this rate 
			multiply with the current balance and add to current balance as real live saving accounts. Inital_Interested_Rate is 0.5 and it sets
			when saving accounts are created. When checking account is called for this service , 422 -Unprocessable entity will return.   			
 			 
 			 
			-End Point 			: 	http://<host>:<port>/accounts/{accountId}/calculate
			-HTTP Code			:	200 (OK)
			-Request Type		:	GET
			-Response Example	:	{   "accountType": "SAVING",
										    "userId": 268225427,
										    "accountId": "26b560dd-bd99-46a1-af10-f95396732c8e",
										    "balance": 6750,
										    "interestRate": 0.5
										}
		5- Send Money From Account to Another Account
			Checking accounts also have an additional feature that are able to send money to another account. It is work as withdrawal as well.
			 Checking accounts have withdrawal limits therefore sending money also has the same limit. When Saving account is called as
			 sourceAccount for this service , 422 -Unprocessable entity will return.
			
			-End Point 			: 	http://<host>:<port>/source-accounts/{sourceAccountId}/destination-accounts/{destinationAccountId}/transfer/{amount}
			-HTTP Code			:	200 (OK)
			-Request Type		:	POST
			-Response Example	:	[
											{
											    "accountType": "CHECKING",
											    "userId": 268225427,
											    "accountId": "b27077df-ad23-42bd-a635-a81fd58598ea",
											    "balance": 4000,
											    "withdrawalLimit": 1000
											},
											{
											    "accountType": "SAVING",
											    "userId": 268225427,
											    "accountId": "26b560dd-bd99-46a1-af10-f95396732c8e",
											    "balance": 7250,
											    "interestRate": 0.5
											}
										]
		