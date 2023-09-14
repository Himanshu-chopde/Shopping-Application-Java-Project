package database.configuration;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class ConnectDatabase {
	static Configuration cfg = null;
	static SessionFactory factory = null;
	static Session session = null;
	static Transaction transaction = null;
	
	static {
		cfg = new Configuration();
		cfg.configure("database/configuration/hibernate.cfg.xml");
		factory = cfg.buildSessionFactory();
	}
	
	public static Session getSession() {
		return factory.openSession();
	}
	
	public static Transaction getTransaction(Session ses) {
		return ses.beginTransaction();
	}
	
	public static void closeSession() {
		if(session != null) {
			session.close();
		}
	}
	
//	cfg = new Configuration();
//	cfg.configure("database/configuration/hibernate.cfg.xml");
//	factory = cfg.buildSessionFactory();
//	session = factory.openSession();
//	transaction = session.beginTransaction();
}
