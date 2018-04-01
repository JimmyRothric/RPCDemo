package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import modle.Account;

public class AccountDao extends BaseDao{
	public AccountDao() {
		// TODO Auto-generated constructor stub
		super();
	}
	public boolean addAccount(Account acc) throws Exception {
		String sql = "insert into Account values(?, ?, ?)";
		try {
			Connection con = super.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, acc.getName());
			stmt.setString(2, acc.getPassword());
			stmt.setInt(3, acc.getBalance());
			stmt.executeUpdate();
			stmt.close();
			con.close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean isExistedName(String name) throws Exception {
		String sql = "select * from Account where Name = ?";
		boolean isExistedName = false;
		try {
			Connection con = super.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				isExistedName = true;
			}
			stmt.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isExistedName;
	}
	
	public Account findAccountByName(String name) throws Exception {
		String sql = "select * from Account where Name = ?";
		Account acc = null;
		try {
			Connection con = super.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				acc = new Account(rs.getString(1), rs.getString(2), rs.getInt(3));
			}
			stmt.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return acc;
	}
	
	public Account isValid(String name, String password) throws Exception {
		String sql = "select * from Account where name = ? and password = ?";
		try {
			Connection con = super.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, name);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			Account acc = null;
			if (rs.next()) {
				acc = new Account(rs.getString(1), rs.getString(2), rs.getInt(3));
			}
			stmt.close();
			con.close();
			return acc;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public int checkBalance(Account acc) throws Exception {
		String sql = "select balance from Account where name = ?";
		int balance = 0;
		try {
			Connection con = super.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, acc.getName());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				balance = rs.getInt(1);
			}
			stmt.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return balance;
	}
	
	public boolean AccessMoney(Account acc, int sum) throws Exception {
		String sql = "update Account set balance = ? where name = ?";
		boolean success = false;
		try {
			Connection con = super.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, checkBalance(acc) + sum);
			stmt.setString(2, acc.getName());
			stmt.executeUpdate();
			stmt.close();
			con.close();
			success = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}
	
}
