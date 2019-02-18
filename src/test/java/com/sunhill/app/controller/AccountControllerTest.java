package com.sunhill.app.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.sunhill.app.AssignmentApplication;
import com.sunhill.app.model.Account;
import com.sunhill.app.model.User;
import com.sunhill.app.model.enumtype.AccountType;
import com.sunhill.app.model.request.CreateUserRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AssignmentApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerTest {

	@Autowired
	TestRestTemplate testRestTemplate;

	@Test
	public void testCreateUsersAndAccounts() {
		CreateUserRequest createUserRequest = new CreateUserRequest();
		createUserRequest.setName("Test Name");
		ResponseEntity<User> response = testRestTemplate.postForEntity("/users", createUserRequest, User.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		User user = response.getBody();
		assertThat(user).isNotNull();
		assertThat(user.getId()).isNotNull();
		assertThat(user.getName()).isEqualTo("Test Name");
		assertThat(user.getAccounts()).extracting("accountType", "withdrawalLimit", "balance", "interestRate")
				.contains(tuple(AccountType.SAVING, null, new BigDecimal("5000"), new BigDecimal("0.5")))
				.contains(tuple(AccountType.CHECKING, new BigDecimal(1000), new BigDecimal("5000"), null));

	}

	// Test to withdrawal All money from SAVING account
	@Test
	public void testWithdrawalFromSavingAccount() {

		CreateUserRequest createUserRequest = new CreateUserRequest();
		createUserRequest.setName("Test Name");
		ResponseEntity<User> response = testRestTemplate.postForEntity("/users", createUserRequest, User.class);

		String accountId = "";
		BigDecimal accountInitialAmount = new BigDecimal(0);
		for (Account account : response.getBody().getAccounts()) {
			if (account.getAccountType().equals(AccountType.SAVING)) {
				accountId = account.getAccountId();
				accountInitialAmount = account.getBalance();
			}
		}

		ResponseEntity<Account> withdrawalResponse = testRestTemplate
				.postForEntity("/accounts/" + accountId + "/withdrawal/" + accountInitialAmount, null, Account.class);
		assertThat(withdrawalResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		Account account = withdrawalResponse.getBody();
		assertThat(account.getBalance().compareTo((BigDecimal.ZERO)) == 0);

	}

	// withdrawal in the range of withdrawal limit for CHECKING account
	@Test
	public void testWithdrawalFromCheckingAccount() {

		CreateUserRequest createUserRequest = new CreateUserRequest();
		createUserRequest.setName("Test Name");
		ResponseEntity<User> response = testRestTemplate.postForEntity("/users", createUserRequest, User.class);

		String accountId = "";
		BigDecimal accountInitialAmount = new BigDecimal(0);
		for (Account account : response.getBody().getAccounts()) {
			if (account.getAccountType().equals(AccountType.CHECKING)) {
				accountId = account.getAccountId();
				accountInitialAmount = account.getBalance();
			}
		}

		ResponseEntity<Account> withdrawalResponse = testRestTemplate
				.postForEntity("/accounts/" + accountId + "/withdrawal/" + 1000, null, Account.class);
		assertThat(withdrawalResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		Account account = withdrawalResponse.getBody();
		assertThat(accountInitialAmount.compareTo(account.getBalance().add(new BigDecimal(1000))) == 0);
	}

	// withdrawal out the range of withdrawal limit for CHECKING account. return
	// unprocessable entity = 422
	@Test
	public void testWithdrawalFromCheckingAccountOutOfLimitation() {

		CreateUserRequest createUserRequest = new CreateUserRequest();
		createUserRequest.setName("Test Name");
		ResponseEntity<User> response = testRestTemplate.postForEntity("/users", createUserRequest, User.class);

		String accountId = "";
		for (Account account : response.getBody().getAccounts()) {
			if (account.getAccountType().equals(AccountType.CHECKING)) {
				accountId = account.getAccountId();
			}
		}

		ResponseEntity<Account> withdrawalResponse = testRestTemplate
				.postForEntity("/accounts/" + accountId + "/withdrawal/" + 2000, null, Account.class);
		assertThat(withdrawalResponse.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@Test
	public void testDepositMoneyToAllAccounts() {

		CreateUserRequest createUserRequest = new CreateUserRequest();
		createUserRequest.setName("Test Name");
		ResponseEntity<User> response = testRestTemplate.postForEntity("/users", createUserRequest, User.class);

		String savingAccountId = "";
		String checkingAccountId = "";
		BigDecimal checkingAccountInitialAmount = new BigDecimal(0);
		BigDecimal savingAccountInitialAmount = new BigDecimal(0);
		for (Account account : response.getBody().getAccounts()) {
			if (account.getAccountType().equals(AccountType.CHECKING)) {
				checkingAccountId = account.getAccountId();
				checkingAccountInitialAmount = account.getBalance();
			} else {
				savingAccountId = account.getAccountId();
				savingAccountInitialAmount = account.getBalance();
			}
		}
		ResponseEntity<Account> depositResponseForSavingAcc = testRestTemplate
				.postForEntity("/accounts/" + savingAccountId + "/deposit/" + 2000, null, Account.class);
		assertThat(depositResponseForSavingAcc.getStatusCode()).isEqualTo(HttpStatus.OK);
		Account savingAcc = depositResponseForSavingAcc.getBody();
		assertThat(savingAccountInitialAmount.compareTo(savingAcc.getBalance().subtract(new BigDecimal(2000))))
				.isEqualTo(0);

		ResponseEntity<Account> depositResponseForCheckingAcc = testRestTemplate
				.postForEntity("/accounts/" + checkingAccountId + "/deposit/" + 2000, null, Account.class);
		assertThat(depositResponseForCheckingAcc.getStatusCode()).isEqualTo(HttpStatus.OK);
		Account checkingAcc = depositResponseForCheckingAcc.getBody();
		assertThat(checkingAccountInitialAmount.compareTo(checkingAcc.getBalance().subtract(new BigDecimal(2000))))
				.isEqualTo(0);
	}

	// checking account does not have interested rate and it will return
	// unprocessable
	// entity= 422
	@Test
	public void testInterestedRateForCheckingAccount() {

		CreateUserRequest createUserRequest = new CreateUserRequest();
		createUserRequest.setName("Test Name");
		ResponseEntity<User> response = testRestTemplate.postForEntity("/users", createUserRequest, User.class);

		String accountId = "";
		for (Account account : response.getBody().getAccounts()) {
			if (account.getAccountType().equals(AccountType.CHECKING)) {
				accountId = account.getAccountId();
			}
		}

		ResponseEntity<Account> interestedRateResponse = testRestTemplate
				.getForEntity("/accounts/" + accountId + "/calculate/", Account.class);
		assertThat(interestedRateResponse.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
	}

	// test Interested rate for Saving Account. account interested rate is 0.5 and
	// the method calculate the new saving and add to the account balance
	@Test
	public void testInterestedRateForSavingAccount() {

		CreateUserRequest createUserRequest = new CreateUserRequest();
		createUserRequest.setName("Test Name");
		ResponseEntity<User> response = testRestTemplate.postForEntity("/users", createUserRequest, User.class);

		String accountId = "";
		BigDecimal savingAccountInitialAmount = new BigDecimal(0);
		for (Account account : response.getBody().getAccounts()) {
			if (account.getAccountType().equals(AccountType.SAVING)) {
				accountId = account.getAccountId();
				savingAccountInitialAmount = account.getBalance();
			}
		}

		ResponseEntity<Account> interestedRateResponse = testRestTemplate
				.getForEntity("/accounts/" + accountId + "/calculate/", Account.class);
		assertThat(interestedRateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		Account savingAcc = interestedRateResponse.getBody();
		assertThat(savingAccountInitialAmount.multiply(new BigDecimal(0.5).add(BigDecimal.ONE))
				.compareTo(savingAcc.getBalance())).isEqualTo(0);
	}

	// Checking accounts only are allowed to send money to other accounts.But
	// receiver are can be both
	@Test
	public void testSendMoneyToAccount() {

		CreateUserRequest createUserRequest = new CreateUserRequest();
		createUserRequest.setName("Test Name");
		ResponseEntity<User> response = testRestTemplate.postForEntity("/users", createUserRequest, User.class);

		String sourceAccount = "";
		String destinationAccount = "";
		BigDecimal sourceCheckingAccInitialAmount = new BigDecimal(0);
		BigDecimal destinationAccountInitialAmount = new BigDecimal(0);
		for (Account account : response.getBody().getAccounts()) {
			if (account.getAccountType().equals(AccountType.CHECKING)) {
				sourceAccount = account.getAccountId();
				sourceCheckingAccInitialAmount = account.getBalance();
			} else {
				destinationAccount = account.getAccountId();
				destinationAccountInitialAmount = account.getBalance();
			}
		}

		ResponseEntity<Account[]> transferResponse = testRestTemplate.postForEntity("/source-accounts/" + sourceAccount
				+ "/destination-accounts/" + destinationAccount + "/transfer/" + 1000, null, Account[].class);
		assertThat(transferResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		BigDecimal finalCheckingAccAmount = new BigDecimal(0);
		BigDecimal finalDestinationAccAmount = new BigDecimal(0);
		for (Account account : transferResponse.getBody()) {
			if (account.getAccountType().equals(AccountType.CHECKING)) {
				finalCheckingAccAmount = account.getBalance();
			} else {
				finalDestinationAccAmount = account.getBalance();
			}
		}

		assertThat(finalCheckingAccAmount.compareTo(sourceCheckingAccInitialAmount.subtract(new BigDecimal(1000))))
				.isEqualTo(0);

		assertThat(finalDestinationAccAmount.compareTo(destinationAccountInitialAmount.add(new BigDecimal(1000))))
				.isEqualTo(0);
	}

	// Checking accounts only are allowed to send money to other accounts.But
	// it should return unprocessable entity = 422
	@Test
	public void testSendMoneyFromSavingAccount() {

		CreateUserRequest createUserRequest = new CreateUserRequest();
		createUserRequest.setName("Test Name");
		ResponseEntity<User> response = testRestTemplate.postForEntity("/users", createUserRequest, User.class);

		String sourceAccount = "";
		String destinationAccount = "";
		for (Account account : response.getBody().getAccounts()) {
			if (account.getAccountType().equals(AccountType.SAVING)) {
				sourceAccount = account.getAccountId();
			} else {
				destinationAccount = account.getAccountId();
			}
		}

		ResponseEntity<Account> transferResponse = testRestTemplate.postForEntity("/source-accounts/" + sourceAccount
				+ "/destination-accounts/" + destinationAccount + "/transfer/" + 1000, null, Account.class);
		assertThat(transferResponse.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

	}
}