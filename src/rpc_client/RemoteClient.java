package rpc_client;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

import modle.Account;
import rpc_server.RemoteCall;

public class RemoteClient {
	
	private Boolean isValid;
	private Account acc;
	
	public RemoteClient() {
		this.isValid = false;
		this.acc = null;
	}

	public Object invoke(RemoteCall call) {
		
		try{
			Socket socket = new Socket("localhost", 8000);
			OutputStream out = socket.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(out);
			InputStream in = socket.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(in);
/*
			RemoteCall call = new RemoteCall("RPCService", "getInformation", new Class[] {String.class},
				new Object[] {"测试 RPC!"});// 向服务器发送Call 对象
*/
			
			oos.writeObject(call);
			// 接收包含了方法执行结果的Call 对象
			call = (RemoteCall) ois.readObject();
			
//			System.out.println(call.getResult());
			ois.close();
			oos.close();
			socket.close();
		} catch (ConnectException e) {
			System.err.print("与服务器连接已断开，请重新连接\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return call.getResult();
	}

	public static void main(String args[]) throws Exception {
		
		RemoteClient client = new RemoteClient();
		
		RemoteCall checkValidCall = new RemoteCall("BankServiceImpl", "getValid", new Class[] {}, new Object[] {});
		client.setIsValid((Boolean) client.invoke(checkValidCall));
		
		while (!client.getIsValid()) {
			int option = 0;
			System.out.println("1.登录\t2.注册");
			Scanner in = new Scanner(System.in);
			option = in.nextInt();
			String name;
			String password;
			switch (option) {
			case 1:
				//登录
				System.out.println("请输入用户名");
				name = in.next();
				System.out.println("请输入密码");
				password = in.next();
				RemoteCall loginCall = new RemoteCall("BankServiceImpl", "login", 
						new Class[] {String.class, String.class}, new Object[] {name, password});
				client.setIsValid((Boolean) client.invoke(loginCall));
				if (client.getIsValid()) {
					System.out.println("登录成功");
				} else {
					System.err.print("登录失败:用户名或密码错误\n");
				}
				break;
			case 2:
				//注册
				System.out.println("请输入用户名");
				name = in.next();
				System.out.println("请输入密码");
				password = in.next();
				RemoteCall registerCall = new RemoteCall("BankServiceImpl", "register",
						new Class[] {String.class, String.class}, new Object[] {name, password});
				client.setIsValid((Boolean) client.invoke(registerCall));
				if (client.getIsValid()) {
					System.out.println("注册成功");
				} else {
					System.err.print("注册失败:该账号已存在或出现其他异常\n");
				}
				break;
			default:
				break;
			}
		}
		
		//获取账户
		RemoteCall getAccountCall = new RemoteCall("BankServiceImpl", "getAccount", new Class[] {}, new Object[] {});
		client.setAcc((Account) client.invoke(getAccountCall));
		
		boolean finished = false;
		while (client.getIsValid() && !finished) {
			try{
				System.out.println("选择服务:\n1.存款\t2.取款\n3.转账\t4.查询余额\n5.退出");
				int choice = 0;
				int sum;
				String target_name;
				String result = null;
				
				Scanner in = new Scanner(System.in);
				choice = in.nextInt();
				switch (choice) {
					case 1:
						//存款
						System.out.println("输入存款金额");
						sum = in.nextInt();
						RemoteCall depositMoneyCall = 
								new RemoteCall("BankServiceImpl", "depositMoney", 
										new Class[] {Account.class, Integer.class}, new Object[] {client.getAcc(), sum});
						result = (String) client.invoke(depositMoneyCall);
						System.out.println(result);
						break;
					case 2:
						//取款
						System.out.println("输入取款金额");
						sum = in.nextInt();
						RemoteCall withdrawMoneyCall = 
								new RemoteCall("BankServiceImpl", "withdrawMoney", 
										new Class[] {Account.class, Integer.class}, new Object[] {client.getAcc(), sum});
						result = (String) client.invoke(withdrawMoneyCall);
						System.out.println(result);
						break;
					case 3:
						//转账
						System.out.println("输入转账目标账户");
						target_name = in.next();
						System.out.println("输入转账金额");
						sum = in.nextInt();
						RemoteCall transferMoneyCall = 
								new RemoteCall("BankServiceImpl", "transferMoney", 
										new Class[] {Account.class, String.class, Integer.class},
											new Object[] {client.getAcc(), target_name, sum});
						result = (String) client.invoke(transferMoneyCall);
						System.out.println(result);
						break;
					case 4:
						//查询余额
						RemoteCall checkBalanceCall = 
						new RemoteCall("BankServiceImpl", "checkBalance", 
								new Class[] {Account.class}, new Object[] {client.getAcc()});
						Integer balance = (Integer) client.invoke(checkBalanceCall);
						System.out.println("当前余额:$" + balance);
						break;
					case 5:
						finished = true;
						RemoteCall logoutCall = new RemoteCall("BankServiceImpl", "setValid", 
								new Class[] {Boolean.class}, new Object[] {false});
						client.invoke(logoutCall);
						break;
					default:
						System.err.print("输入格式错误\n");
						break;
				}
			} catch (InputMismatchException e) {
				System.err.print("请输入数字\n");
			} 
		}
		System.out.println("SYSTEM IS CLOSED");
	}

	public Boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

	public Account getAcc() {
		return acc;
	}

	public void setAcc(Account acc) {
		this.acc = acc;
	}
}
