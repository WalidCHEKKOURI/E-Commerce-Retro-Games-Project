package net.kzn.retrogamesbackend.daoimpl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.kzn.retrogamesbackend.dao.UserDAO;
import net.kzn.retrogamesbackend.dto.Address;
import net.kzn.retrogamesbackend.dto.Cart;
import net.kzn.retrogamesbackend.dto.User;


@Repository("userDAO")
@Transactional
public class UserDAOImpl implements UserDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public boolean addUser(User user) {
		try {			
			sessionFactory.getCurrentSession().persist(user);			
			return true;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean addAddress(Address address) {
		try {			
			sessionFactory.getCurrentSession().persist(address);			
			return true;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean addCart(Cart cart) {
		try {			
			sessionFactory.getCurrentSession().persist(cart);			
			return true;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
}