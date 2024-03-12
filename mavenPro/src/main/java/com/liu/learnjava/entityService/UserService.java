package com.liu.learnjava.entityService;

import com.liu.learnjava.entity.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class UserService {
	@Autowired
	SessionFactory sessionFactory;
	public User fetchUserById(long id){
		return sessionFactory.getCurrentSession().byId(User.class).load(id);
	}
	public User  getUserById(long id){
		User user = fetchUserById(id);
		if(user == null){
			throw new RuntimeException("user not found by id"+id);
		}
		return user;
	}
	public User fetchUserByEmail(String email){
		User example = new User();
		example.setEmail(email);
		List<User> list = sessionFactory.getCurrentSession().createQuery("from User u where u.email = ?1",User.class).setParameter(1,email).list();
		return list.isEmpty() ? null : list.get(0);
	}
	public User getUserEmail(String email){
		User user = fetchUserByEmail(email);
		if(user == null){
			throw new RuntimeException("user not fount by email"+email);
		}
		return user;
	}
	public List<User> getUsers(int pageIndex){
		int pageSize = 100;
		return sessionFactory.getCurrentSession().createQuery("from User u", User.class).setFirstResult((pageIndex -1)*pageSize).setMaxResults(pageSize).list();
	}
	public User signin(String email,String password){
		List<User> list = sessionFactory.getCurrentSession().createQuery("from User u where u.email = ?1 and u.password = ?2", User.class)
				.setParameter(1,email).setParameter(2,password).list();
		return list.isEmpty() ? null : list.get(0);
	}
	public User login(String email ,String password){
		List<User> list = sessionFactory.getCurrentSession().createNamedQuery("login", User.class)//name query
				.setParameter("e",email).setParameter("pwd",password).list();
		return list.isEmpty() ? null : list.get(0);
	}
//	insert
	public User register(String email, String password, String name){
//		创建一个user对象
		User user = new User();
//		设置各个属性
		user.setName(name);
		user.setPassword(password);
		user.setEmail(email);
//		不用设置id，因为使用了自增主键，
//		保存到数据库
		sessionFactory.getCurrentSession().persist(user);
//		现在已经获得了id
		System.out.println(user.getId()+"-----这是是自增获取到的");
		return user;
	}
//	delete
	public boolean deleteUser(Long id){
		User user = sessionFactory.getCurrentSession().byId(User.class).load(id);
		if(user!= null){
			sessionFactory.getCurrentSession().remove(user);
			return true;
		}
		return  false;
	}
//	update
	public void updateUser(Long id,String name){
		User user = sessionFactory.getCurrentSession().byId(User.class).load(id);
		user.setName(name);
		sessionFactory.getCurrentSession().merge(user);
	}
}
