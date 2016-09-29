package com.codefict.film;

import java.sql.*;
import java.util.UUID;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Token {
	private static final String TokenValidTimeStr = "(15 / 60 / 24)";
	private static final String PasswdSalt1 = "FILM";
	private static final String PasswdSalt2 = "CAMERA";
	
	private Database DB;
	
	private Token(Database DB) {
		this.DB = DB; 
		
		this.RemoveExpireTokens();
	}
	public static Token ServerInstance(Database DB) { return new Token(DB); }
	
	public void RemoveExpireTokens() {
		String ExpriedTokenRemoveSql = "DELETE FROM tokens WHERE vtime<SYSDATE";
		try { DB.runSql( ExpriedTokenRemoveSql ); }
		catch (SQLException NotExistUser) { }
	}
	public void UpdateTokenValidTime(String Token) {
		String ExpriedTokenRemoveSql = String.format("UPDATE tokens SET vtime=(SYSDATE + %s) WHERE token='%s'"
													, TokenValidTimeStr
													, Token);
		try { DB.runSql( ExpriedTokenRemoveSql ); }
		catch (SQLException NotExistUser) { }
	}
	public String getUserID(String Token) throws NotAuthToken {
		String UserID = "";
		String getUserIDFromTokenSql = String.format("SELECT usr.ID FROM usr JOIN tokens ON usr.u_no=tokens.usr WHERE tokens.token='%s'"
													, Token);
		try { 
			ResultSet rs = DB.runSql( getUserIDFromTokenSql ); 
			rs.next();
			UserID = rs.getString(1).trim();
		}
		catch (SQLException NotAuthorization) {
			throw new NotAuthToken();
		}
		return UserID;
	}

	
	// Login Logic
	public String getLoginToken(String user_id, String user_pwd) {
		String token = "";
		String PasswdOnDB;

		try {
			PasswdOnDB = getPasswdOnDB(user_id);
		}
		catch (NotExistUser e) {
			RegistUser(user_id, user_pwd);
			PasswdOnDB = HashingPasswd(user_id, user_pwd);
		}
		
		boolean AgreePasswd = HashingPasswd(user_id, user_pwd).equals(PasswdOnDB);
		if (!AgreePasswd) {
			token = "Not Matched Password";
			return token;
		}

		do {
			token = MakingToken();
		} while(!UpdateToken(token, user_id));
		
		return token;
	}
	private String getPasswdOnDB(String UserId) throws NotExistUser {
		String Passwd = "";
		String UserCheckQuery = String.format("SELECT pwd FROM usr WHERE id='%s'", UserId);
		try {
			ResultSet rs = DB.runSql( UserCheckQuery );
			rs.next();
			Passwd = rs.getString(1);
		} catch (SQLException NotExistUser) {
			throw new NotExistUser();
		}
		return Passwd;
	}
	private boolean RegistUser(String ID, String Passwd) {
		String HashedPasswd = HashingPasswd(ID, Passwd);
		
		String UserCheckQuery = String.format("INSERT INTO usr (u_no, ID, pwd) VALUES (usr_ai.NEXTVAL, '%s', '%s')"
											, ID
											, HashedPasswd);
		try {
			DB.runSql( UserCheckQuery );
		} catch (SQLException ExistUser) {
			return false;
		}
		return true;
	}
	private String HashingPasswd(String Id, String Passwd) {
		String OneCycleHash = HashingSHA256(Passwd + PasswdSalt1 + Id);
		String HashedPasswd = HashingSHA256(HashingSHA256(Id) + PasswdSalt2 + OneCycleHash);
		return HashedPasswd;
	}
	private static String HashingSHA256(String str){
		String SHA = ""; 
		try{
			MessageDigest sh = MessageDigest.getInstance("SHA-256"); 
			sh.update(str.getBytes()); 
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer(); 
			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			SHA = sb.toString();
		} catch(NoSuchAlgorithmException e){
			e.printStackTrace(); 
			SHA = null; 
		}
		return SHA;
	}
	private String MakingToken() { return UUID.randomUUID().toString();	}
	private boolean UpdateToken(String TokenID, String ID) {
		String UserCheckQuery = String.format("INSERT INTO tokens (t_id, usr, token, vtime) "
											+ "VALUES (tokens_ai.NEXTVAL, (SELECT u_no FROM usr WHERE ID='%s'), '%s', SYSDATE + %s)"
											, ID
											, TokenID
											, TokenValidTimeStr);
		try {
			DB.runSql( UserCheckQuery );
		} catch (SQLException ExistToken) {
			return false;
		}
		return true;
	}

	
	private class NotExistUser extends Exception {
		private static final long serialVersionUID = -2738327184777413628L;

		@Override
		public String getMessage() {
			return "Not Exist User In Token Database";
		}
	}
	public class NotAuthToken extends Exception {
		private static final long serialVersionUID = -7904466226409073171L;

		@Override
		public String getMessage() {
			return "Not Authorization Token";
		}
	}
}
