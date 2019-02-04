
public class Account {
public String accountName = "";
public String accountPw = "";
public String accountPwEncrypted = "";
public String server = "";
public Account(String un,String pw, String serv)
{
	accountName = un;
	accountPw = pw;
	server = serv;
}
}
