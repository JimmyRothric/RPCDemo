package rpc_service;

import modle.Account;

public interface BankService {
	public Boolean login(String name, String password);
	
	public Boolean register(String name, String password);
	
	public Integer checkBalance(Account acc);
	
	public String depositMoney(Account acc, Integer sum);
	
	public String withdrawMoney(Account acc, Integer sum);
	
	public String transferMoney(Account current_acc, String target_name, Integer sum);
}
