package application;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class MainH {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Configuration cfg = null;
		SessionFactory factory = null;
		Session session = null;
		Transaction transaction = null;
		
		cfg = new Configuration();
		cfg.configure("database/configuration/hibernate.cfg.xml");
		factory = cfg.buildSessionFactory();
		session = factory.openSession();
		transaction = session.beginTransaction();
		
		@SuppressWarnings("unchecked")
		Query<String> query = (Query<String>)session.createSQLQuery("select category from ProductList where category = :categoryExist");
    	query.setParameter("categoryExist", "laptop");
    	if(query.list() != null) {
    		System.out.println(query.list());
    		System.out.println("not====================================================");
    	}
    	else if(query.list() == null) {
    		System.out.println("delete=================================================");
    	}
		

	}

}
