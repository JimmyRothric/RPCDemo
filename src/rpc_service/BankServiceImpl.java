package rpc_service;

import dao.AccountDao;
import modle.Account;

public class BankServiceImpl implements BankService {
	private Account account;
	private boolean isValid;
	public BankServiceImpl() {
		account = null;
		isValid = false;
	}
	
	@Override
	public Boolean login(String name, String password) {

		AccountDao dao = new AccountDao();
	
		try {
			account = dao.isValid(name, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (account == null) {
			return false;
		} else {
			isValid = true;
			return true;
		}
	}
	
	@Override
	public Boolean register(String name, String password) {
		
		AccountDao dao = new AccountDao();
		
		boolean isExisted = false;
		boolean success = false;
		try {
			isExisted = dao.isExistedName(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (isExisted) {
			return false;
		} else {
			account = new Account(name, password, 0);
			try {
				success = dao.addAccount(account);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (success) {
				isValid = true;
			}
		}
		return success;
	}
	
	public Account getAccoutByName(String name) {
		
		AccountDao dao = new AccountDao();
		Account acc = null;
		
		try {
			acc = dao.findAccountByName(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return acc;
	}
	
	@Override
	//查询余额
	public Integer checkBalance(Account acc) {	
		
		AccountDao dao = new AccountDao();
		int balance = 0;
		
		try {
			balance = dao.checkBalance(acc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return balance;
	}
	
	@Override
	//存款
	public String depositMoney(Account acc, Integer sum) {
		
		AccountDao dao = new AccountDao();
		boolean success = false;
		
		if (sum < 0){
			return ("存款失败:输入正整数金额\n");
		}
		try {
			success = dao.AccessMoney(acc, sum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (success) {
			return ("存款成功\t当前余额:$" + checkBalance(acc));
		} else {
			return ("存款失败:DEPOSIT MONEY ERROR\n");
		}
	}
	
	@Override
	//取款
	public String withdrawMoney(Account acc, Integer sum) {
		
		AccountDao dao = new AccountDao();
		boolean success = false;
		
		if (sum < 0){
			return ("取款失败:输入正整数金额\n");
		}
		if (checkBalance(acc) < sum) {
			return ("取款失败:余额不足\n");
		} else {
			try {
				success = dao.AccessMoney(acc, -sum);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (success) {
			return ("取款成功\t当前余额:$" + checkBalance(acc));
		} else {
			return ("取款失败:WITHDRAW MONEY ERROR\n");
		}
	}
	
	@Override
	//转账
	public String transferMoney(Account current_acc, String target_name, Integer sum) {
		AccountDao dao = new AccountDao();
		if (current_acc.getName().equals(target_name)) {
			return ("转账失败:无法自转账给当前账户\n");
		}
		if (sum < 0){
			return ("转账失败:输入正整数金额\n");
		}
		try {
		
			if (checkBalance(current_acc) < sum) { 
				return ("转账失败:余额不足\n");
			} else if (!dao.isExistedName(target_name)) {
				return ("转账失败:目标账户不存在\n");
			} else {
				Account target_acc = getAccoutByName(target_name);
				boolean withdraw_completed = dao.AccessMoney(current_acc, -sum);
				if (withdraw_completed) {
					boolean disposit_completed = dao.AccessMoney(target_acc, sum);
					if (disposit_completed){
						return ("转账成功\t当前余额:$" + checkBalance(current_acc));
					} else {
						dao.AccessMoney(current_acc, sum);
						return ("转账失败:TRANSFER MONEY ERROR\n");
					}
					
				} else {
					return ("转账失败:TRANSFER MONEY ERROR\n");
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ("转账失败:TRANSFER MONEY ERROR\n");
		
	}
			
	/**
	 * Getter & Setter
	 */
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Boolean getValid() {
		return isValid;
	}

	public void setValid(Boolean isValid) {
		this.isValid = isValid;
	}
}
