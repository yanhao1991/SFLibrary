package com.successfactors.library.server.dao;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.successfactors.library.server.hibernate.HibernateSessionFactory;
import com.successfactors.library.shared.QueryType;
import com.successfactors.library.shared.model.SLOrder;

/**
 * 
 * @author haimingli
 *
 */
public class MysqlOrderDao {
	private static final Logger log = Logger.getLogger(MysqlOrderDao.class);
	private Session session = null;

	/**
	 * Construct(do nothing)
	 */
	public MysqlOrderDao() {
		log.debug("MysqlOrderDao construct is running");
	}

	/**
	 * Add a record in database when order a book
	 * @param slOrder
	 * @return whether this action is successful
	 */
	public boolean orderBook(SLOrder slOrder){
		try{
			log.debug("Start: Add order record");
			session = HibernateSessionFactory.getSession();
			session.beginTransaction();
			session.save(slOrder);
			session.getTransaction().commit();
			log.debug("Successful: Add order record");
			return true;
		}catch(HibernateException e){
			e.printStackTrace();
			session.getTransaction().rollback();
			log.error("FAILED class:MysqlDao method:addOrderRecord");
			return false;
		}finally{
			HibernateSessionFactory.closeSession();
		}
	}
	
	/**
	 * Get the SLOrder in the database according to the given orderId
	 * @param orderId
	 * @return SLOrder When exception happens or orderId is wrong the slOrder is null
	 */
	public SLOrder getSLOrderByOrderId(int orderId){
		try {
		      log.debug("Get order record by order id.");
		      session = HibernateSessionFactory.getSession();
		      String hql = null;
		      hql = "from SLOrder as p where p.orderId= ?";
		      Query q = session.createQuery(hql);
		      q.setLong(0, orderId);
		        return (SLOrder) q.uniqueResult();
		    } catch (HibernateException e) {
		      e.printStackTrace();
		      log.error("FAILED class: MysqlOrderDao method: getSLOrderByOrderId");
		      return null;
		    } finally {
		      HibernateSessionFactory.closeSession();
		    }
	}
	
	/**
	 * Update the record of the given slOrder
	 * @param slOrder
	 * @return Whether this action is successful
	 */
	public boolean updateOrder(SLOrder slOrder){
	    try {
	        log.debug("Start: update order");
	        session = HibernateSessionFactory.getSession();
	        session.beginTransaction();
	        session.update(slOrder);
	        session.getTransaction().commit();
	        log.debug("Successful: update order");
	        return true;
	      } catch (HibernateException e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	        log.error("FAILED class: MysqlOrderDao method: updateOrder");
	        return false;
	      } finally {
	        HibernateSessionFactory.closeSession();
	      }
	}
	
	/**
	 * search order list
	 * @param firstType
	 * @param firstValue
	 * @param secondType
	 * @param secondValue
	 * @param itemsPerPage
	 * @param pageNum
	 * @return
	 */
	public List<SLOrder> searchOrderList(String firstType, String firstValue, 
			String secondType, String secondValue, int itemsPerPage, int pageNum){
	    try {
	    	log.debug("Start: query SLOrder");
	        session = HibernateSessionFactory.getSession();
	        StringBuffer sb = new StringBuffer();
	        sb.append("from SLOrder as p ");
	        if(QueryType.ALL != firstType){
	        	sb.append(" where " + getSql(firstType, firstValue));
	        	if(QueryType.ALL != secondType){
	        		sb.append(" and " + getSql(secondType, secondValue));
	        	}
	        }
	        Query q = session.createQuery(sb.toString());
	        q.setFirstResult((pageNum - 1) * itemsPerPage);
	        q.setMaxResults(itemsPerPage);

	        List<SLOrder> results = new ArrayList<SLOrder>();
	        List list = q.list();
	        Iterator it = list.iterator();
	        while (it.hasNext()) {
	          SLOrder bean = (SLOrder) it.next();
	          results.add(bean);
	        }
	        log.debug("Get SLOrders:" + results.size());
	        return results;
	      } catch (HibernateException e) {
	        e.printStackTrace();
	        log.error("FAILED: class MysqlOrderDao method searchOrderList");
	        return null;
	      } finally {
	        HibernateSessionFactory.closeSession();
	      }
	}
	
	/**
	 * Get the count of the searched orders
	 * @param firstType
	 * @param firstValue
	 * @param secondType
	 * @param secondValue
	 * @return -1 if there is any exception
	 */
	public long selectCount(String firstType, String firstValue, 
			String secondType, String secondValue){
	    try {
	    	log.debug("Start: select count");
	        session = HibernateSessionFactory.getSession();
	        StringBuffer sb = new StringBuffer();
	        sb.append("select count(*) from SLOrder as p ");
	        if(!firstType.equals(QueryType.ALL)){
	        	sb.append(" where " + getSql(firstType, firstValue));
	        	if(secondType.equals(QueryType.ALL)){
	        		sb.append(" and " + getSql(secondType, secondValue));
	        	}
	        }
	        Query q = session.createQuery(sb.toString());
	        if (null != q.uniqueResult()){
	        	return (Long) q.uniqueResult();
	        }else{
	        	return 0;
	        }
	      } catch (HibernateException e) {
	        e.printStackTrace();
	        log.error("FAILED: class MysqlOrderDao method selectCount");
	        return -1;
	      } finally {
	        HibernateSessionFactory.closeSession();
	      }
	}
	
	private String getSql(String type, String value){
		StringBuffer sb = new StringBuffer();
		sb.append(" p." + type + " = " + value);
		return sb.toString();
	}
}